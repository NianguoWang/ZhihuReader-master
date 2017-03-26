package com.wangng.zhihureader.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.adapter.Home1Adapter;
import com.wangng.zhihureader.adapter.TopStoryPagerAdapter;
import com.wangng.zhihureader.base.BaseFragment;
import com.wangng.zhihureader.base.OnRecycleViewItemClickListener;
import com.wangng.zhihureader.data.DataManager;
import com.wangng.zhihureader.data.model.News;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.util.DensityUtil;
import com.wangng.zhihureader.util.ToastUtil;
import com.wangng.zhihureader.util.UIHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wng on 2017/3/25.
 */

public class Home1Fragment extends BaseFragment<Home1Presenter, Home1Model> implements Home1Contract.View, OnRecycleViewItemClickListener<Story> {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.list_view)
    ListView mListview;

    private boolean mHasAddHeaderView;
    private boolean mIsLoading;
    private int mLastVisibleItem;
    private int mTotalItemCount;
    private int mDistance;
    private FloatingActionButton mFab;
    private View headerView;
    private ViewPager mViewPager;

    private Home1Adapter adapter = null;
    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private List<Story> mTopStories = new ArrayList<>();
    private List<Story> mStories = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                int currentItem = mViewPager.getCurrentItem();
                currentItem++;
                if(currentItem > mTopStories.size() - 1) {
                    currentItem = 0;
                }
                mViewPager.setCurrentItem(currentItem, true);
                sendEmptyMessageDelayed(0, 3000);
            }
        }
    };

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_home1;
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
        mSwipeRefreshLayout.setRefreshing(true);

        mListview.setOnScrollListener(new AbsListView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(mTotalItemCount == mLastVisibleItem && scrollState == SCROLL_STATE_IDLE) {
                    if(!mIsLoading) {
                        mIsLoading = true;
                        Calendar c = Calendar.getInstance();
                        c.set(mYear, mMonth, --mDay);
                        mPresenter.getBeforeHomeStoryList(false, c.getTimeInMillis());
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mLastVisibleItem = firstVisibleItem + visibleItemCount;
                mTotalItemCount = totalItemCount;
            }
        });

        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickDialog();
            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.getHomeStoryList();
    }

    @Override
    public void addHeaderView(List<Story> top_stories) {
        if(mHasAddHeaderView) {
            return;
        }
        mTopStories = top_stories;
        headerView = View.inflate(mContext, R.layout.autoplay_viewpager, null);
        mViewPager = (ViewPager) headerView.findViewById(R.id.view_pager);
        final LinearLayout container = (LinearLayout) headerView.findViewById(R.id.ll_container);
        final ImageView redPoint = (ImageView) headerView.findViewById(R.id.iv_red_point);
        for (int i = 0; i < top_stories.size(); i++) {
            ImageView point = new ImageView(mContext);
            point.setImageResource(R.drawable.shape_point_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = DensityUtil.dip2px(mContext, 10);
            }
            point.setLayoutParams(params);
            container.addView(point);
        }
        TopStoryPagerAdapter pagerAdapter = new TopStoryPagerAdapter(mContext, top_stories);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int leftMargin = (int) (mDistance * positionOffset + position
                        * mDistance);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) redPoint
                        .getLayoutParams();
                params.leftMargin = leftMargin;

                redPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        redPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mDistance = container.getChildAt(1).getLeft()
                                - container.getChildAt(0).getLeft();
                        redPoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
        handler.sendEmptyMessageDelayed(0, 3000);
        mListview.addHeaderView(headerView);
        mHasAddHeaderView = true;
    }

    @Override
    public void removeHeaderView() {
        mHasAddHeaderView = false;
        mListview.removeHeaderView(headerView);
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
    public void showListView(boolean clear, List<Story> stories) {
        mIsLoading = false;
        if(clear) {
            mStories.clear();
        }
        mStories.addAll(stories);
        if(adapter == null) {
            adapter = new Home1Adapter(mContext, mStories, this);
            mListview.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoadError(final boolean first, final boolean clear, final long before) {
        mIsLoading = false;
        ToastUtil.showSnackBar(mSwipeRefreshLayout, R.string.network_error, R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first) {
                    mPresenter.getHomeStoryList();
                } else {
                    if(before == 0) {
                        throw new RuntimeException("timeMillis can not be zero!");
                    }
                    mPresenter.getBeforeHomeStoryList(clear, before);
                }
            }
        });
    }

    private void showPickDialog() {
        Calendar now = Calendar.getInstance();
        now.set(mYear, mMonth, mDay);
        DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                Calendar temp = Calendar.getInstance();
                temp.clear();
                temp.set(year, monthOfYear, dayOfMonth);
                mPresenter.getBeforeHomeStoryList(true, temp.getTimeInMillis());
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        dialog.setMaxDate(Calendar.getInstance());
        Calendar minDate = Calendar.getInstance();
        // 2013.5.20是知乎日报api首次上线
        minDate.set(2013, 5, 20);
        dialog.setMinDate(minDate);
        dialog.vibrate(false);

        dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void onRecycleViewItemClick(Story story) {
        UIHelper.showStoryDetail(mContext, story.id);
    }
}
