package com.wangng.zhihureader.ui.edittheme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;

import com.jaeger.recyclerviewdivider.RecyclerViewDivider;
import com.orhanobut.logger.Logger;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.adapter.AllThemeAdapter;
import com.wangng.zhihureader.base.BaseActivity;
import com.wangng.zhihureader.base.OnThemeChangedListener;
import com.wangng.zhihureader.data.DataManager;
import com.wangng.zhihureader.data.model.Theme;
import com.wangng.zhihureader.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 小爱 on 2017/3/20.
 */

public class AllThemeActivity extends BaseActivity<AllThemePresenter, AllThemeModel> implements AllThemeContract.View, OnThemeChangedListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    private Unbinder unbinder;
    private ArrayList<Theme.ThemeBean> mThemes;
    private AllThemeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_theme);

        unbinder = ButterKnife.bind(this);
        toolbar.setTitle(R.string.home_show);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupRecyclerView();
        initData();
    }

    private void setupRecyclerView() {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewDivider divider = new RecyclerViewDivider.Builder(this)
                .setStyle(RecyclerViewDivider.Style.END)
                .build();
        recycleView.addItemDecoration(divider);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(recycleView);
    }

    @Override
    public void initData() {
        mPresenter.getAllTheme();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_all_theme;
    }

    @Override
    protected void initView() {
        toolbar.setTitle(R.string.home_show);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupRecyclerView();
    }

    @Override
    public void showLoadError() {
        ToastUtil.showSnackBar(recycleView, R.string.load_error);
    }

    @Override
    public void showRecycleView(List<Theme.ThemeBean> themeBeen) {
        mThemes = (ArrayList<Theme.ThemeBean>) themeBeen;
        mAdapter = new AllThemeAdapter(this, themeBeen, this);
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("themes", mThemes);
        setResult(101, intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private ItemTouchHelper.Callback mCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //可以上下拖拽
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            //设置不能所有滑动
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                //分别把中间所有的item的位置重新交换
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mThemes, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mThemes, i, i - 1);
                }
            }
            mAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
        }
    };

    @Override
    public void onThemeChanged(Theme.ThemeBean themeBean, boolean isChecked) {
        int index = mThemes.indexOf(themeBean);
        mThemes.get(index).order = isChecked ? 1 : 0;
    }
}
