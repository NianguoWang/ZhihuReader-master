package com.wangng.zhihureader.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.adapter.MainPageAdapter;
import com.wangng.zhihureader.base.BaseView;
import com.wangng.zhihureader.data.DataManager;
import com.wangng.zhihureader.data.model.Theme;
import com.wangng.zhihureader.ui.edittheme.AllThemeActivity;
import com.wangng.zhihureader.util.ThreadManager;
import com.wangng.zhihureader.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFragment extends Fragment implements BaseView {

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.arrow_more)
    ImageView mMoreTheme;
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        DataManager.getInstance().getSubscribedTheme()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Theme.ThemeBean>>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("onCompleted!!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage(), e);
                    }

                    @Override
                    public void onNext(List<Theme.ThemeBean> themeBeen) {
                        setupViewpager(themeBeen);
                    }
                });
    }

    private void setupViewpager(List<Theme.ThemeBean> subscribed) {
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    mFab.setVisibility(View.VISIBLE);
                } else {
                    mFab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        mTablayout.setupWithViewPager(mViewPager);
        titles.add("首页");
        fragments.add(new Home1Fragment());
        for (int i = 0; i < subscribed.size() ; i++) {
            final Theme.ThemeBean themeBean = subscribed.get(i);
            fragments.add(StoryFragment.newInstance(themeBean));
            titles.add(themeBean.name);
        }
        MainPageAdapter adapter = new MainPageAdapter(getChildFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode != 100 || resultCode != 101) {
            return;
        }
        final ArrayList<Theme.ThemeBean> themes = (ArrayList<Theme.ThemeBean>) data.getSerializableExtra("themes");
        ArrayList<Theme.ThemeBean> subscribed = new ArrayList<>();
        for (Theme.ThemeBean theme : themes) {
            if(theme.order == 1) {
                subscribed.add(theme);
            }
        }
        setupViewpager(subscribed);
        ThreadManager.getShortPool().execute(new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance().saveThemeList(themes);
            }
        });
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.arrow_more)
    public void onClick() {
        UIHelper.showAllTheme(getActivity(), 100);
    }
}
