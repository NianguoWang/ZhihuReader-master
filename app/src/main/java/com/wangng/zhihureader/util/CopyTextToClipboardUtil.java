package com.wangng.zhihureader.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Spanned;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by 小爱 on 2017/3/11.
 */

public class CopyTextToClipboardUtil {
    private CopyTextToClipboardUtil() {}

    public static void copyTextToClipboard(Context context, CharSequence text) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", text);
        manager.setPrimaryClip(clipData);
    }

}
