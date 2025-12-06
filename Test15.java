package com.stupra;

public class Test15 {
    public static void getSeason(int mon) {
        switch (mon) {
            case 12, 1, 2 -> System.out.println(Season.WINTER.getContext());
            case 3, 4, 5 -> System.out.println(Season.SPRING.getContext());
            case 6, 7, 8 -> System.out.println(Season.SUMMER.getContext());
            case 9, 10, 11 -> System.out.println(Season.AUTUMN.getContext());
            default -> System.out.println("请输入正确月份");
        }
    }

    public static void main(String[] args) {
        getSeason(11);

        for (Season season : Season.values()) {
            System.out.println(season.getContext());
        }
    }
}
