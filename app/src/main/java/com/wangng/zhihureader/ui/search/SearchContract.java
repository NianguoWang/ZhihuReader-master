package com.wangng.zhihureader.ui.search;

import com.wangng.zhihureader.base.BaseModel;
import com.wangng.zhihureader.base.BasePresenter;
import com.wangng.zhihureader.base.BaseView;
import com.wangng.zhihureader.data.model.Story;

import java.util.List;

import rx.Observable;

/**
 * Created by wng on 2017/3/23.
 */

public interface SearchContract {
    interface View extends BaseView{
        void startLoading();
        void stopLoading();
        void showSearchFail();
        void showRecycleView(List<Story> stories);

    }

    interface Model extends BaseModel{
        Observable<List<Story>> searchForResult(String keyWord);
        boolean unbookmarkStroy(int id);
    }

    abstract class Presenter extends BasePresenter<Model, View>{
        abstract void searchForResult(String keyWord);
        abstract void unbookmarkStroy(int id);
    }
}
