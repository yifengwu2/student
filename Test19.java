package com.stupra;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Test19 {
    public static void main(String[] args) throws InterruptedException {
        // 固定大小线程池
//        ExecutorService executor = Executors.newFixedThreadPool(3);
        //带缓冲线程池
//        ExecutorService executor = Executors.newCachedThreadPool();
        //单线程线程池
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            System.out.println("执行任务");
        });
        List<Future<Object>> futures = executor.invokeAll(Arrays.asList(
                () -> {
                    System.out.println("【invokeAll-1】正在计算 A");
                    return "A";
                },
                () -> {
                    System.out.println("【invokeAll-2】正在计算 B");
                    return "B";
                },
                () -> {
                    System.out.println("【invokeAll-3】正在计算 C");
                    return "C";
                }
        ));
        futures.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        executor.shutdown();
        System.out.println("主程序结束，JVM 即将退出");


    }
}
