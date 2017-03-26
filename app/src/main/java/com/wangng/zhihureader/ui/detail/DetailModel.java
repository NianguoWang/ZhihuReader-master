package com.wangng.zhihureader.ui.detail;

import com.wangng.zhihureader.data.model.StoryDetail;

import rx.Observable;

/**
 * Created by 小爱 on 2017/3/24.
 */

public class DetailModel implements DetailContract.Model {
    @Override
    public Observable<StoryDetail> getStorydetail(int id) {
        return DATA_MANAGER.getStorydetail(id);
    }

    @Override
    public boolean ifBookmarked(int id) {
        return DATA_MANAGER.ifBookmarded(id);
    }

    @Override
    public boolean bookmarkStory(int id) {
        return DATA_MANAGER.bookmarkStory(id);
    }

    @Override
    public boolean unbookmarkStory(int id) {
        return DATA_MANAGER.unbookmarkStroy(id);
    }
}
