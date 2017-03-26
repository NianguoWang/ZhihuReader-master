package com.wangng.zhihureader.ui.home;

import com.wangng.zhihureader.data.model.News;

import rx.Observable;

/**
 * Created by wng on 2017/3/26.
 */

public class Home1Model implements Home1Contract.Model {
    @Override
    public Observable<News> getHomeStoryList() {
        return DATA_MANAGER.getHomeStoryList();
    }

    @Override
    public Observable<News> getBeforeHomeStoryList(long before) {
        return DATA_MANAGER.getBeforeHomeStoryList(before);
    }
}
