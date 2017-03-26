package com.wangng.zhihureader.ui.edittheme;

import com.wangng.zhihureader.R;
import com.wangng.zhihureader.data.model.Theme;
import com.wangng.zhihureader.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 小爱 on 2017/3/24.
 */

public class AllThemePresenter extends AllThemeContract.Presenter {
    @Override
    void getAllTheme() {
        mModel.getAllTheme()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Theme.ThemeBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoadError();
                    }

                    @Override
                    public void onNext(List<Theme.ThemeBean> themeBeen) {
                        mView.showRecycleView(themeBeen);
                    }
                });
    }
}
