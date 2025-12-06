package com.stupra;

public class Door implements Take {
    private int state = CLOSE;

    @Override
    public void off() {
        if (state == 0) {
            System.out.println("门已关闭，无需再关");
        }
        System.out.println("正在关门");
        state = 0;
    }

    @Override
    public void on() {
        if (state == 1) {
            System.out.println("门已开，无需在开");
        }
        System.out.println("正在开门");
        state = 1;
    }


    public static void main(String[] args) {
        Door door = new Door();
        door.alarm();
        door.on();
        door.on();
        door.off();
        door.on();
    }
}

interface Take {
    int CLOSE = 0;
    int OPEN = 1;

    default void alarm() {
        System.out.println("正在报警");
    }

    void off();

    void on();
}
