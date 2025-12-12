package com.stupra;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class Test17 {
    public static void main(String[] args) {
        Student2 stu = new Student2();

        AtomicReferenceFieldUpdater<Student2, String> updater =
                AtomicReferenceFieldUpdater.newUpdater(Student2.class, String.class, "name");

        System.out.println(updater.compareAndSet(stu, null, "张三"));
        System.out.println(stu);


    }
}

class Student2 {
    volatile String name;

    @Override
    public String toString() {
        return "Student2{" +
                "name='" + name + '\'' +
                '}';
    }
}
