package com.wangng.zhihureader.data.model;

import java.util.List;

/**
 * Created by wng on 2017/2/18.
 */

public class StoryList {
    public int color;
    public String name;
    public String description;
    public String image;
    public String image_source;
    public String background;
    public List<Editor> editors;
    public List<Story> stories;

    public static class Editor {
        public int id;
        public String name;
        public String bio;
        public String avatar;
        public String url;
    }

}
