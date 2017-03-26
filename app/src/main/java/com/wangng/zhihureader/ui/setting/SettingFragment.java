package com.wangng.zhihureader.ui.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.wangng.zhihureader.R;
import com.wangng.zhihureader.data.local.PreferenceUtil;
import com.wangng.zhihureader.util.UIHelper;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;

/**
 * Created by wng on 2017/2/25.
 */
public class SettingFragment extends PreferenceFragmentCompat {

    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.fragment_setting);

        findPreference("no_picture_mode").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                PreferenceUtil.putBoolean(mContext, "no_picture_mode", preference.getSharedPreferences().getBoolean("no_picture_mode", false));
                return false;
            }
        });
        findPreference("in_app_browser").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                PreferenceUtil.putBoolean(mContext, "in_app_browser", preference.getSharedPreferences().getBoolean("in_app_browser", false));
                return false;
            }
        });
        findPreference("clear_image_cache").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                clearImageCahce();
                return false;
            }
        });
        final Preference cacheDuration = findPreference("time_of_saving_articles");
        cacheDuration.setSummary(PreferenceUtil.getString(mContext, "time_of_saving_articles", "7天"));
        cacheDuration.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                cacheDuration.setSummary((String) newValue);
                PreferenceUtil.putString(mContext, "time_of_saving_articles", preference.getSharedPreferences().getString("time_of_saving_articles", "7天"));
                return true;
            }
        });
    }

    private void clearImageCahce() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("正在删除……");
        progressDialog.show();
        Observable.just("a")
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        progressDialog.dismiss();
                        UIHelper.showToast(mContext, "清除成功！");
                    }
                });
    }
}
