package com.wangng.zhihureader.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.wangng.zhihureader.R;
import com.wangng.zhihureader.ui.bookmark.BookmarkFragment;
import com.wangng.zhihureader.util.UIHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_MAIN_FRAGMENT = "mainFragment";
    private static final String TAG_BOOKMARK_FRAGMENT = "bookmarkFragment";

    private Context mContext;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private MainFragment mMainFragment;
    private BookmarkFragment mBookmarkFragment;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        if(savedInstanceState != null) {
            mMainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, TAG_MAIN_FRAGMENT);
            mBookmarkFragment = (BookmarkFragment) getSupportFragmentManager().getFragment(savedInstanceState, TAG_BOOKMARK_FRAGMENT);
        } else {
            mMainFragment = new MainFragment();
            mBookmarkFragment = new BookmarkFragment();
        }

        if(!mMainFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, mMainFragment).commit();

        }
        if(!mBookmarkFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.container,mBookmarkFragment).commit();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showHomeFragment();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void showHomeFragment() {
        getSupportFragmentManager().beginTransaction().show(mMainFragment).hide(mBookmarkFragment).commit();
        toolbar.setTitle(R.string.home);
    }

    private void showBookmarkFragment() {
        getSupportFragmentManager().beginTransaction().show(mBookmarkFragment).hide(mMainFragment).commit();
        toolbar.setTitle(R.string.bookmark);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mMainFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            showHomeFragment();
            navigationView.setCheckedItem(R.id.nav_home);
        } else if (id == R.id.nav_bookmark) {
            showBookmarkFragment();
            navigationView.setCheckedItem(R.id.nav_bookmark);
        } else if (id == R.id.nav_setting) {
            UIHelper.showSetting(mContext);
        } else if (id == R.id.nav_about) {
            UIHelper.showAbout(mContext);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if(mMainFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, TAG_MAIN_FRAGMENT, mMainFragment);
        }
        if(mBookmarkFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, TAG_BOOKMARK_FRAGMENT, mBookmarkFragment);
        }
    }

}
