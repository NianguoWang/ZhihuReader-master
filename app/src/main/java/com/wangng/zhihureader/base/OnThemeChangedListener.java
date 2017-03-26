package com.wangng.zhihureader.base;

import com.wangng.zhihureader.data.model.Theme;

/**
 * Created by wng on 2017/3/20.
 */

public interface OnThemeChangedListener {
    void onThemeChanged(Theme.ThemeBean themeBean, boolean isChecked);
}
