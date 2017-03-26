package com.wangng.zhihureader.ui.home;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.adapter.HomeAdapter;
import com.wangng.zhihureader.base.BaseFragment;
import com.wangng.zhihureader.base.OnRecycleViewItemClickListener;
import com.wangng.zhihureader.data.DataManager;
import com.wangng.zhihureader.data.model.News;
import com.wangng.zhihureader.data.model.Story;
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

public class HomeFragment extends BaseFragment implements OnRecycleViewItemClickListener<Story> {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    private FloatingActionButton mFab;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private List<Story> mStories = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_home;
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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取最后一个完全显示的item position
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部并且是向下滑动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        Calendar c = Calendar.getInstance();
                        c.set(mYear, mMonth, --mDay);
                        loadMore(c.getTimeInMillis(), false);
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
                // 隐藏或者显示fab
                if(dy > 0) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
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
        DataManager.getInstance().getHomeStoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<News>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        mSwipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(mRecycleView, R.string.network_error, Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                initData();
                            }
                        }).show();
                    }

                    @Override
                    public void onNext(News news) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mStories.clear();
                        mStories.addAll(news.stories);
                        showRecycleView(news.top_stories);
                    }
                });
    }

    private void showRecycleView(List<Story> topStories) {
        HomeAdapter homeAdapter = null;
        if(homeAdapter == null) {
            homeAdapter = new HomeAdapter(mContext, mStories, this);
            homeAdapter.setTopStories(topStories);
            mRecycleView.setAdapter(homeAdapter);
        } else {
            homeAdapter.notifyDataSetChanged();
        }
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
                loadMore(temp.getTimeInMillis(), true);
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

    private void loadMore(long before, final boolean clear) {
        DataManager.getInstance().getBeforeHomeStoryList(before)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<News>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showSnackBar(mFab, R.string.network_error1);
                    }

                    @Override
                    public void onNext(News news) {
                        if(clear) {
                            mStories.clear();
                        }
                        mStories.addAll(news.stories);
                        showRecycleView(null);
                    }
                });
    }

    @Override
    public void onRecycleViewItemClick(Story story) {
        UIHelper.showStoryDetail(mContext, story.id);
    }
}
