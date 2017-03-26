package com.wangng.zhihureader.ui.home;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.adapter.StoryAdapter;
import com.wangng.zhihureader.base.BaseFragment;
import com.wangng.zhihureader.base.OnRecycleViewItemClickListener;
import com.wangng.zhihureader.data.DataManager;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.data.model.StoryList;
import com.wangng.zhihureader.data.model.Theme;
import com.wangng.zhihureader.util.ToastUtil;
import com.wangng.zhihureader.util.UIHelper;

import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StoryFragment extends BaseFragment<StoryPresenter, StoryModel> implements OnRecycleViewItemClickListener<Story>, StoryContract.View {

    private static final String THEME_BEAN = "theme_bean";
    private Theme.ThemeBean mThemeBean;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    public StoryFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static StoryFragment newInstance(Theme.ThemeBean themeBean) {
        StoryFragment fragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putParcelable(THEME_BEAN, themeBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThemeBean = getArguments().getParcelable(THEME_BEAN);
        }
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        mPresenter.getThemeStoryList(mThemeBean.id);
    }

    @Override
    public void startLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadError() {
        ToastUtil.showSnackBar(mRecycleView, R.string.network_error, R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    public void showRecycleView(StoryList storyList) {
        StoryAdapter adapter = null;
        if(adapter == null) {
            adapter = new StoryAdapter(mContext, storyList, this);
            mRecycleView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRecycleViewItemClick(Story story) {
        Logger.d(story.toString());
        UIHelper.showStoryDetail(mContext, story.id);
    }
}
