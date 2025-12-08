package com.stupra;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建一个 Counter 类，含 increment() 方法。
 * 启动 10 个线程，每个线程调用 1000 次 increment()。最终结果应为 10000。
 */
@Slf4j
public class Counter {
    private static final Object lock = new Object();
    private static int count = 0;

    public static void increment() {
        synchronized (lock) {
            count++;
        }
    }

    public static int getCount() {
        synchronized (lock) {
            return count;
        }
    }

    public static void main(String[] args) {
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    Counter.increment();
                }
            }, "t" + i);
            list.add(t);
            t.start();
        }
        for (Thread t : list) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(Counter.getCount());

    }
}
