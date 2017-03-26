package com.wangng.zhihureader.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wangng.zhihureader.util.TUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 小爱 on 2017/3/23.
 */

public abstract class BaseActivity<P extends BasePresenter, M extends BaseModel> extends AppCompatActivity {

    public P mPresenter;
    public M mModel;
    protected Context mContext;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        unbinder = ButterKnife.bind(this);
        mContext = this;

        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.attachVM(this, mModel);
            mPresenter.setContext(mContext);
        }

        initView();
        initData();
    }

    protected abstract void initData();

    protected abstract int getLayoutResID();

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
