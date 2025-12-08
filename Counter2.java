package com.stupra;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Counter2 {
    private static final ReentrantLock lock = new ReentrantLock();
    private static int count = 0;

    public static void creatment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public static int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }


    public static void main(String[] args) {
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    creatment();
                }
            });
            list.add(t);
            t.start();
        }
        for (Thread t :list){
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(Counter2.getCount());
    }
}
