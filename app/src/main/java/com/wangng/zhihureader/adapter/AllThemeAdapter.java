package com.wangng.zhihureader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.base.OnThemeChangedListener;
import com.wangng.zhihureader.data.DataManager;
import com.wangng.zhihureader.data.model.Theme;
import com.wangng.zhihureader.ui.edittheme.AllThemeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wng on 2017/3/20.
 */

public class AllThemeAdapter extends RecyclerView.Adapter<AllThemeAdapter.AllThemeViewHolder>{

    private Context mContext;
    private ArrayList<Theme.ThemeBean> mThemes;
    private OnThemeChangedListener mListener;
    public AllThemeAdapter(Context context, List<Theme.ThemeBean> themeBeen, OnThemeChangedListener listener) {
        mContext = context;
        mThemes = (ArrayList<Theme.ThemeBean>) themeBeen;
        mListener = listener;

    }

    @Override
    public AllThemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllThemeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_all_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(final AllThemeViewHolder holder, final int position) {
        final Theme.ThemeBean themeBean = mThemes.get(position);
        holder.themeBean = themeBean;
        holder.mThemeName.setText(themeBean.name);
        holder.mSwitch.setChecked(themeBean.order == 1);
        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onThemeChanged(holder.themeBean, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mThemes.size();
    }

    static class AllThemeViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public TextView mThemeName;
        public SwitchCompat mSwitch;
        public Theme.ThemeBean themeBean;
        public AllThemeViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mThemeName = (TextView) itemView.findViewById(R.id.theme_name);
            mSwitch = (SwitchCompat) itemView.findViewById(R.id.switch_button);
        }
    }
}
