package com.stupra;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test21 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        long start = System.currentTimeMillis();
        List<Future<Integer>> futures = executor.invokeAll(Arrays.asList(
                () -> {
                    Thread.sleep(100);
                    return 100;
                },
                () -> {
                    Thread.sleep(200);
                    return 200;
                },
                () -> {
                    Thread.sleep(50);
                    return 50;
                },
                () -> {
                    Thread.sleep(150);
                    return 150;
                }
        ));
        futures.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("耗时:" + (end - start));

        executor.shutdown();


    }
}
