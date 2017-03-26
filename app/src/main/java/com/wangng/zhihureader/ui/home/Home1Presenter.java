package com.wangng.zhihureader.ui.home;

import com.orhanobut.logger.Logger;
import com.wangng.zhihureader.data.model.News;
import com.wangng.zhihureader.data.model.Story;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 小爱 on 2017/3/26.
 */

public class Home1Presenter extends Home1Contract.Presenter {

    @Override
    void getHomeStoryList() {
        mModel.getHomeStoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<News>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        mView.stopLoading();
                        mView.showLoadError(true, true, 0);
                    }

                    @Override
                    public void onNext(News news) {
                        mView.stopLoading();
                        mView.addHeaderView(news.top_stories);
                        mView.showListView(true, news.stories);
                    }
                });
    }

    @Override
    void getBeforeHomeStoryList(final boolean clear, final long before) {
        mModel.getBeforeHomeStoryList(before)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<News>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadError(false, clear, before);
                    }

                    @Override
                    public void onNext(News news) {
                        if(clear) {
                            mView.removeHeaderView();
                        }
                        mView.showListView(clear, news.stories);
                    }
                });
    }
}
