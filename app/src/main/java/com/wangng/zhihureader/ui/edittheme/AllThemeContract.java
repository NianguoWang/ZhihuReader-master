package com.wangng.zhihureader.ui.edittheme;

import com.wangng.zhihureader.base.BaseModel;
import com.wangng.zhihureader.base.BasePresenter;
import com.wangng.zhihureader.base.BaseView;
import com.wangng.zhihureader.data.model.Theme;

import java.util.List;

import rx.Observable;

/**
 * Created by 小爱 on 2017/3/24.
 */

public interface AllThemeContract {

    interface View extends BaseView {
        void showLoadError();
        void showRecycleView(List<Theme.ThemeBean> themeBeen);
    }

    interface Model extends BaseModel {
        Observable<List<Theme.ThemeBean>> getAllTheme();
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getAllTheme();
    }
}
