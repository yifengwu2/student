package com.stupra;

public class NaturalNumbers {
    private int number;

    public NaturalNumbers(int number) {
        this.number = number;
    }

    //计算1到这个自然数的各个数之和的方法
    public int toSum() {
        return sumRecursive(this.number);
    }

    //私有方法辅助
    private int sumRecursive(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return n + sumRecursive(n - 1);

    }

    //判断这个自然数是否为素数
    public boolean isPrime() {
        int n = this.number;

        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }

        return true;
    }


}
