package com.wangng.zhihureader.ui.home;

import com.wangng.zhihureader.base.BaseModel;
import com.wangng.zhihureader.base.BasePresenter;
import com.wangng.zhihureader.base.BaseView;
import com.wangng.zhihureader.data.model.News;
import com.wangng.zhihureader.data.model.Story;

import java.util.List;

import rx.Observable;

/**
 * Created by 小爱 on 2017/3/26.
 */

public interface Home1Contract {

    interface View extends BaseView {
        void startLoading();
        void stopLoading();
        void showListView(boolean clear, List<Story> stories);
        void showLoadError(boolean first, boolean clear, long before);
        void addHeaderView(List<Story> mTopStories);
        void removeHeaderView();
    }

    interface Model extends BaseModel {
        Observable<News> getHomeStoryList();
        Observable<News> getBeforeHomeStoryList(long before);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getHomeStoryList();
        abstract void getBeforeHomeStoryList(boolean clear, long before);
    }
}
