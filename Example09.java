package com.stupra;

import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 生产者-消费者-仓储模型（LIFO 栈语义）
 */
public class Example09 {
    public static void main(String[] args) {
        //三个生产者
        for (int i = 0; i < 3; i++) {
            new Producer("生产者" + i).start();
        }
        //五个消费者
        for (int i = 0; i < 3; i++) {
            new Consumer("消费者" + i).start();
        }
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        Storehouse storehouse = Storehouse.getInstance();
        storehouse.getStatue();

    }
}

//生产者
@Slf4j
class Producer extends Thread {

    public Producer(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            production("" + i);
        }
    }

    //生产物品
    public void production(String product) {
//        Storehouse storehouse = Storehouse.getInstance();
//        synchronized (storehouse.stack) {
//            while (storehouse.getSize() == 10) {
//                log.debug("仓库已满，请等待");
//                try {
//                    storehouse.stack.wait();
//                } catch (InterruptedException e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//            storehouse.push(product);
//            storehouse.stack.notifyAll();
//        }

        Storehouse.getInstance().push(product);
        log.debug("{}生产{}", getName(), product);
    }
}

@Slf4j
//仓库(用栈实现线程安全)
class Storehouse {
    private static final int cap = 10;
    private static volatile Storehouse instance;
    final Deque<String> stack = new LinkedList<>();
    private int totalProduced = 0;
    private int totalConsumer = 0;

    private Storehouse() {
    }

    //获取单例
    public static Storehouse getInstance() {
        if (instance == null) {
            synchronized (Storehouse.class) {
                if (instance == null) {
                    instance = new Storehouse();
                }
            }
        }
        return instance;
    }

    //入栈
    public void push(String pro) {
        synchronized (stack) {
            while (stack.size() >= 10) {
                try {
                    stack.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted", e);
                }
            }
            stack.addFirst(pro);
            //唤醒等待的消费者
            stack.notifyAll();
            totalProduced++;
        }
    }

    //出栈
    public String pop() {
        synchronized (stack) {
            while (stack.isEmpty()) {
                try {
                    stack.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("{} 被中断", Thread.currentThread().getName(), e);
                    throw new RuntimeException("Interrupted", e);
                }
            }
            //当容量满时弹出一个唤醒
            String product = stack.pollLast();
            stack.notifyAll();
            totalConsumer++;
            return product;
        }
    }

    //获取当前运行状态
    public void getStatue() {
        synchronized (stack) {
            log.debug("仓库状态:容量={} | 当前={}/{} | 生产={} | 消费={} | 空闲率={}",
                    cap, getSize(), cap, totalProduced, totalConsumer, (100.0 * (cap - stack.size())) / cap
            );

        }
    }

    //出栈(带超时)保护性暂停带超时
    public String pop(long timeout) {
        synchronized (stack) {
            long start = System.currentTimeMillis();
            while (stack.isEmpty()) {
                long end = System.currentTimeMillis();
                long parseTime = end - start;
                long waitTime = timeout - parseTime;

                if (waitTime <= 0) {
                    return null;
                }
                try {
                    stack.wait(waitTime);
                } catch (InterruptedException e) {
                    log.error("{}被中断", Thread.currentThread().getName(), e);
                    throw new RuntimeException("Interrupted", e);
                }
            }
            totalConsumer++;
            return stack.pollLast();
        }
    }

    //获取容量
    public int getSize() {
        synchronized (stack) {
            return stack.size();
        }
    }

}

//消费者
@Slf4j
class Consumer extends Thread {

    public Consumer(String name) {
        super(name);
    }

    @Override
    public void run() {
        Storehouse storehouse = Storehouse.getInstance();
        for (int i = 0; i < 10; i++) {
            String product = storehouse.pop(2000);
            if (product == null) {
                log.error("{} 等不到货，主动退出第 {} 次消费", getName(), i + 1);
            } else {
                log.info("{} ← 消费成功：{}", getName(), product);
            }
        }
    }
}
