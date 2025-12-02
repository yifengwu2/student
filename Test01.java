package com.stupra;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class Test01 {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Customer().start();
        }
        Sleeper.sleep(1);

        for (Integer id : OrderBox.getIds()) {
            new Shop(id, "商品" + id).start();
        }
    }
}

class Sleeper {
    public static void sleep(long i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

//商店
@Slf4j(topic = "Shop")
class Shop extends Thread {
    private int id;

    private String mail;

    public Shop(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        log.debug("开始做餐");
        MealOrder order = OrderBox.getMealOrder(this.id);
        log.debug("餐品单号{}", order.getId());
        order.complete(mail);
        log.debug("餐品订单{},餐品内容{}", order.getId(), mail);
    }
}

//顾客
@Slf4j(topic = "Customer")
class Customer extends Thread {
    @Override
    public void run() {
        MealOrder order = OrderBox.createMealOrder();
        log.debug("开始点餐，订单id{}", order.getId());
        Object response = order.get(5000);
        log.debug("订单id{},订单内容{}", order.getId(), response);
    }
}

//存放所有未完成的订单
class OrderBox {
    private static int id;
    private static final Map<Integer, MealOrder> boxes = new Hashtable<>();

    public synchronized static int getId() {
        return ++id;
    }

    //通过订单号获取订单
    public static MealOrder getMealOrder(int id) {
        MealOrder order = boxes.get(id);
        boxes.remove(id);
        return order;
    }

    //创建订单
    public static MealOrder createMealOrder() {
        int id = getId();
        MealOrder order = new MealOrder(id);
        boxes.put(id, order);
        return order;
    }

    //打印map中的id，方便店员查询赋值
    public static Set<Integer> getIds() {
        return boxes.keySet();
    }

}

//核心类，负责等待/通知机制，带超时
@Slf4j(topic = "MealOrder")
class MealOrder {
    private int id;
    private Object response;

    public MealOrder(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Object get(long timeout) {
        //每个实例中有多个线程会同时访问
        synchronized (this) {
            log.debug("开始记录时间");
            long begin = System.currentTimeMillis();
            long parseTime = 0;
            long waitTime;
            while (response == null) {
                waitTime = timeout - parseTime;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    log.debug("进入等待");
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                parseTime = System.currentTimeMillis() - begin;
            }
        }
        return response;
    }

    //通知机制
    public void complete(Object object) {
        synchronized (this) {
            response = object;
            this.notifyAll();
        }
    }
}
