package com.wangng.zhihureader.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.wangng.zhihureader.App;
import com.wangng.zhihureader.data.model.News;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.data.model.StoryDetail;
import com.wangng.zhihureader.data.model.StoryList;
import com.wangng.zhihureader.data.model.Theme;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 小爱 on 2017/2/25.
 */

public class CacheDao implements IcacheDAO{

    private static CacheDao INSTANCE;
    private final BriteDatabase mDatabaseHelper;
    private CacheOpenHelper mCache;

    private static Func1<Cursor, Theme.ThemeBean> mThemeMapperFunction = new Func1<Cursor, Theme.ThemeBean>() {
        @Override
        public Theme.ThemeBean call(Cursor cursor) {
            Theme.ThemeBean theme = new Theme.ThemeBean();
            theme.id = cursor.getInt(cursor.getColumnIndex(CacheOpenHelper.ThemeColumns.THEME_ID));
            theme.color = cursor.getInt(cursor.getColumnIndex(CacheOpenHelper.ThemeColumns.COLOR));
            theme.name = cursor.getString(cursor.getColumnIndex(CacheOpenHelper.ThemeColumns.NAME));
            theme.description = cursor.getString(cursor.getColumnIndex(CacheOpenHelper.ThemeColumns.DESCRIPTION));
            theme.thumbnail = cursor.getString(cursor.getColumnIndex(CacheOpenHelper.ThemeColumns.THUMBNAIL));
            theme.order = cursor.getInt(cursor.getColumnIndex(CacheOpenHelper.ThemeColumns.SHOWINHOME));
            return theme;
        }
    };

    private static Func1<Cursor, Story> mStoryMapperFunction = new Func1<Cursor, Story>() {
        @Override
        public Story call(Cursor cursor) {
            Story story = new Story();
            story.id = cursor.getInt(cursor.getColumnIndex(CacheOpenHelper.StoryColumns.STORY_ID));
            story.title = cursor.getString(cursor.getColumnIndex(CacheOpenHelper.StoryColumns.TITLE));
            story.type = cursor.getInt(cursor.getColumnIndex(CacheOpenHelper.StoryColumns.TYPE));
            ArrayList<String> images = new ArrayList<>();
            final String image = cursor.getString(cursor.getColumnIndex(CacheOpenHelper.StoryColumns.IMAGE));
            if(image != null) {
                images.add(image);
                story.images = images;
            }
            return story;
        }
    };

    private static Func1<Cursor, StoryDetail> mStorydetailMapperFunction = new Func1<Cursor, StoryDetail>() {
        @Override
        public StoryDetail call(Cursor cursor) {
            StoryDetail storyDetail = new StoryDetail();
            storyDetail.setId(cursor.getColumnIndex(CacheOpenHelper.StoryColumns.STORY_ID));
            storyDetail.setTitle(cursor.getString(cursor.getColumnIndex(CacheOpenHelper.StoryColumns.TITLE)));
            storyDetail.setBody(cursor.getString(cursor.getColumnIndex(CacheOpenHelper.StoryColumns.BODY)));
            ArrayList<String> images = new ArrayList<>();
            final String image = cursor.getString(cursor.getColumnIndex(CacheOpenHelper.StoryColumns.IMAGE));
            if(image != null) {
                images.add(image);
                storyDetail.setImages(images);
                storyDetail.setImage(image);
            }
            return storyDetail;
        }
    };

