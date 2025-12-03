package com.stupra;

public class Person {

    public Person(int idCard, int number, String name, String sex, int age, String major) {
        this.idCard = idCard;
        this.number = number;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.major = major;
    }

    private int idCard;

    private int number;

    private String name;

    private String sex;

    private int age;

    private String major;

    @Override
    public String toString() {
        return "Person{" +
                "idCard=" + idCard +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age + '\'' +
                ", major=" + major+" "
                ;
    }

    public static void main(String[] args) {
        Student1.Score score = new Student1.Score(12, 34, 55);
        Student1 student1 = new Student1(11, 212, "小明", "男", 12, score, "数据");
        System.out.println(student1);
//        System.out.println(new Teacher(11, 34, "小红", "男", 32, 22, "数据"));
    }
}

class Student1 extends Person {
    private Score score;

    public Student1(int idCard, int number, String name, String sex, int age, Score score, String major) {
        super(idCard, number, name, sex, age, major);
        this.score = score;
    }


    static class Score {
        private int Math;
        private int Java;
        private int English;

        public Score(int math, int java, int english) {
            Math = math;
            Java = java;
            English = english;
        }

        @Override
        public String toString() {
            return "Score{" +
                    "Math=" + Math +
                    ", Java=" + Java +
                    ", English=" + English +
                    '}';
        }
    }

    @Override
    public String toString() {
        return super.toString() + this.score.toString();
    }
}

class Teacher1 extends Person {
    private int time;

    public Teacher1(int idCard, int number, String name, String sex, int age, int time, String major) {
        super(idCard, number, name, sex, age, major);
        this.time = time;
    }

    @Override
    public String toString() {
        return super.toString() + "time" + time + "}";
    }
}
