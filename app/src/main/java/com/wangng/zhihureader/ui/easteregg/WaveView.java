package com.wangng.zhihureader.ui.easteregg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wng on 2017/3/2.
 */

public class WaveView extends View {

    private static final int[] mColors = new int[] { Color.RED, Color.BLUE, Color.YELLOW,
            Color.GREEN };
    private ArrayList<Wave> mWaves = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            flushWaves();
            invalidate();

            if(!mWaves.isEmpty()) {
                sendEmptyMessageDelayed(1, 50);
            }
        }
    };

    private void flushWaves() {
        ArrayList<Wave> deleteWave = new ArrayList<>();
        for (Wave wave : mWaves) {
            wave.radius += 5;
            wave.paint.setStrokeWidth(wave.radius / 3);
            int alpha = wave.paint.getAlpha();
            if(alpha <= 0) {
                deleteWave.add(wave);
                continue;
            }
            alpha -= 5;
            if(alpha < 0) {
                alpha = 0;
            }
            wave.paint.setAlpha(alpha);
        }
        mWaves.removeAll(deleteWave);
    }

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                addToList((int) event.getX(), (int) event.getY());
                break;
        }
        return true;
    }

    public void addToList(int x, int y) {
        if(mWaves.isEmpty()) {
            addWave(x, y);
            mHandler.sendEmptyMessage(1);
        } else {
            Wave lastWave = mWaves.get(mWaves.size() - 1);
            if(Math.abs(lastWave.x - x) > 10 || Math.abs(lastWave.y - y) > 10) {
                addWave(x, y);
            }
        }
    }

    private void addWave(int x, int y) {
        Wave wave = new Wave();
        wave.x = x;
        wave.y = y;
        wave.radius = 0;

        Paint paint = new Paint();
        paint.setStrokeWidth(wave.radius / 3);
        int colorIndex = (int) (Math.random() * mColors.length);
        paint.setColor(mColors[colorIndex]);
        paint.setAntiAlias(true);
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.STROKE);
        wave.paint = paint;
        mWaves.add(wave);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mWaves.size(); i++) {
            Wave wave = mWaves.get(i);
            canvas.drawCircle(wave.x, wave.y, wave.radius, wave.paint);
        }
    }

    class Wave {
        public int x;//圆环圆心的横坐标
        public int y;//圆环圆心的纵坐标
        public int radius;//圆环的半径
        public Paint paint;//画圆环的画笔
    }
}
