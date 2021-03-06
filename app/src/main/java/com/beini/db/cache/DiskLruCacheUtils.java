package com.beini.db.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.beini.app.GlobalApplication;
import com.beini.util.BLog;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by beini on 2017/3/15.
 */

public class DiskLruCacheUtils {
    private static DiskLruCache mDiskLruCache = null;


    public static DiskLruCacheUtils getInstance() {
        BLog.e("DiskLruCacheUtils");
        DiskLruCacheUtils diskLruCacheUtils = new DiskLruCacheUtils();
        File cacheDir = getDiskCacheDir(GlobalApplication.getInstance().getApplicationContext(), "bitmap");
        BLog.e("cacheDir "+cacheDir.getAbsolutePath());
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        //第一个参数指定的是数据的缓存地址，第二个参数指定当前应用程序的版本号，第三个参数指定同一个key可以对应多少个缓存文件，基本都是传1，第四个参数指定最多可以缓存多少字节的数据。
        try {
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(GlobalApplication.getInstance().getApplicationContext()), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diskLruCacheUtils;
    }

    /**
     * 每当版本号改变，缓存路径下存储的所有数据都会被清除掉
     *
     * @param context
     * @return
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 获取到缓存地址的路径，然后判断一下该路径是否存在，如果不存在就创建一下
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


    public void clearCache(String key) throws IOException {
        mDiskLruCache.remove(key);
    }

    public boolean setCache(String key, InputStream inputStream) {

        DiskLruCache.Editor editor;
        try {
            editor = mDiskLruCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            int index;
            while ((index = inputStream.read()) != -1) {
                outputStream.write(index);
            }
            inputStream.close();
            outputStream.close();
            editor.commit();
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public InputStream getCache(String key) {
        DiskLruCache.Editor editor;
        InputStream inputStream = null;
        try {
            editor = mDiskLruCache.edit(key);
            inputStream = editor.newInputStream(0);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}
