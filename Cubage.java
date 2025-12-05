package com.stupra;

public interface Cubage {
    double PI = 3.14;

    double doCubage();
}

class Cylinder implements Cubage {
    private double r;
    private int h;

    public Cylinder(double r, int h) {
        this.r = r;
        this.h = h;
    }

    @Override
    public double doCubage() {
        return (Cubage.PI * r * r * h);
    }

    public double getR() {
        return r;
    }

    public int getH() {
        return h;
    }
}

class Cone implements Cubage {
    private double r;
    private int h;

    public Cone(double r, int h) {
        this.r = r;
        this.h = h;
    }

    @Override
    public double doCubage() {
        return (Cubage.PI * r * r * h) / 3;
    }

    public static void main(String[] args) {
        Cylinder cylinder = new Cylinder(3, 3);
        System.out.println("圆柱的体积是" + cylinder.doCubage());
        Cone cone = new Cone(4, 2);
        System.out.println("圆锥体积是:" + cone.doCubage());
    }
}
