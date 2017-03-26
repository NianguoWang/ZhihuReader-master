package com.wangng.zhihureader.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by 小爱 on 2017/3/3.
 *
 * 强大的Toast，防止连续弹出。
 */
public class ToastUtil {

    private static Toast mToast;
    private ToastUtil(){}

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    public static void showToast(Context context, String text) {
        if(mToast==null){
            //如果等于null，则创建
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }else {
            //如果不等于空，则直接将text设置给toast
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void showSnackBar(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackBar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackBar(View view, String text, String action, View.OnClickListener l) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction(action, l).show();
    }

    public static void showSnackBar(View view, int textResId, int actionResId, View.OnClickListener l) {
        Snackbar.make(view, textResId, Snackbar.LENGTH_LONG).setAction(actionResId, l).show();
    }

}
