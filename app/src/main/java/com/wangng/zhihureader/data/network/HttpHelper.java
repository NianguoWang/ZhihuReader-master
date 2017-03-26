package com.wangng.zhihureader.data.network;

import com.wangng.zhihureader.data.model.News;
import com.wangng.zhihureader.data.model.StoryDetail;
import com.wangng.zhihureader.data.model.StoryList;
import com.wangng.zhihureader.data.model.Theme;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by 小爱 on 2017/2/21.
 * 网络请求帮助类
 */

public class HttpHelper{
    public static String BASE_URL = "http://news-at.zhihu.com/";
    private static HttpHelper INSTANCE;
    private static Retrofit mRetrofit;
    private HttpHelper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public static HttpHelper getInstance() {
        if(INSTANCE == null) {
            synchronized (HttpHelper.class) {
                if(INSTANCE == null) {
                    INSTANCE = new HttpHelper();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取主题列表
     * @return
     */
    public Observable<Theme> getTheme() {
        return mRetrofit.create(Api.class)
                .getTheme();
    }

    /**
     * 获取最新的消息列表
     * @return
     */
    public Observable<News> getLatestNews() {
        return mRetrofit.create(Api.class)
                .getHomeStory();
    }

    /**
     * 获取过往的消息
     * @param date 具体时间
     * @return
     */
    public Observable<News> getBeforeNews(String date) {
        return mRetrofit.create(Api.class)
                .getBeforeHomeStory(date);
    }

    /**
     * 获取某个主题的内容列表
     * @param id 主题的id
     * @return
     */
    public Observable<StoryList> getThemeStories(int id) {
        return mRetrofit.create(Api.class)
                .getThemeStories(id);
    }

    /**
     * 获取story详情
     * @param id story的id
     * @return
     */
    public Observable<StoryDetail> getStoryDetail(int id) {
        return mRetrofit.create(Api.class)
                .getStoryDetail(id);
    }

}
