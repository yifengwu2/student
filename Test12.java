package com.stupra;

interface Startable {
    void start();
}

public class Test12 {
    public static Startable getPlan() {
        return () -> System.out.println("一架飞机在空中飞行");
    }

    public static Startable getShip() {
        return () -> System.out.println("一架轮船在水中航行");
    }

    public static void working(Startable flyable) {
        flyable.start();
    }

    public static void main(String[] args) {
        Startable plan = getPlan();
        Startable ship = getShip();
        working(plan);
        working(ship);
    }
}
