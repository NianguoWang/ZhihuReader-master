package com.wangng.zhihureader.ui.bigimage;

import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.wangng.zhihureader.R;
import com.wangng.zhihureader.base.BaseActivity;

import butterknife.BindView;

public class BigImageActivity extends BaseActivity {

    @BindView(R.id.photo_view)
    PhotoView photoView;
    @Override
    protected void initData() {
        String imgUrl = getIntent().getStringExtra("imgUrl");
        Picasso.with(mContext).load(imgUrl).into(photoView);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_big_image;
    }

    @Override
    protected void initView() {
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
