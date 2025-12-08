package com.stupra;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Example07 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                if (sc.hasNext()) {
                    String s = sc.nextLine();
                    list.add(s.trim());
                } else {
                    list.add("");
                }
            }
            String res = list.stream().filter(Objects::nonNull)
                    .filter(s -> s.toLowerCase().startsWith("a"))
                    .filter(s -> s.length() > 2)
                    .sorted()
                    .collect(Collectors.joining(" "));

            System.out.println(res);
        }finally {
            sc.close();
        }



    }
}
