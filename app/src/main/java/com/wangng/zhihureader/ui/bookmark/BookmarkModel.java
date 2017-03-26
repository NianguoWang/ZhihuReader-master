package com.wangng.zhihureader.ui.bookmark;

import com.wangng.zhihureader.data.model.Story;

import java.util.List;

import rx.Observable;

/**
 * Created by wng on 2017/3/24.
 */

public class BookmarkModel implements BookmarkContract.Model {
    @Override
    public Observable<List<Story>> getBookmarkedStory() {
        return DATA_MANAGER.getBookmarkedStory();
    }

    @Override
    public boolean unbookmarkStory(int id) {
        return DATA_MANAGER.unbookmarkStroy(id);
    }
}
