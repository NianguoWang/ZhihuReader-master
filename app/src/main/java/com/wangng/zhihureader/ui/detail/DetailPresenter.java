package com.wangng.zhihureader.ui.detail;

import android.content.res.Configuration;
import android.text.Html;

import com.wangng.zhihureader.data.model.StoryDetail;
import com.wangng.zhihureader.util.CopyTextToClipboardUtil;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 小爱 on 2017/3/24.
 */

public class DetailPresenter extends DetailContract.Presenter {

    private StoryDetail mStoryDetail;
    @Override
    void getStorydetail(int id) {
        mView.startLoading();
        mModel.getStorydetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StoryDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.stopLoading();
                        mView.showLoadError();
                    }

                    @Override
                    public void onNext(StoryDetail storyDetail) {
                        mView.stopLoading();
                        mStoryDetail = storyDetail;
                        mView.setCollapsingToolbarLayoutTitle(storyDetail.getTitle());
                        mView.loadImage(storyDetail.getImage());
                        String body = storyDetail.getBody();
                        if (body == null) {
                            mView.loadUrl(storyDetail.getShare_url());
                        } else {
                            mView.loadDataWithBaseURL(convertBody(body));
                        }
                    }
                });
    }

    @Override
    boolean ifBookmarked(int id) {
        return mModel.ifBookmarked(id);
    }

    @Override
    void bookmarkStory() {
        if(mModel.bookmarkStory(mStoryDetail.getId())){
            mView.showBookmarkSuccess();
        } else {
            mView.showBookmarkFail();
        }
    }

    @Override
    void unbookmarkStory() {
        if(mModel.unbookmarkStory(mStoryDetail.getId())) {
            mView.showUnbookmarkSuccess();
        } else {
            mView.showUnbookmarkFail();
        }
    }

    @Override
    void copyLink() {
        CopyTextToClipboardUtil.copyTextToClipboard(mContext, Html.fromHtml(mStoryDetail.getShare_url()));
        mView.showCopySuccess();
    }

    @Override
    void copyText() {
        CopyTextToClipboardUtil.copyTextToClipboard(mContext, Html.fromHtml(mStoryDetail.getBody()));
        mView.showCopySuccess();
    }

    @Override
    void shareAsText() {
        mView.shareAsText(mStoryDetail.getTitle(), mStoryDetail.getShare_url());
    }

    @Override
    void openInBrowser() {
        mView.openInBrowser(mStoryDetail.getShare_url());
    }

    @Override
    void showbottomSheetDialog() {
        if(mStoryDetail == null) {
            mView.showLoadError();
        } else {
            mView.showbottomSheetDialog();
        }
    }

    private String convertBody(String preResult) {

        preResult = preResult.replace("<div class=\"img-place-holder\">", "");
        preResult = preResult.replace("<div class=\"headline\">", "");

        // 在api中，css的地址是以一个数组的形式给出，这里需要设置
        // in fact,in api,css addresses are given as an array
        // api中还有js的部分，这里不再解析js
        // javascript is included,but here I don't use it
        // 不再选择加载网络css，而是加载本地assets文件夹中的css
        // use the css file from local assets folder,not from network
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";


        // 根据主题的不同确定不同的加载内容
        // load content judging by different theme
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if ((mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES) {
            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
        }

        return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(preResult)
                .append("</body></html>").toString();
    }
}
