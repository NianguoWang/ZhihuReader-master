package com.wangng.zhihureader.ui.home;

import com.wangng.zhihureader.base.BaseModel;
import com.wangng.zhihureader.base.BasePresenter;
import com.wangng.zhihureader.base.BaseView;
import com.wangng.zhihureader.data.model.StoryList;

import rx.Observable;

/**
 * Created by wng on 2017/3/24.
 */

public interface StoryContract {

    interface View extends BaseView {
        void startLoading();
        void stopLoading();
        void showLoadError();
        void showRecycleView(StoryList storyList);
    }

    interface Model extends BaseModel {
        Observable<StoryList> getThemeStoryList(int themeId);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getThemeStoryList(int themeId);
    }
}
