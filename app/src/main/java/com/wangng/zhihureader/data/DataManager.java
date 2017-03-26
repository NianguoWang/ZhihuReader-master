package com.wangng.zhihureader.data;

import android.content.Context;
import android.text.TextUtils;

import com.wangng.zhihureader.App;
import com.wangng.zhihureader.data.local.CacheDao;
import com.wangng.zhihureader.data.local.PreferenceUtil;
import com.wangng.zhihureader.data.model.News;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.data.model.StoryDetail;
import com.wangng.zhihureader.data.model.StoryList;
import com.wangng.zhihureader.data.model.Theme;
import com.wangng.zhihureader.data.network.HttpHelper;
import com.wangng.zhihureader.util.NetworkState;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wng on 2017/3/4.
 * 单例模式，用来管理数据的。
 */

public class DataManager{
    private volatile static DataManager mInstance;

    private Context mContext;
    private CacheDao mDbHelper;
    private HttpHelper mNetHelper;

    private DataManager() {
        mContext = App.getApp();
        mDbHelper = CacheDao.getInstance();
        mNetHelper = HttpHelper.getInstance();
    }

    public static DataManager getInstance() {
        if(mInstance == null) {
            synchronized (DataManager.class) {
                if(mInstance == null) {
                    mInstance = new DataManager();
                }
            }
        }
        return mInstance;
    }

    public Observable<List<Theme.ThemeBean>> getSubscribedTheme() {
        return mDbHelper.getSubscribedTheme()
                .flatMap(new Func1<List<Theme.ThemeBean>, Observable<List<Theme.ThemeBean>>>() {
            @Override
            public Observable<List<Theme.ThemeBean>> call(List<Theme.ThemeBean> themeBeen) {
                if(themeBeen.size() == 0) {
                    return mNetHelper.getTheme()
                            .map(new Func1<Theme, List<Theme.ThemeBean>>() {
                                @Override
                                public List<Theme.ThemeBean> call(Theme theme) {
                                    return theme.others;
                                }
                            })
                            .doOnNext(new Action1<List<Theme.ThemeBean>>() {
                                @Override
                                public void call(List<Theme.ThemeBean> themeBeen) {
                                    for (int i = 0; i < 3; i++) {
                                        themeBeen.get(i).order = 1;
                                    }
                                    mDbHelper.insertThemeList(themeBeen);
                                }
                            })
                            .map(new Func1<List<Theme.ThemeBean>, List<Theme.ThemeBean>>() {
                                @Override
                                public List<Theme.ThemeBean> call(List<Theme.ThemeBean> themeBeen) {
                                    return themeBeen.subList(0, 3);
                                }
                            });
                }
                return Observable.just(themeBeen);
            }
        });
    }

    public void saveThemeList(List<Theme.ThemeBean> themes) {
        mDbHelper.insertThemeList(themes);
    }

    public Observable<List<Theme.ThemeBean>> getAllTheme() {
        return mDbHelper.getAllTheme();
    }

    public Observable<StoryDetail> getStorydetail(final int id) {
        if(NetworkState.networkConnected(mContext)) {
            return mNetHelper.getStoryDetail(id).doOnNext(new Action1<StoryDetail>() {
                @Override
                public void call(StoryDetail storyDetail) {
                    mDbHelper.updateContent(storyDetail);
                }
            });
        } else {
            return mDbHelper.getContentById(id);
        }
    }

    public boolean bookmarkStory(int id) {
        return mDbHelper.bookmarkStory(id);
    }

    public boolean unbookmarkStroy(int id) {
        return mDbHelper.unbookmarkStroy(id);
    }

    public boolean ifBookmarded(int id) {
        return mDbHelper.ifBookmarded(id);
    }

    public Observable<StoryList> getThemeStoryList(final int themeId) {
        if(NetworkState.networkConnected(mContext)) {
            return mNetHelper.getThemeStories(themeId).doOnNext(new Action1<StoryList>() {
                @Override
                public void call(StoryList storyList) {
                    saveStory(storyList.stories, themeId, System.currentTimeMillis());
                }
            });
        } else {
            return mDbHelper.getThemeStoryList(themeId);
        }
    }

    public Observable<News> getHomeStoryList() {
        if(NetworkState.networkConnected(mContext)) {
            return mNetHelper.getLatestNews().doOnNext(new Action1<News>() {
                @Override
                public void call(News news) {
                    try {
                        DateFormat format = new SimpleDateFormat("yyyyMMdd");
                        Date date = format.parse(news.date);
                        saveStory(news.stories, 0, date.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            return mDbHelper.getHomeStoryList();
        }
    }

    public Observable<News> getBeforeHomeStoryList(final long before) {
        String sDate;
        Date d = new Date(before + 24*60*60*1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        sDate = format.format(d);
        return mNetHelper.getBeforeNews(sDate).doOnNext(new Action1<News>() {
            @Override
            public void call(News news) {
                saveStory(news.stories, 0, before);
            }
        });
    }

    public void saveStory(List<Story> stories, int themeId, long date) {
        mDbHelper.insertStoryList(stories, themeId, date);
    }


    public Observable<List<Story>> getBookmarkedStory() {
        return mDbHelper.getBookmarkedStories();
    }

    public Observable<List<Story>> searchBookmarkedStory(String keyWord) {
        return mDbHelper.searchStories(keyWord);
    }

    public void clearOverdueStories() {
        mDbHelper.clearOverdueStories();
    }

    public boolean getPictureMode() {
        return PreferenceUtil.getBoolean(mContext, "no_picture_mode");
    }
}
