package com.wangng.zhihureader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.base.OnRecycleViewItemClickListener;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.data.model.StoryList;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by 小爱 on 2017/3/8.
 */
public class StoryAdapter extends RecyclerView.Adapter{

    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_NORMAL = 1;

    private boolean withHeader;
    private Context mContext;
    private StoryList mStoryList;
    private List<Story> mStories;
    private OnRecycleViewItemClickListener mListener;
    public StoryAdapter(Context context, StoryList storyList, OnRecycleViewItemClickListener listener) {
        checkNotNull(storyList);
        checkNotNull(storyList.stories);
        mContext = context;
        mStoryList = storyList;
        mStories = storyList.stories;
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(TextUtils.isEmpty(mStoryList.name)) {
            return ITEM_TYPE_NORMAL;
        } else {
            if(position == 0) {
                return ITEM_TYPE_HEADER;
            } else {
                return ITEM_TYPE_NORMAL;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_top_story, parent, false));
        } else {
            return new NormalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_nomal_story, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).mTvDesc.setText(mStoryList.description);
            Picasso.with(mContext).load(mStoryList.background).into(((HeaderViewHolder) holder).mIvThumbnail);
        } else if (holder instanceof NormalViewHolder) {
            Story story = null;
            if(withHeader) {
                story = mStories.get(position - 1);
            } else {
                story = mStories.get(position);
            }
            ((NormalViewHolder) holder).mStory = story;
            ((NormalViewHolder) holder).mTvTitle.setText(story.title);
            ArrayList<String> images = story.images;
            if(images != null && images.size() > 0) {
                ((NormalViewHolder) holder).mImageView.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(images.get(0)).into(((NormalViewHolder) holder).mImageView);
            } else  {
                ((NormalViewHolder) holder).mImageView.setVisibility(View.GONE);
            }
            ((NormalViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRecycleViewItemClick(((NormalViewHolder) holder).mStory);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(TextUtils.isEmpty(mStoryList.name)) {
            withHeader = false;
            return mStories.size();
        }
        withHeader = true;
        return mStories.size() + 1;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public ImageView mIvThumbnail;
        public TextView mTvDesc;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mIvThumbnail = (ImageView) itemView.findViewById(R.id.iv_theme_thumbnail);
            mTvDesc = (TextView) itemView.findViewById(R.id.tv_theme_desc);
        }
    }

}
