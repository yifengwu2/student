package com.stupra;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Example03 {
    public static void main(String[] args) {
        Employee1 employee1 = new Employee1(4, "zhaoliu", 40, 7500);
        Employee1 employee2 = new Employee1(3, "wangwu", 33, 6500);
        Employee1 employee3 = new Employee1(2, "lisi", 28, 4500);
        Employee1 employee4 = new Employee1(5, "zhangsan", 33, 7200);
        Employee1 employee5 = new Employee1(1, "qinba", 24, 4200);

        Employee1[] emps = {employee1, employee2, employee3, employee4, employee5};


        List<Employee1> list = new ArrayList<>();
        list.add(employee1);
        list.add(employee3);
        list.add(employee4);
        list.add(employee2);
        list.add(employee5);

        list.sort((o1, o2) -> {
            if (o1.getAge() != o2.getAge()) {
                return o2.getAge() - o1.getAge();
            }
            return o1.getId() - o2.getId();
        });
        System.out.println(list);

    }


}

@Setter
@Getter
class Employee1 implements Comparable<Employee1> {
    private int id;
    private String name;
    private int age;
    private int salary;

    public Employee1(int id, String name, int age, int salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public int compareTo(Employee1 o) {
        return o.getAge() - this.getAge();
    }

    @Override
    public String toString() {
        return "Employee1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }

}


