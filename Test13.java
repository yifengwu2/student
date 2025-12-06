package com.stupra;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test13 {
    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    //虚假等待
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没[{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟休息会");
//                    Sleeper(2);
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                log.debug("有烟没[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("小南开始干活");
                } else {
                    log.debug("没干成活");
                }
            }
        }, "小南").start();

        new Thread(() -> {
            log.debug("外卖到了吗[{}]", hasTakeout);
            synchronized (room) {
                if (!hasTakeout) {
                    log.debug("等外卖中[{}]", hasTakeout);
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                log.debug("外卖到了吗[{}]", hasTakeout);
                if (hasTakeout) {
                    log.debug("小女开始吃外卖");
                } else {
                    log.debug("没吃成外卖");
                }
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
            synchronized (room) {
                hasTakeout = true;
                log.debug("外卖送到了");
//                room.notify();
                room.notifyAll();
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
