package com.wangng.zhihureader.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangng.zhihureader.util.TUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wng on 2017/3/23.
 */

public abstract class BaseFragment<P extends BasePresenter, M extends BaseModel> extends Fragment {

    private View mRootView;
    protected Context mContext;
    public P mPresenter;
    public M mModel;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView == null) {
            mRootView = inflater.inflate(getLayoutResID(), null);
        }
        unbinder = ButterKnife.bind(this, mRootView);

        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.attachVM(this, mModel);
            mPresenter.setContext(mContext);
        }
        initView();
        initData();
        return mRootView;
    }

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
