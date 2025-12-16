package com.stupra;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test22 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Object o = null;
        try {
            o = executor.invokeAny(Arrays.asList(
                    () -> {
                        Thread.sleep(300);
                        return "DB-A";
                    },
                    () -> {
                        Thread.sleep(100);
                        throw new RuntimeException("Timeout");
                    },
                    () -> {
                        Thread.sleep(200);
                        return "DB-C";
                    }
            ));
        } catch (Exception e) {
            System.out.println("All failed");
            System.out.println(e.getMessage());
        }

        System.out.println(o);

        executor.shutdown();
    }
}
