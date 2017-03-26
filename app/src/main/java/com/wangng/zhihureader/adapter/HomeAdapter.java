package com.wangng.zhihureader.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.base.OnRecycleViewItemClickListener;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wng on 2017/2/20.
 */
public class HomeAdapter extends RecyclerView.Adapter{

    private static final int ITEM_TYPE_TOP = 0;
    private static final int ITEM_TYPE_NOMAL = 1;

    private boolean withHeader = false;
    private int mDistance;
    private Context mContext;
    private OnRecycleViewItemClickListener mListener;
    private List<Story> mStories;
    private List<Story> mTopStories;

    public HomeAdapter(Context context, List<Story> stories, OnRecycleViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
        mStories = stories;
    }

    public void setTopStories(List<Story> topStories) {
        mTopStories = topStories;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_TOP) {
            return new TopViewHolder(LayoutInflater.from(mContext).inflate(R.layout.autoplay_viewpager, parent, false));
        } else if(viewType == ITEM_TYPE_NOMAL) {
            return new NormalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_nomal_story, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TopViewHolder) {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0) {
                        int currentItem = ((TopViewHolder) holder).mViewPager.getCurrentItem();
                        currentItem++;
                        if(currentItem > mTopStories.size() - 1) {
                            currentItem = 0;
                        }
                        ((TopViewHolder) holder).mViewPager.setCurrentItem(currentItem, true);
                        sendEmptyMessageDelayed(0, 3000);
                    }
                }
            };
            for (int i = 0; i < mTopStories.size(); i++) {
                ImageView point = new ImageView(mContext);
                point.setImageResource(R.drawable.shape_point_gray);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i > 0) {
                    params.leftMargin = DensityUtil.dip2px(mContext, 10);
                }
                point.setLayoutParams(params);
                ((TopViewHolder) holder).mContainer.addView(point);
            }
            TopStoryPagerAdapter pagerAdapter = new TopStoryPagerAdapter(mContext, mTopStories);
            ((TopViewHolder) holder).mViewPager.setAdapter(pagerAdapter);
            ((TopViewHolder) holder).mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int leftMargin = (int) (mDistance * positionOffset + position
                            * mDistance);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((TopViewHolder) holder).mRedPoint
                            .getLayoutParams();
                    params.leftMargin = leftMargin;

                    ((TopViewHolder) holder).mRedPoint.setLayoutParams(params);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            ((TopViewHolder) holder).mRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            mDistance = ((TopViewHolder) holder).mContainer.getChildAt(1).getLeft()
                                    - ((TopViewHolder) holder).mContainer.getChildAt(0).getLeft();
                            ((TopViewHolder) holder).mRedPoint.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    });
            handler.sendEmptyMessageDelayed(0, 3000);
        } else if(holder instanceof NormalViewHolder) {
            Story story = null;
            if(withHeader) {
                story = mStories.get(position - 1);
            } else {
                story = mStories.get(position);
            }
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
                    mListener.onRecycleViewItemClick(((NormalViewHolder) holder).mStory);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mTopStories == null || mTopStories.isEmpty()) {
            withHeader = false;
            return mStories.size();
        }
        withHeader = true;
        return mStories.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(mTopStories == null || mTopStories.isEmpty()) {
            return ITEM_TYPE_NOMAL;
        } else {
            if(position == 0) {
                return ITEM_TYPE_TOP;
            } else {
                return ITEM_TYPE_NOMAL;
            }
        }
    }

    static class TopViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public ViewPager mViewPager;
        public LinearLayout mContainer;
        public ImageView mRedPoint;
        public TopViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mViewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
            mContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
            mRedPoint = (ImageView) itemView.findViewById(R.id.iv_red_point);
        }
    }

}
