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
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1, ((task, queue) ->
                //1.死等
//                queue.put(task)
                //2.带超时的等待
//                queue.offer(task, 500, TimeUnit.MILLISECONDS)
                log.warn("【拒绝】任务 {} 因队列满被丢弃", task)
              // 不要再调 queue.offer(...)！它已在 tryPut 中执行过了
        ));


        for (int i = 0; i < 3; i++) {
            int d = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    log.debug(e.getMessage());
                }
            });
        }

    }
}

//拒绝策略
@FunctionalInterface
interface RejectPolicy<T> {
    void reject(T task, BlockingQueue<T> blockingQueue);
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

    private RejectPolicy<Runnable> policy;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCap, RejectPolicy<Runnable> policy) {
        this.blockingQueue = new BlockingQueue<>(queueCap,timeout,timeUnit);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.policy = policy;
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
//                blockingQueue.put(task);
                blockingQueue.tryPut(policy, task);

                //1）队列满了就死等
                //2）队列满了等待timeout秒

                //3）让调用者放弃任务执行
                //4）让调用者抛出异常
                //5）让调用者自己执行任务
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

    private long timeout;
    private TimeUnit timeUnit;

    public BlockingQueue(int capacity, long timeout, TimeUnit timeUnit) {
        emptyWaitSet = lock.newCondition();
        this.capacity = capacity;
        fullWaitSet = lock.newCondition();
        this.timeout = timeout;
        this.timeUnit = timeUnit;
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
                    log.debug("当前线程被打断,{}", e.getMessage());
                }
            }
            deque.addLast(task);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    //存放任务(带超时)
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (deque.size() == capacity) {
                log.debug("任务队列已满，当前{}任务进入{}等待", task, timeout);
                if (nanos <= 0) {
                    return false;
                }
                try {
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.debug("线程 {} 被中断", Thread.currentThread().getName(), e);
                    return false;
                }
            }
            deque.offerLast(task);
            emptyWaitSet.signal();
            return true;
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
            fullWaitSet.signal();
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
            fullWaitSet.signal();
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

    public void tryPut(RejectPolicy<T> policy, T task) {
        lock.lock();
        try {
            if (!offer(task, timeout, timeUnit)) {
                policy.reject(task, this);
            }
        } finally {
            lock.unlock();
        }
    }
}


