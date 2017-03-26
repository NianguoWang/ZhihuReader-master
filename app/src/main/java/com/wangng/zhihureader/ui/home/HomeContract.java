package com.wangng.zhihureader.ui.home;

import com.wangng.zhihureader.base.BasePresenter;
import com.wangng.zhihureader.base.BaseView;
import com.wangng.zhihureader.data.model.News;

/**
 * Created by wng on 2017/2/20.
 */

public interface HomeContract {
    interface View extends BaseView {
        void showLoading();
        void dismissLoading();
        void showSuccess(News news);
        void showError();
    }

    abstract class Presenter extends BasePresenter {

    }
}
