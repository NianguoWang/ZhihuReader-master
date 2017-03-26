package com.wangng.zhihureader.ui.easteregg;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.wangng.zhihureader.R;

/**
 * Created by wng on 2017/3/2.
 */
public class EasterEggActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easteregg);

        WindowManager wm = this.getWindowManager();

        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        int width = point.x;
        int height = point.y;

        WaveView waveView = (WaveView) findViewById(R.id.wave_view);
        waveView.addToList(width/2 , height/2);
    }
}
