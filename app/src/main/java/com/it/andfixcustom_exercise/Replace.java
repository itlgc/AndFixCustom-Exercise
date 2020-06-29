package com.it.andfixcustom_exercise;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//目的： 通过注解来筛选出类中要修复的方法
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Replace {
    String clazz();//修复哪一个class
    String  method();// method
}
