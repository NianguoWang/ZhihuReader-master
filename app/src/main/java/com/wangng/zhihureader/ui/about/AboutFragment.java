package com.wangng.zhihureader.ui.about;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.wangng.zhihureader.R;
import com.wangng.zhihureader.customtabs.CustomFallback;
import com.wangng.zhihureader.customtabs.CustomTabActivityHelper;
import com.wangng.zhihureader.data.local.PreferenceUtil;
import com.wangng.zhihureader.util.CopyTextToClipboardUtil;
import com.wangng.zhihureader.util.ToastUtil;
import com.wangng.zhihureader.util.UIHelper;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by wng on 2017/2/26.
 */
public class AboutFragment extends PreferenceFragmentCompat {

    private CustomTabsIntent.Builder customTabsIntent;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.fragment_about);

        customTabsIntent = new CustomTabsIntent.Builder();
        customTabsIntent.setToolbarColor(getActivity().getResources().getColor(R.color.colorPrimary));
        customTabsIntent.setShowTitle(true);

        findPreference("version").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //TODO
                return false;
            }
        });

        findPreference("author").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showEasterEgg();
                return false;
            }
        });

        findPreference("follow_me_on_zhihu").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                followOnZhihu();
                return false;
            }
        });

        findPreference("add_friend_wechat").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                addFriendInWecaht();
                return false;
            }
        });

        findPreference("feedback").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                feedback();
                return false;
            }
        });

        findPreference("coffee").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                donateDeveloper();
                return false;
            }
        });

        findPreference("open_source_license").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                UIHelper.showOpenLicense(getActivity());
                return false;
            }
        });

        final Preference followOnGithub = findPreference("follow_me_on_github");
        followOnGithub.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                followOnGithub();
                return false;
            }
        });

    }

    private void followOnGithub() {
        if (PreferenceUtil.getBoolean(getActivity(), "in_app_browser",true)){
            CustomTabActivityHelper.openCustomTab(
                    getActivity(),
                    customTabsIntent.build(),
                    Uri.parse(getActivity().getString(R.string.github_url)),
                    new CustomFallback() {
                        @Override
                        public void openUri(Activity activity, Uri uri) {
                            super.openUri(activity, uri);
                        }
                    });
        } else {
            try{
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse( getActivity().getString(R.string.github_url))));
            } catch (android.content.ActivityNotFoundException ex){
                //TODO 处理错误
            }
        }
    }

    private void followOnZhihu() {
        if (PreferenceUtil.getBoolean(getActivity(), "in_app_browser",true)){
            CustomTabActivityHelper.openCustomTab(
                    getActivity(),
                    customTabsIntent.build(),
                    Uri.parse(getActivity().getString(R.string.zhihu_url)),
                    new CustomFallback() {
                        @Override
                        public void openUri(Activity activity, Uri uri) {
                            super.openUri(activity, uri);
                        }
                    });
        } else {
            try{
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse( getActivity().getString(R.string.zhihu_url))));
            } catch (android.content.ActivityNotFoundException ex){
                //TODO 处理错误
            }
        }
    }

    private void donateDeveloper() {
        UIHelper.showDialog(getContext(), R.string.donate, R.string.donate_content, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                copyTextToClipboard(getActivity().getString(R.string.donate_account));
            }
        });
    }

    private void addFriendInWecaht() {
        UIHelper.showDialog(getContext(), R.string.add_friend_wechat, R.string.add_friend_wechat_message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                copyTextToClipboard(getActivity().getString(R.string.add_wechat_number));
            }
        });
    }

    private void copyTextToClipboard(String text) {
        CopyTextToClipboardUtil.copyTextToClipboard(getActivity(), text);
    }

    /**
     * 发邮件反馈
     */
    private void feedback() {
        try{
            Uri uri = Uri.parse(getActivity().getString(R.string.sendto));
            Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
            intent.putExtra(Intent.EXTRA_SUBJECT, getActivity().getString(R.string.mail_topic));
            intent.putExtra(Intent.EXTRA_TEXT,
                    getActivity().getString(R.string.device_model) + Build.MODEL + "\n"
                            + getActivity().getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
                            + getActivity().getString(R.string.version));
            getActivity().startActivity(intent);
        }catch (android.content.ActivityNotFoundException ex){

        }
    }

    long lastTimeMillis = 0;
    byte count = 5;
    private void showEasterEgg() {
        long currentTimeMillis = SystemClock.uptimeMillis();
        if(count == 5) {
            count--;
            lastTimeMillis = currentTimeMillis;
            ToastUtil.showToast(getActivity(), "再点击" + count + "进入彩蛋。");
        } else {
            if(currentTimeMillis - lastTimeMillis < 800) {
                count --;
                lastTimeMillis = currentTimeMillis;
                if(count == 0) {
                    UIHelper.showEasterEgg(getActivity());
                } else {
                    ToastUtil.showToast(getActivity(), "再点击" + count + "进入彩蛋。");
                }
            } else {
                count = 5;
            }
        }
    }
}
