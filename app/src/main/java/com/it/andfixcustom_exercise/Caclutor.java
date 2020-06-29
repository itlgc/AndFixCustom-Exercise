package com.it.andfixcustom_exercise;

public class Caclutor {

    public int caculator() {
        //后端日志检测到的抛出异常的地方  需要针对这个方法进行修复
        throw new RuntimeException("异常");

    }

    //正常的方法
    public int caculator2() {
        return 10;
    }
}
