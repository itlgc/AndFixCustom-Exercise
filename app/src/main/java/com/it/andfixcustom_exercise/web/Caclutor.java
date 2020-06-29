package com.it.andfixcustom_exercise.web;

import com.it.andfixcustom_exercise.Replace;

/**
 * 模拟修复类  这个类在发出去的版本中是不存在的，自己编写好借此生成修复包 xxx.dex 放到服务器中去修复已经发出的版本
 *
 * 真实开发中的话因为修复的方法所在的类有可能有很多其他类以及方法的引用，所以不会像我们测试环境这样，
 * 以及对于混淆后的apk，这里在注解里传入的包名实际在运行中会无法和混淆后的包名进行对比找到要修复的方法，
 * 所以最好通过阿里提供的apkpatch工具去将新旧apk传入来生成修复包。它帮我们解决了这个问题
 */
public class Caclutor {
    //修复类
    @Replace(clazz = "com.it.andfixcustom_exercise.Caclutor", method = "caculator")
    //需要保证定义个方法和待修复类中待修复方法定义保持一致
    public int caculator() {
        //针对跑异常地方做修复
        return 10;
    }


    //正常的方法，不需要修复所以不需要加注解
    public int caculator2() {

        return 10;
    }
}
