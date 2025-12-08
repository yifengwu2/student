package com.stupra;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Servant {
    private String name;
    private String position;
    private int age;
    private int salary;

    public Servant(String name, String position, int age, int salary) {
        this.name = name;
        this.position = position;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Servant{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }

    public static void main(String[] args) {
        //创建一个list对象
        List<Servant> list = new ArrayList<>();
        Servant servant1 = new Servant("王振", "经理", 40, 5000);
        Servant servant2 = new Servant("刘莉莉", "出纳", 33, 3200);
        Servant servant3 = new Servant("王红", "秘书", 25, 2800);
        list.add(servant1);
        list.add(servant2);
        list.add(servant3);
        //将元素遍历一遍
        for (Servant servant : list) {
            System.out.println(servant);
        }
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            Servant servant = list.get(i);
            if (Objects.equals(servant.getName(), "刘莉莉")) {
                index = i;
                break;
            }
        }
        System.out.println("-----------------------------------------------------");
        list.add(index, new Servant("张芳", "副经理", 38, 4200));
        for (Servant servant : list) {
            System.out.println(servant);
        }
        System.out.println("---------------------------------");
        int index2=0;
        for (int i = 0; i < list.size(); i++) {
            Servant servant = list.get(i);
            if (Objects.equals(servant.getName(), "刘莉莉")) {
                index2 = i;
                break;
            }
        }
        list.remove(index2);
        for (Servant servant : list) {
            System.out.println(servant);
        }





    }
}
