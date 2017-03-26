package com.wangng.zhihureader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.base.OnRecycleViewItemClickListener;
import com.wangng.zhihureader.data.model.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wng on 2017/3/19.
 */

public class BookmarkAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<Story> mStories;
    private OnRecycleViewItemClickListener mListener;
    public BookmarkAdapter(Context context, List<Story> stories, OnRecycleViewItemClickListener l) {
        mContext = context;
        mStories = stories;
        mListener = l;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_nomal_story, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Story story = mStories.get(position);
        ArrayList<String> images = story.images;
        if(images != null && images.size() > 0) {
            ((NormalViewHolder) holder).mImageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(images.get(0)).into(((NormalViewHolder) holder).mImageView);
        } else {
            ((NormalViewHolder) holder).mImageView.setVisibility(View.GONE);
        }
        ((NormalViewHolder) holder).mStory = story;
        ((NormalViewHolder) holder).mTvTitle.setText(story.title);
        ((NormalViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecycleViewItemClick(story);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

}
