package com.example.chang.helloworld.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.InputStream;

/**
 * Android工具类
 *
 * @author Barkley.Chang
 * @date 2018-12-29 16:01
 */
public final class AndroidUtil {
    private AndroidUtil() {
    }

    /**
     * 根据手机的分辨率从dp的单位转成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        //  获取当前手机的像素密度
        final float scale = context.getResources().getDisplayMetrics().density;
        // 四舍五入取整
        return (int) (dpValue * scale + 0.5F);
    }

    /**
     * 根据手机的分辨率从px单位转成dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        // 获取当前手机的像素密度
        final float scale = context.getResources().getDisplayMetrics().density;
        // 四舍五入取整
        return (int) (pxValue / scale + 0.5F);
    }

    /**
     * 检查某个权限。
     *
     * @param act
     * @param permissions
     * @param requestCode
     * @return true: 已启用该权限;false: 未启动该权限
     */
    public static boolean checkPermission(Activity act, String[] permissions, int requestCode) {
        boolean result = true;

        // 只对Android 6.0及以上系统进行校验
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int check = PackageManager.PERMISSION_GRANTED;

            for (String permission : permissions) {
                // 检查当前App是否开启了名称为permission的权限
                check = ContextCompat.checkSelfPermission(act, permission);

                if (check != PackageManager.PERMISSION_GRANTED) {
                    break;
                }
            }

            if (check != PackageManager.PERMISSION_GRANTED) {
                // 未开启该权限,则请求系统弹窗,好让用户选择是否立即开启权限
                ActivityCompat.requestPermissions(act, permissions, requestCode);
                result = false;
            }
        }

        return result;
    }

    /**
     * 从assets资产文件中获取文本字符串
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getTxtFromAssets(Context context, String fileName) {
        String result = "";
        try {
            // 打开资产文件并获得输入流
            InputStream is = context.getAssets().open(fileName);
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            result = new String(buffer, "utf8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 从assets资产文件中获取位图对象
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getImgFromAssets(Context context, String fileName) {
        Bitmap bitmap = null;

        try {
            // 打开资产文件并获得输入流
            InputStream is = context.getAssets().open(fileName);
            // 解析输入流得到位图数据
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}