package com.stupra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BadQueue {
    private final List<String> list = new ArrayList<>();

    void add(String s) {
        list.add(s);
    }

    String take() {
        return list.remove(0);
    }

    public static void main(String[] args) throws InterruptedException {
        BadQueue badQueue = new BadQueue();
        //1. 启动 2 个生产者线程，并等待它们完成
        Thread p1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                badQueue.add("P1-" + i);
            }
        }, "Producer-1");

        Thread p2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                badQueue.add("P2-" + i);
            }
        }, "Producer-2");


        //启动两个消费者
        Thread c1 = new Thread(() -> {
            while (true) {
                try {
                    String s = badQueue.take();
                    System.out.println("C1 took" + s);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    break;
                }

            }

        }, "Consumer-1");

        Thread c2 = new Thread(() -> {
            while (true) {
                try {
                    String s = badQueue.take();
                    System.out.println("C2 took" + s);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    break;
                }

            }

        }, "Consumer-2");

        p1.start();
        p2.start();
        p1.join();
        p2.join();


        System.out.println("✅ Producers done. List size = " + badQueue.list.size());

        c1.start();
        c2.start();
        c1.join(2000); // 最多等 2 秒
        c2.join(2000);

        System.out.println("💥 Final list: " + badQueue.list);
        System.out.println("💥 Final size: " + badQueue.list.size());

    }
}
