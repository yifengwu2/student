package com.stupra;

public class Plural {
    //实部
    private int real;
    //虚部
    private int imaginary;

    public Plural(int real, int imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    //加法
    public Plural add(Plural other) {
        int real = this.real + other.real;
        int imaginary = this.imaginary + other.imaginary;

        return new Plural(real, imaginary);
    }

    //减法
    public Plural subtract(Plural other) {
        int real = this.real - other.real;
        int imaginary = this.imaginary - other.imaginary;

        return new Plural(real, imaginary);
    }

    //乘法
    public Plural multiply(Plural other) {
        int a = this.real;
        int b = this.imaginary;
        int c = other.real;
        int d = other.imaginary;
        int newReal = a * c - b * d;
        int imaginary = a * d + b * c;
        return new Plural(newReal, imaginary);
    }

    @Override
    public String toString() {
        if (imaginary == 0) {
            return String.valueOf(real);
        } else if (real == 0) {
            return imaginary + "i";
        } else if (imaginary < 0) {
            return real + "" + imaginary + "i"; // 自动带负号
        } else {
            return real + "+" + imaginary + "i";
        }
    }

    public static void main(String[] args) {
        Plural p1 = new Plural(2, 3);  // 2 + 3i
        Plural p2 = new Plural(1, 4);  // 1 + 4i

        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);
        System.out.println("p1 + p2 = " + p1.add(p2));     // 3+7i
        System.out.println("p1 - p2 = " + p1.subtract(p2)); // 1-1i
        System.out.println("p1 * p2 = " + p1.multiply(p2)); // -10+11i ✅
    }
}
