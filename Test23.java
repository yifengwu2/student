package com.stupra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Test23 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(() -> {
            System.out.println("Log: order processing...");
        });
        List<Callable<Double>> list = Arrays.asList(
                () -> (1 - 0.1) * 100,
                () -> (1 - 0.15) * 100,
                () -> (1 - 0.2) * 100
        );
        try {
            List<Future<Double>> futures = executor.invokeAll(list);
            double min = Double.MAX_VALUE;
            for (Future<Double> future : futures) {
                Double d = future.get();
                min = Math.min(min, d);
            }
            System.out.println(min);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();
    }
}
