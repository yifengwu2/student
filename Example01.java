package com.stupra;

public class Example01 {
    public static void main(String[] args) {
        Computer computer1 = new Computer("s");
        Lamp lamp1 = new Lamp();
        Employee employee = new Employee(computer1, lamp1);
        employee.work();
        employee.work();
        employee.leave();
    }
}

interface Switch {
    //开状态
    int ON = 1;
    //关状态
    int OFF = 0;

    //打开
    void open();

    //关闭
    void close();
}

class Lamp implements Switch {
    int on = 0;

    @Override
    public void open() {
        if (on == 1) {
            System.out.println("灯已经开了,无需再开了");
        } else if (on == 0) {
            System.out.println("开灯！");
            on = 1;
        }
    }

    @Override
    public void close() {
        if (on == 1) {
            System.out.println("关灯！");
            on = 0;
        } else if (on == 0) {
            System.out.println("灯已经关闭，无需再关");
        }
    }
}

class Computer implements Switch {
    int on = 0;
    private String logo;

    public Computer(String logo) {
        this.logo = logo;
    }

    @Override
    public void open() {
        if (on == 0) {
            System.out.println("计算机开始工作");
            on = 1;
        } else {
            System.out.println("计算机已经开了");
        }
    }

    @Override
    public void close() {
        if (on == 1) {
            System.out.println("正在关机");
        } else if (on == 0) {
            System.out.println("计算机已经关闭");
        }
    }
}

class Employee {
    private final Switch computer;
    private final Switch lamp;

    public Employee(Switch computer, Switch lamp) {
        this.computer = computer;
        this.lamp = lamp;
    }

    public void work() {
        System.out.println("开始工作");
        computer.open();
        lamp.open();
    }

    public void leave() {
        System.out.println("准备离开");
        computer.close();
        lamp.close();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "computer=" + computer +
                ", lamp=" + lamp +
                '}';
    }


}

