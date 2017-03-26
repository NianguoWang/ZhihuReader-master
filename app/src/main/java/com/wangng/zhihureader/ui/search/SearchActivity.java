package com.wangng.zhihureader.ui.search;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.wangng.zhihureader.R;
import com.wangng.zhihureader.adapter.BookmarkAdapter;
import com.wangng.zhihureader.base.BaseActivity;
import com.wangng.zhihureader.base.OnRecycleViewItemClickListener;
import com.wangng.zhihureader.data.DataManager;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.util.ToastUtil;
import com.wangng.zhihureader.util.UIHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends BaseActivity<SearchPresenter, SearchModel> implements SearchContract.View,OnRecycleViewItemClickListener<Story> {

    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private List<Story> mStories;

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mPresenter.searchForResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.searchForResult(newText);
                return true;
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void startLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSearchFail() {
        ToastUtil.showSnackBar(mSearchView, R.string.unbookmark_fail);
    }

    @Override
    public void showRecycleView(List<Story> stories) {
        mStories = stories;
        BookmarkAdapter adapter = null;
        if(adapter == null) {
            adapter = new BookmarkAdapter(this, stories, this);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRecycleViewItemClick(Story story) {
        UIHelper.showStoryDetail(this, story.id);
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
            mPresenter.unbookmarkStroy(story.id);
            mStories.remove(story);
            mRecyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };
}
