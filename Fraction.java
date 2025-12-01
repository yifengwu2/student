package com.stupra;

public class Fraction {
    //分子
    private int molecular;
    //分母
    private int denominator;

    public Fraction(int molecular, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("分母不能为0");
        }
        this.molecular = molecular;
        this.denominator = denominator;
    }

    public int getMolecular() {
        return molecular;
    }

    public int getDenominator() {
        return denominator;
    }

    public Fraction Addition(Fraction other) {
        int a = this.molecular, b = this.denominator;
        int c = other.molecular, d = other.denominator;
        return new Fraction(a * d + c * b, b * d);
    }

    //减法
    public Fraction Subtraction(Fraction other) {
        int a = this.molecular, b = this.denominator;
        int c = other.molecular, d = other.denominator;
        return new Fraction(a * d - c * b, b * d);
    }

    //乘法
    public  Fraction Multiplication(Fraction other) {
        return new Fraction(this.molecular * other.molecular,
                this.denominator * other.denominator);
    }

    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;

    }


}
