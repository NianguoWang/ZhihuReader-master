package com.wangng.zhihureader.ui.bookmark;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.adapter.BookmarkAdapter;
import com.wangng.zhihureader.base.BaseFragment;
import com.wangng.zhihureader.base.OnRecycleViewItemClickListener;
import com.wangng.zhihureader.data.DataManager;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.util.ToastUtil;
import com.wangng.zhihureader.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BookmarkFragment extends BaseFragment<BookmarkPresenter, BookmarkModel> implements BookmarkContract.View, OnRecycleViewItemClickListener<Story> {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;

    private List<Story> mStories = new ArrayList<>();
    private BookmarkAdapter adapter;

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
                mPresenter.getBookmarkStory();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(layoutManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecycleView);
    }

    @Override
    protected void initData() {
        mPresenter.getBookmarkStory();
    }

    @Override
    public void showRecyclerView(List<Story> stories) {
        mStories.clear();
        mStories.addAll(stories);
        if(adapter == null) {
            adapter = new BookmarkAdapter(mContext, mStories, this);
            mRecycleView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
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
        ToastUtil.showSnackBar(mSwipeRefreshLayout, R.string.load_error);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bookmark, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if(itemId == R.id.action_search) {
            UIHelper.showSearch(mContext);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecycleViewItemClick(Story story) {
        UIHelper.showStoryDetail(mContext, story.id);
    }

    private ItemTouchHelper.Callback mCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = 0;
            int swipeFlags = ItemTouchHelper.START;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final Story story = mStories.get(viewHolder.getAdapterPosition());
            mPresenter.unbookmarkStory(story.id);
            mStories.remove(story);
            mRecycleView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };

}
