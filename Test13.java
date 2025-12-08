package com.stupra;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Test13 {
    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;
    static ReentrantLock ROOM = new ReentrantLock();
    //等待烟的休息室
    static Condition waitCigaretteSet = ROOM.newCondition();
    //等外卖的休息室
    static Condition waitTakeoutSet = ROOM.newCondition();

    //虚假等待
    public static void main(String[] args) {
        new Thread(() -> {
            ROOM.lock();
            try {
                log.debug("有烟没[{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟休息会");
//                    Sleeper(2);
                    try {
                        waitCigaretteSet.await();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                log.debug("小南开始干活");
            } finally {
                ROOM.unlock();
            }

        }, "小南").start();

        new Thread(() -> {
            ROOM.lock();
            log.debug("外卖到了吗[{}]", hasTakeout);
            try {
                while (!hasTakeout) {
                    log.debug("等外卖中[{}]", hasTakeout);
                    try {
                        waitTakeoutSet.await();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                log.debug("小女开始吃外卖");
            } finally {
                ROOM.unlock();
            }
        }, "小女").start();


        for (int i = 0; i <= 5; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.debug("可以开始干活");
                }
            }, "其他人").start();
        }
        Sleeper(1);
        new Thread(() -> {
            ROOM.lock();
            try {
                hasTakeout = true;
                log.debug("外卖送到了");
//                room.notify();
                waitTakeoutSet.signal();
            } finally {
                ROOM.unlock();
            }
        }, "送烟").start();
    }

    private static void Sleeper(int i) {
        try {
            Thread.sleep(i * 1000L);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
