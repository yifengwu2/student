package com.stupra;

/**
 * 他第一次执行完关门但期间碰到障碍物了所以他门是开的，
 * 但是他睡醒后又把状态改了，所以上次开着门的那个状态被覆盖了，
 * 其实门应该是开着的，但是他下一次检测的仍然是关闭状态。
 * “写覆盖”（Write Tearing）：
 * 两个线程对同一变量反复写，后写的覆盖先写的，系统丢失了关键事实。
 */
class DoorController {
    private static  volatile DoorController instance;

    private DoorController() {
        System.out.println("正在初始化传感器");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        System.out.println("初始化完成");
    }

    public static DoorController getInstance() {
        synchronized (DoorController.class) {
            if (instance == null) {
                instance = new DoorController();
            }
            return instance;
        }
    }

    private volatile boolean open = true; // true=开门中/已开；false=正在关/已关
    private volatile boolean closing = false; // true=电机正在执行关门动作

    private volatile boolean canClose = true;

    public void close() {
        if (!open || !canClose) {
            System.out.println("关门被拒绝" + open);
            return;
        }
        if (open) {
            System.out.println("->门开始关闭");
            closing = true;
            try {
                Thread.sleep(2000); // 模拟关门耗时
            } catch (InterruptedException e) {
                Thread.currentThread().isInterrupted();
                return;
            }
            //先查看其他线程对他改没改，如果其他线程已经优先一步改了，
            //就直接退
            //对其他线程open==true无效
            //对其他线程open==false有效
            if (!open || !canClose) {
                System.out.println("睡醒发现门已关 → 不再重复关闭！");
                closing = false;
                return;//主动放弃写权限，把状态主权还给 t2
            }

            open = false;
            closing = false;
            System.out.println("门已关闭");
        }
    }

    public void onObstacleDetected() {
        System.out.println("检测到障碍物！强制开门");
        open = true;
        closing = false;
        canClose = false;
    }

    public void obstacleCleared() {
        System.out.println("障碍已清除，恢复关门许可");
        canClose = true;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isClosing() {
        return closing;
    }
}

public class Example08 {
    public static void main(String[] args) {
        // 启动关门线程
        DoorController door = DoorController.getInstance();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                door.close();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        });

        // 同时启动障碍检测线程（模拟用户手挡住）
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            door.onObstacleDetected();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            door.onObstacleDetected();

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

