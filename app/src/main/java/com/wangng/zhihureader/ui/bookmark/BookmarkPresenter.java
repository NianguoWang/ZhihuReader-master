package com.wangng.zhihureader.ui.bookmark;

import com.wangng.zhihureader.R;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.util.ToastUtil;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 小爱 on 2017/3/24.
 */

public class BookmarkPresenter extends BookmarkContract.Presenter {
    @Override
    void getBookmarkStory() {
        mView.startLoading();
        mModel.getBookmarkedStory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Story>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.stopLoading();
                        mView.showLoadError();
                    }

                    @Override
                    public void onNext(List<Story> stories) {
                        mView.stopLoading();
                        if(stories.size() > 1) {
                            mView.showRecyclerView(stories);
                        }
                    }
                });
    }

    @Override
    void unbookmarkStory(int id) {
        mModel.unbookmarkStory(id);
    }
}
