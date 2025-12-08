package com.stupra;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Example06 {

    public static boolean ispalindrome(String s) {
        LinkedList<Character> list = new LinkedList<>();

        if (s == null) return false;
        int n = s.length();
        if (n == 0) return true;
        char[] chars = s.toCharArray();
        for (int i = n - 1; i >= 0; i--) {
            list.add(chars[i]);
        }
        int i = 0;
        while (!list.isEmpty()) {
            Character c = list.pollLast();
            if (c != s.charAt(i)) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(Example06.ispalindrome("aaaaaaa"));
    }
}
