package com.wangng.zhihureader.ui.edittheme;

import com.wangng.zhihureader.data.model.Theme;

import java.util.List;

import rx.Observable;

/**
 * Created by 小爱 on 2017/3/24.
 */

public class AllThemeModel implements AllThemeContract.Model {
    @Override
    public Observable<List<Theme.ThemeBean>> getAllTheme() {
        return DATA_MANAGER.getAllTheme();
    }
}
