package com.stupra;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义线程池
 */
@Slf4j(topic = "TestPool")
public class TestPool {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(2, 1000L, TimeUnit.MILLISECONDS, 10);
        for (int i = 0; i < 3; i++) {
            int d = i;
            threadPool.execute(() -> {
                log.debug("{}", d);
            });
        }

    }
}

@Slf4j
class ThreadPool {
    //等待任务队列
    private final BlockingQueue<Runnable> blockingQueue;

    //线程集合
    private final HashSet<Worker> workers = new HashSet<>();

    //核心线程数
    private volatile int coreSize;

    //获取任务超时时间
    private long timeout;

    private TimeUnit timeUnit;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCap) {
        this.blockingQueue = new BlockingQueue<>(queueCap);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    //执行任务
    public void execute(Runnable task) {
        //当任务数没有超过coreSize时，直接交给worker对象执行
        //如果任务数超过coreSize时，加入任务队列
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增worker{}，{}", worker, task);
                workers.add(worker);
                worker.start();
            } else {
                log.debug("加入任务队列{}", task);
                blockingQueue.put(task);
            }
        }
    }

    private class Worker extends Thread {
        private Runnable task;

        private Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            //1)当task不为空，执行任务
            //2)当task执行完毕，再接着从任务队列获取任务
            while (task != null || (task = blockingQueue.take()) != null) {
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    log.debug("错误信息{}", e.getMessage());
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("worker被移除{}", this);
                workers.remove(this);
            }
        }

    }
}

@Slf4j(topic = "BlockingQueue")
class BlockingQueue<T> {
    private final Deque<T> deque = new ArrayDeque<>();

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition emptyWaitSet;

    private final Condition fullWaitSet;

    private final int capacity;

    public BlockingQueue(int capacity) {
        emptyWaitSet = lock.newCondition();
        this.capacity = capacity;
        fullWaitSet = lock.newCondition();
    }

    //存放任务
    public void put(T task) {
        lock.lock();
        try {
            while (deque.size() == capacity) {
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.debug("当前{}线程被打断,{}", Thread.currentThread().getName(), e.getMessage());
                }
            }
            deque.push(task);
            emptyWaitSet.signalAll();
        } finally {
            lock.unlock();
        }
    }

    //获取任务
    public T take() {
        lock.lock();
        try {
            while (deque.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.debug("当前{}线程被打断,{},", Thread.currentThread().getName(), e.getMessage());
                }
            }
            T t = deque.removeFirst();
            fullWaitSet.signalAll();
            return t;

        } finally {
            lock.unlock();
        }
    }

    //带超时的poll
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            //转化为纳秒级
            long nanos = unit.toNanos(timeout);
            while (deque.isEmpty()) {
                if (nanos <= 0) {
                    return null;
                }
                try {
                    //剩余时间
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.debug("当前线程被打断,{},", e.getMessage());
                }
            }
            T poll = deque.poll();
            fullWaitSet.signalAll();
            return poll;
        } finally {
            lock.unlock();
        }
    }

    //获取任务个数
    public int getSize() {
        lock.lock();
        try {
            return deque.size();
        } finally {
            lock.unlock();
        }
    }
}


