package com.wangng.zhihureader.base;

import android.content.Context;

/**
 * Created by wng on 2017/2/16.
 */

public abstract class BasePresenter<M, V> {
    public M mModel;
    public V mView;
    protected Context mContext;

    public void attachVM(V v, M m) {
        this.mModel = m;
        this.mView = v;
    }

    public void setContext (Context context) {
        mContext = context;
    }

    public void detachVM() {
        mView = null;
        mModel = null;
    }
}
