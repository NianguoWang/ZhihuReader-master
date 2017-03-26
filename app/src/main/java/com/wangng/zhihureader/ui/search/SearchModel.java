package com.wangng.zhihureader.ui.search;

import com.wangng.zhihureader.data.model.Story;

import java.util.List;

import rx.Observable;

/**
 * Created by 小爱 on 2017/3/23.
 */

public class SearchModel implements SearchContract.Model {
    @Override
    public Observable<List<Story>> searchForResult(String keyWord) {
        return DATA_MANAGER.searchBookmarkedStory(keyWord);
    }

    @Override
    public boolean unbookmarkStroy(int id) {
        return DATA_MANAGER.unbookmarkStroy(id);
    }
}
