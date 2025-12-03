package com.stupra;

abstract class Teacher0 {
    private int baseSalary;
    private int hourlySubsidy;
    private int time;

    public Teacher0(int wage, int sub, int time) {
        this.baseSalary = wage;
        this.hourlySubsidy = sub;
        this.time = time;
    }

    protected int getBaseSalary() {
        return baseSalary;
    }

    protected int getHourlySubsidy() {
        return hourlySubsidy;
    }

    protected final int calculateMonthlySalary() {
        return getHourlySubsidy() * time + getBaseSalary();
    }

}

class Professor extends Teacher0 {

    public Professor(int time) {
        super(5000, 70, time);
    }

}

class Lecture extends Teacher0 {

    public Lecture(int time) {
        super(2600, 55, time);
    }

}

public class Test07 {
    public static void main(String[] args) {
        Lecture lecture = new Lecture(5);
        System.out.println(lecture.calculateMonthlySalary());

        Professor professor = new Professor(4);
        System.out.println(professor.calculateMonthlySalary());

    }
}
