package com.wangng.zhihureader.data.local;

import com.wangng.zhihureader.data.model.News;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.data.model.StoryDetail;
import com.wangng.zhihureader.data.model.StoryList;
import com.wangng.zhihureader.data.model.Theme;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import rx.Observable;

/**
 * Created by 小爱 on 2017/2/25.
 */

public interface IcacheDAO {

    /**
     * 插入所有主题
     * @param themes
     * @return
     */
    int insertThemeList(List<Theme.ThemeBean> themes);

    /**
     * 获取用户订阅的主题
     * @return
     */
    Observable<List<Theme.ThemeBean>> getSubscribedTheme();

    /**
     * 获取所有的主题
     * @return
     */
    Observable<List<Theme.ThemeBean>> getAllTheme();

    /**
     * 插入story列表
     * @param stories story列表
     * @param themeId 列表对应的主题的id，首页数据id为0
     * @param date 列表数据对应的时间换算而成的毫秒值
     * @return 插入成功的条数
     */
    int insertStoryList(List<Story> stories, int themeId, long date);

    /**
     * 根据id更新对应的story详情
     * @param content story的详情
     * @return 是否插入成功
     */
    boolean updateContent(StoryDetail content);

    /**
     * 收藏某个story
     * @param id story的id
     * @return 是否收藏成功
     */
    boolean bookmarkStory(int id);

    /**
     * 取消收藏某个story
     * @param id
     * @return
     */
    boolean unbookmarkStroy(int id);

    /**
     * 根据id查询某个story是否已经收藏
     * @param id
     * @return
     */
    boolean ifBookmarded(int id);

    Observable<List<Story>> getBookmarkedStories();

    /**
     * 根据主题id获取对应story的列表
     * @param id 主题对应的id
     * @return story的列表
     */
    Observable<StoryList> getThemeStoryList(int id);

    /**
     * 获取首页的story列表
     * @return
     */
    Observable<News> getHomeStoryList();

    /**
     * 根据关键字搜索收藏的story
     * @param keyWord 用户输入的关键字
     * @return
     */
    Observable<List<Story>> searchStories(String keyWord);

    /**
     * 根据story 的id获取详情
     * @param id story的id
     * @return id对应的story 的详情
     */
    Observable<StoryDetail> getContentById(int id);

    /**
     * 清除失效的storied
     */
    void clearOverdueStories();
}
