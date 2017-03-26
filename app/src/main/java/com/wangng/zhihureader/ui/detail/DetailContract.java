package com.wangng.zhihureader.ui.detail;

import com.wangng.zhihureader.base.BaseModel;
import com.wangng.zhihureader.base.BasePresenter;
import com.wangng.zhihureader.base.BaseView;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.data.model.StoryDetail;

import rx.Observable;

/**
 * Created by wng on 2017/3/24.
 */

public interface DetailContract {

    interface View extends BaseView {
        void startLoading();
        void stopLoading();
        void loadUrl(String url);
        void loadDataWithBaseURL(String data);
        void showLoadError();
        void showCopySuccess();
        void setCollapsingToolbarLayoutTitle(String title);
        void loadImage(String url);
        void shareAsText(String title, String url);
        void openInBrowser(String url);
        void showbottomSheetDialog();
        void showBookmarkSuccess();
        void showBookmarkFail();
        void showUnbookmarkSuccess();
        void showUnbookmarkFail();
    }

    interface Model extends BaseModel {
        Observable<StoryDetail> getStorydetail(int id);
        boolean ifBookmarked(int id);
        boolean bookmarkStory(int id);
        boolean unbookmarkStory(int id);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        abstract void getStorydetail(int id);
        abstract boolean ifBookmarked(int id);
        abstract void bookmarkStory();
        abstract void unbookmarkStory();
        abstract void copyLink();
        abstract void copyText();
        abstract void shareAsText();
        abstract void openInBrowser();
        abstract void showbottomSheetDialog();
    }
}
