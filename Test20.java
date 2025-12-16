package com.stupra;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test20 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(
                () -> {
                    System.out.println("Task-1 start");
                    Sleep(1);
                    System.out.println("Task-1 end");
                }
        );
        executor.submit(() -> {
            System.out.println("Task-2 start");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Task-2 end");
        });

        executor.submit(() -> {
            System.out.println("Task-3 start");
            System.out.println("Task-3 end");
        });

        executor.shutdown();
        Sleep(2);
        System.out.println("All done!");

    }

    public static void Sleep(int i) {
        try {
            Thread.sleep(i * 1000L);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
