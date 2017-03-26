package com.wangng.zhihureader.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.data.model.Story;
import com.wangng.zhihureader.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wng on 2017/3/6.
 */
public class TopStoryPagerAdapter extends PagerAdapter{

    private Context mContext;
    private List<Story> mStories;
    public TopStoryPagerAdapter(Context context, List<Story> stories) {
        mContext = context;
        mStories = stories;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Story topStory = mStories.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.page_top_story, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showStoryDetail(mContext, topStory.id);
            }
        });
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        Picasso.with(mContext).load(topStory.image).into(imageView);
        tvTitle.setText(topStory.title);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mStories.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
