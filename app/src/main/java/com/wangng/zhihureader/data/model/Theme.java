package com.wangng.zhihureader.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by wng on 2017/2/18.
 */

public class Theme implements Parcelable {
    public int limit;
    public List<ThemeBean> others;
    public List<ThemeBean> subscribed;

    public static class ThemeBean implements Parcelable, Comparable {
        public int id;
        public int color;
        public int order;//展示在首页的顺序，0表示不展示在首页
        public String name;
        public String description;
        public String thumbnail;

        @Override
        public String toString() {
            return "ThemeBean{" +
                    "id=" + id +
                    ", order=" + order +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.color);
            dest.writeInt(this.order);
            dest.writeString(this.name);
            dest.writeString(this.description);
            dest.writeString(this.thumbnail);
        }

        public ThemeBean() {
        }

        protected ThemeBean(Parcel in) {
            this.id = in.readInt();
            this.color = in.readInt();
            this.order = in.readInt();
            this.name = in.readString();
            this.description = in.readString();
            this.thumbnail = in.readString();
        }

        public static final Creator<ThemeBean> CREATOR = new Creator<ThemeBean>() {
            @Override
            public ThemeBean createFromParcel(Parcel source) {
                return new ThemeBean(source);
            }

            @Override
            public ThemeBean[] newArray(int size) {
                return new ThemeBean[size];
            }
        };

        @Override
        public int compareTo(@NonNull Object o) {
            ThemeBean tb = (ThemeBean) o;
            int result = 0;
            result = this.order > tb.order ? 1 : (this.order == tb.order ? 0 : -1);
            if(result == 0) {
                result = this.id > tb.id ? -1 : 1;
            }
            return result;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.limit);
        dest.writeTypedList(this.others);
        dest.writeTypedList(this.subscribed);
    }

    public Theme() {
    }

    protected Theme(Parcel in) {
        this.limit = in.readInt();
        this.others = in.createTypedArrayList(ThemeBean.CREATOR);
        this.subscribed = in.createTypedArrayList(ThemeBean.CREATOR);
    }

    public static final Creator<Theme> CREATOR = new Creator<Theme>() {
        @Override
        public Theme createFromParcel(Parcel source) {
            return new Theme(source);
        }

        @Override
        public Theme[] newArray(int size) {
            return new Theme[size];
        }
    };
}
