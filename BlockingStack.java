package com.stupra;

import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.LinkedList;

@Slf4j(topic = "BlockingStack")
public class BlockingStack<T> {
    private static final Object lock = new Object();
    private final Deque<T> list = new LinkedList<>();

    //存元素
    public void push(T element) {
        synchronized (list) {
            list.addFirst(element);
            lock.notifyAll();
        }
    }

    //取元素
    public T pop() {
        synchronized (list) {
            while (list.isEmpty()) {
                log.debug("栈为空，进入等待");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            return list.pollLast();
        }
    }

    //带超时的阻塞栈
    public T poll(long timeout) throws InterruptedException {
        synchronized (this) {
            long start = System.currentTimeMillis();
            while (list.isEmpty()) {
                //经过的时间
                long end = System.currentTimeMillis();
                long parseTime = end - start;
                //剩余的时间
                long waitTime = timeout - parseTime;
                //超时了，直接退
                if (waitTime <= 0) {
                    return null;
                }
                try {
                    //如果有元素了其他线程唤醒他吗？然后就跳出去了,然后判断
                    this.wait(waitTime);//JVM 保证：wait(nanos) 最多等 nanos 纳秒，到时自动唤醒
                } catch (InterruptedException e) {
                    throw new InterruptedException();
                }

            }
            return list.pollLast();

        }
    }

    public static void main(String[] args) {
        BlockingStack<String> stack = new BlockingStack<>();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                stack.push("" + i);
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(stack.pop());
            }
        }, "t2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


    }
}
