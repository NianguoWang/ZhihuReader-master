package com.wangng.zhihureader.ui.search;

import com.wangng.zhihureader.data.model.Story;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 小爱 on 2017/3/23.
 */

public class SearchPresenter extends SearchContract.Presenter {
    @Override
    void searchForResult(String keyWord) {
        mView.startLoading();
        mModel.searchForResult(keyWord).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Story>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.stopLoading();
                        mView.showSearchFail();
                    }

                    @Override
                    public void onNext(List<Story> stories) {
                        mView.stopLoading();
                        mView.showRecycleView(stories);
                    }
                });
    }

    @Override
    void unbookmarkStroy(int id) {
        mModel.unbookmarkStroy(id);
    }
}
