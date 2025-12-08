package com.stupra;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Example05 {
    public static void main(String[] args) {
        Class<String> clazz = String.class;
        //获取String类中的域
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            System.out.println(field.getName());
        }
        System.out.println("-----------------");
        //获取String类中的方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            System.out.print(Modifier.toString(m.getModifiers()) + " ");
            System.out.print(m.getReturnType().getSimpleName() + " ");
            System.out.println(m.getName()+"()");
        }
        //打印修饰符


    }
}