    private CacheDao() {
        mCache = new CacheOpenHelper(App.getApp());
        SqlBrite sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Logger.d(message);
            }
        });
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(mCache, Schedulers.io());
        mDatabaseHelper.setLoggingEnabled(true);
    }

    public static CacheDao getInstance() {
        if(INSTANCE == null) {
            synchronized (CacheDao.class) {
                if(INSTANCE == null) {
                    INSTANCE = new CacheDao();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public int insertThemeList(List<Theme.ThemeBean> themes) {
        checkNotNull(themes, "themes can not be null!");
        final SQLiteDatabase db = mCache.getWritableDatabase();
        db.delete(CacheOpenHelper.TABLE_NAME_THEME, null, null);
        int count = 0;
        for (int i = 0 ; i < themes.size() ; i++) {
            final Theme.ThemeBean theme = themes.get(i);
            ContentValues values = new ContentValues();
            values.put(CacheOpenHelper.ThemeColumns.THEME_ID, theme.id);
            values.put(CacheOpenHelper.ThemeColumns.COLOR, theme.color);
            values.put(CacheOpenHelper.ThemeColumns.NAME, theme.name);
            values.put(CacheOpenHelper.ThemeColumns.DESCRIPTION, theme.description);
            values.put(CacheOpenHelper.ThemeColumns.THUMBNAIL, theme.thumbnail);
            values.put(CacheOpenHelper.ThemeColumns.SHOWINHOME, theme.order);
            long insert = db.insert(CacheOpenHelper.TABLE_NAME_THEME, null, values);
            if(insert != -1) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Observable<List<Theme.ThemeBean>> getSubscribedTheme() {
        String sql = "SELECT * FROM " + CacheOpenHelper.TABLE_NAME_THEME + " WHERE " + CacheOpenHelper.ThemeColumns.SHOWINHOME + " > 0";
        return mDatabaseHelper.createQuery(CacheOpenHelper.TABLE_NAME_THEME, sql).mapToList(mThemeMapperFunction);
    }

    @Override
    public Observable<List<Theme.ThemeBean>> getAllTheme() {
        String sql = "SELECT * FROM " + CacheOpenHelper.TABLE_NAME_THEME ;
        return mDatabaseHelper.createQuery(CacheOpenHelper.TABLE_NAME_THEME, sql).mapToList(mThemeMapperFunction);
    }

    @Override
    public int insertStoryList(List<Story> stories, int themeId, long date) {
            checkNotNull(stories, "stories can not be null!");
            int count = 0;
            for (Story story : stories) {
                ContentValues values = new ContentValues();
                values.put(CacheOpenHelper.StoryColumns.STORY_ID, story.id);
                values.put(CacheOpenHelper.StoryColumns.TITLE, story.title);
                final ArrayList<String> images = story.images;
                String image = null;
                if(images != null && images.size() > 0) {
                    image = images.get(0);
                }
                values.put(CacheOpenHelper.StoryColumns.IMAGE, image);
                values.put(CacheOpenHelper.StoryColumns.TYPE, story.type);
                values.put(CacheOpenHelper.StoryColumns.THEME_ID, themeId);
                values.put(CacheOpenHelper.StoryColumns.TIME, date);
                //为了插入的数据不重复，我把story 的id设置成了主键，但是这样的划重复插入同一条数据又会报主键冲突的错，导致插入数据库的操作终止；
                //这是我们不想要的，所以在调用insert方法的时候传入SQLiteDatabase.CONFLICT_IGNORE参数，如果主键冲突，就直接忽略了。
                final long insert = mCache.getWritableDatabase().insertWithOnConflict(CacheOpenHelper.TABLE_NAME_STORY, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            if(insert != -1) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean updateContent(StoryDetail content) {
        checkNotNull(content, "content can not be null!");
        checkNotNull(content.getBody(), "content.getBody() can not be null!");
        ContentValues values = new ContentValues();
        values.put(CacheOpenHelper.StoryColumns.BODY, content.getBody());
        String whereClause = CacheOpenHelper.StoryColumns.STORY_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(content.getId())};
        final int update = mCache.getWritableDatabase().update(CacheOpenHelper.TABLE_NAME_STORY, values, whereClause, whereArgs);
        return update > 0;
    }

    @Override
    public boolean bookmarkStory(int id) {
        ContentValues values = new ContentValues();
        values.put(CacheOpenHelper.StoryColumns.BOOKMARK, 1);
        String whereClause = CacheOpenHelper.StoryColumns.STORY_ID + "=?";
        String whereArgs = String.valueOf(id);
        final int update = mDatabaseHelper.update(CacheOpenHelper.TABLE_NAME_STORY, values, whereClause, whereArgs);
        return update > 0;
    }

    @Override
    public boolean unbookmarkStroy(int id) {
        ContentValues values = new ContentValues();
        values.put(CacheOpenHelper.StoryColumns.BOOKMARK, 0);
        String whereClause = CacheOpenHelper.StoryColumns.STORY_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        final int update = mCache.getWritableDatabase().update(CacheOpenHelper.TABLE_NAME_STORY, values, whereClause, whereArgs);
        return update > 0;
    }

    @Override
    public boolean ifBookmarded(int id) {
        final SQLiteDatabase db = mCache.getWritableDatabase();
        String[] columns = new String[]{CacheOpenHelper.StoryColumns.BOOKMARK};
        String selection = CacheOpenHelper.StoryColumns.STORY_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        final Cursor cursor = db.query(CacheOpenHelper.TABLE_NAME_STORY, columns, selection, selectionArgs, null, null, null);
        if(cursor != null && cursor.moveToNext()) {
            final int bookmarked = cursor.getInt(cursor.getColumnIndex(CacheOpenHelper.StoryColumns.BOOKMARK));
            return bookmarked == 1;
        }
        return false;
    }

    @Override
    public Observable<List<Story>> getBookmarkedStories() {
        String sql = "SELECT * FROM " + CacheOpenHelper.TABLE_NAME_STORY + " where " + CacheOpenHelper.StoryColumns.BOOKMARK + "=1 order by " + CacheOpenHelper.StoryColumns.STORY_ID + " desc";
        return mDatabaseHelper.createQuery(CacheOpenHelper.TABLE_NAME_STORY, sql).mapToList(mStoryMapperFunction);
    }

    @Override
    public Observable<StoryList> getThemeStoryList(int id) {
        String sql = "SELECT * FROM " + CacheOpenHelper.TABLE_NAME_STORY + " where " + CacheOpenHelper.StoryColumns.THEME_ID + "=?";
        String whereArgs = String.valueOf(id);
        return mDatabaseHelper.createQuery(CacheOpenHelper.TABLE_NAME_STORY, sql, whereArgs)
                .mapToList(mStoryMapperFunction)
                .flatMap(new Func1<List<Story>, Observable<StoryList>>() {
            @Override
            public Observable<StoryList> call(List<Story> stories) {
                StoryList storyList = new StoryList();
                storyList.stories = stories;
                return Observable.just(storyList);
            }
        });
    }

    @Override
    public Observable<News> getHomeStoryList() {
        String sql = "SELECT * FROM " + CacheOpenHelper.TABLE_NAME_STORY + " where " + CacheOpenHelper.StoryColumns.THEME_ID + "=0";
        return mDatabaseHelper.createQuery(CacheOpenHelper.TABLE_NAME_STORY, sql)
                .mapToList(mStoryMapperFunction)
                .flatMap(new Func1<List<Story>, Observable<News>>() {
                    @Override
                    public Observable<News> call(List<Story> stories) {
                        News news = new News();
                        news.stories = stories;
                        return Observable.just(news);
                    }
                });
    }

    @Override
    public Observable<List<Story>> searchStories(String keyWord) {
        String sql = "SELECT * FROM " + CacheOpenHelper.TABLE_NAME_STORY + " where " + CacheOpenHelper.StoryColumns.BOOKMARK + "=1 and "
                + CacheOpenHelper.StoryColumns.TITLE + " like '%" + keyWord + "%'";
        return mDatabaseHelper.createQuery(CacheOpenHelper.TABLE_NAME_STORY, sql).mapToList(mStoryMapperFunction);
    }

    @Override
    public Observable<StoryDetail> getContentById(int id) {
        String sql = "SELECT * FROM " + CacheOpenHelper.TABLE_NAME_STORY + " where " + CacheOpenHelper.StoryColumns.STORY_ID + "=?";
        String whereArgs = String.valueOf(id);
        return mDatabaseHelper.createQuery(CacheOpenHelper.TABLE_NAME_STORY, sql, whereArgs).mapToOne(mStorydetailMapperFunction);
    }

    @Override
    public void clearOverdueStories() {
        final String cacheTime = PreferenceUtil.getString(App.getApp(), "time_of_saving_articles");
        final long l = Long.parseLong(cacheTime);
        final long nowInMillis = System.currentTimeMillis();
        String whereClause = CacheOpenHelper.StoryColumns.TIME + "<? and " + CacheOpenHelper.StoryColumns.BOOKMARK + "!=1";
        String whereArgs = String.valueOf(nowInMillis);
        mDatabaseHelper.delete(CacheOpenHelper.TABLE_NAME_STORY, whereClause, whereArgs);
    }
}
