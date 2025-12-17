package com.stupra;

import lombok.extern.slf4j.Slf4j;

import javax.management.RuntimeMBeanException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class Test24 {
    public static void main(String[] args) {
        ThreadFactory nameFactory = (r) -> {
            Thread t = new Thread(r);
            t.setName("PriceFetcher-" + t.getId());
            return t;
        };
        ExecutorService executor = Executors.newFixedThreadPool(3,nameFactory);
        List<Callable<Integer>> list = Arrays.asList(
                () -> {
                    try {
                        log.debug("校内论坛开始查询");
                        Thread.sleep(800);
                        log.debug("校内论坛返回价格:100");
                        return 100;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("校内论坛出现异常");
                    }
                },
                () -> {
                    try {
                        log.debug("闲鱼开始查询");
                        Thread.sleep(300);
                        log.debug("咸鱼返回价格:200");
                        return 200;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("咸鱼查询被中断");
                    }
                },
                () -> {
                    log.debug("转转开始查询:");
                    Thread.sleep(500);
                    log.debug("转转返回价格:300");
                    return 300;
                }
        );
        Object invoked = null;
        try {
            invoked = executor.invokeAny(list);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        log.debug("竞速模式：最快返回价格 = {}", invoked);

        //  **同时拿到全部价格做对比分析**
        List<Future<Integer>> futures = null;
        try {
            futures = executor.invokeAll(list, 10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("查询被中断{}", e.getMessage());
            return;
        }
        futures.forEach(f -> {
            try {
                Object o = f.get();
                log.debug("价格{}", o);
            } catch (Exception e) {
                log.error("查询错误，跳过");

            }
        });

        try {
            Integer i = executor.invokeAny(list, 600, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            log.warn("{}", e.getMessage());
            return;
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(2, TimeUnit.SECONDS))
                    log.error("❌ 线程池强制关闭失败");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            //恢复中断状态
            Thread.currentThread().interrupt();

        }


    }
}
