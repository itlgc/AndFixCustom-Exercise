package com.it.andfixcustom_exercise;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;


/**
 * 修复包管理
 * Created by lgc on 2020-03-21.
 */
public class DexManager {

    private Context context;

    public DexManager(Context context) {
        this.context = context;
    }


    //加载修复包
    public void load(File file) {
        try {
            DexFile dexFile = DexFile.loadDex(file.getAbsolutePath(),
                    new File(context.getCacheDir(), "opt").getAbsolutePath(), Context.MODE_PRIVATE);

            //当前的dex里面的class 类名集合
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                // 类的全类名
                String clazzName= entries.nextElement();
                //这里是不能通过反射去实例化的，反射只能实例化已经加载进内存的类， 对于在手机储存中的外部类是无法通过反射实例化的
                Class realClazz= dexFile.loadClass(clazzName, context.getClassLoader());
                if (realClazz != null) {
                    fixClazz(realClazz);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fixClazz(Class realClazz) {
        //得到修复包中类的方法集合
        Method[] methods=realClazz.getMethods();
        for (Method rightMethod : methods) {
            Replace replace = rightMethod.getAnnotation(Replace.class);
            if (replace == null) {
                continue;
            }

            //拿到了从网络上下载的修复包中的 method

            String clazzName=replace.clazz();
            String methodName=replace.method();

            try {
                // 本地的bug  class中method
                Class wrongClazz=  Class.forName(clazzName);
                Method wrongMethod = wrongClazz.getDeclaredMethod(methodName, rightMethod.getParameterTypes());

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    //Android4.4以下 <18
                    replaceDalvik(Build.VERSION.SDK_INT,wrongMethod, rightMethod);
                }else {
                    replace(Build.VERSION.SDK_INT,wrongMethod, rightMethod);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //通过native方法类实现修复
    public native  void replace(int sdk, Method wrongMethod, Method rightMethod);
    public native  void replaceDalvik(int sdk, Method wrongMethod, Method rightMethod);
}
