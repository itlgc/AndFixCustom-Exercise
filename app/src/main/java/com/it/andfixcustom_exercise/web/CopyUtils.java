package com.it.andfixcustom_exercise.web;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by lgc on 2020-02-10.
 *
 * @description
 */
public class CopyUtils {

    public static File copyAssetsAndWrite(Context context, String fileName) {
        try {
            File cacheDir = context.getCacheDir();
            //            File cacheDir = context.getDir("FixDir", Context.MODE_PRIVATE);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (res) {
                    InputStream is = context.getAssets().open(fileName);
                    FileOutputStream fos = new FileOutputStream(outFile);
                    byte[] buffer = new byte[is.available()];
                    int byteCount;
                    while ((byteCount = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.flush();
                    is.close();
                    fos.close();
                    Log.d("TAG:", "文件拷贝成功"+outFile.getAbsolutePath());
                    return outFile;
                }
            } else {
                Log.d("TAG:", "文件已经存在"+outFile.getAbsolutePath());
                return outFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Assets目录下的fileName文件拷贝至手机sd卡
     *
     * @param context
     * @param fileName
     */
    public static String copyAssetsAndWriteToSdcard(Context context, String fileName) {

        try {
            File outFile = new File(Environment.getExternalStorageDirectory(), fileName);

            InputStream is = context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[is.available()];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            Log.d("TAG:", "文件拷贝成功" + outFile.getAbsolutePath());
            return outFile.getAbsolutePath();

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
