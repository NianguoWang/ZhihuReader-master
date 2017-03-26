package com.wangng.zhihureader.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.orhanobut.logger.Logger;

/**
 * Created by wng on 2017/2/22.
 */

public class CacheOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "table_zhihudaily_cache.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME_STORY = "zhihu_story";
    public static final String TABLE_NAME_THEME = "zhihu_theme";

    public CacheOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder table_story = new StringBuilder();
        table_story.append("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME_STORY).append("(")
                .append(StoryColumns.STORY_ID).append(" integer primary key not null,")
                .append(StoryColumns.TITLE).append(" TEXT,")
                .append(StoryColumns.IMAGE).append(" TEXT,")
                .append(StoryColumns.TYPE).append(" integer,")
                .append(StoryColumns.BODY).append(" TEXT,")
                .append(StoryColumns.TIME).append(" real,")
                .append(StoryColumns.THEME_ID).append(" integer,")
                .append(StoryColumns.BOOKMARK).append(" integer default 0)");
        db.execSQL(table_story.toString());

        StringBuilder table_theme = new StringBuilder();
        table_theme.append("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME_THEME).append("(")
                .append(ThemeColumns._ID).append(" integer primary key autoincrement,")
                .append(ThemeColumns.THEME_ID).append(" TEXT,")
                .append(ThemeColumns.COLOR).append(" TEXT,")
                .append(ThemeColumns.NAME).append(" TEXT,")
                .append(ThemeColumns.DESCRIPTION).append(" TEXT,")
                .append(ThemeColumns.THUMBNAIL).append(" TEXT,")
                .append(ThemeColumns.SHOWINHOME).append(" integer default 0)");
        Logger.d(table_theme);
        db.execSQL(table_theme.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public interface StoryColumns extends BaseColumns {
        String STORY_ID = "story_id";
        String TYPE = "type";
        String TITLE = "title";
        String IMAGE = "image";
        String BODY = "body";
        String TIME = "time";
        String BOOKMARK = "bookmark";
        String THEME_ID = "theme_id";//对应着哪个主题下的story
    }

    public interface ThemeColumns extends BaseColumns {
        String THEME_ID = "theme_id";
        String COLOR = "color";
        String NAME = "name";
        String DESCRIPTION = "description";
        String THUMBNAIL = "thumbnail";
        String SHOWINHOME = "show_in_home";//显示在首页的顺序
    }
}
