package com.wangng.zhihureader.data.model;

import java.util.ArrayList;

/**
 * Created by wng on 2017/2/20.
 */

public class Story {
    public int id;
    public int type;
    public String title;
    public String image;//top_stories用的字段
    public String ga_prefix;// 供 Google Analytics 使用
    public ArrayList<String> images;//其他story用的字段

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", images=" + images +
                '}';
    }
}
