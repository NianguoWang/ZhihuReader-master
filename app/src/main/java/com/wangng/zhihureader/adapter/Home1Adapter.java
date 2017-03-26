package com.wangng.zhihureader.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.base.OnRecycleViewItemClickListener;
import com.wangng.zhihureader.data.model.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小爱 on 2017/3/25.
 */

public class Home1Adapter extends BaseAdapter{

    private Context mContext;
    private List<Story> mStories;
    private OnRecycleViewItemClickListener mListener;
    public Home1Adapter(Context context, List<Story> stories, OnRecycleViewItemClickListener listener) {
        mContext = context;
        mStories = stories;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mStories.size();
    }

    @Override
    public Object getItem(int position) {
        return mStories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_nomal_story, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Story story = mStories.get(position);
        ArrayList<String> images = story.images;
        if(images != null && images.size() > 0) {
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(images.get(0)).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }
        holder.mTvTitle.setText(story.title);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecycleViewItemClick(story);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView mTvTitle;
    }
}
