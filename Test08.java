package com.stupra;

public class Test08 {
    public static void main(String[] args) {
        // 🔹 矩形棱柱：高=4，宽=3，深=5（即长方体 3×5×4）
        Rectangle rect = new Rectangle(4, 3, 5);
        System.out.println("矩形棱柱：体积=" + rect.bulk() + ", 表面积=" + rect.surfaceArea());
        // 预期：体积=60，表面积=94（2×15 + 2×(3+5)×4 = 30 + 64）

        // 🔹 圆柱：高=10，半径=2
        Circle cyl = new Circle(10, 2);
        System.out.println("圆柱：体积=" + cyl.bulk() + ", 表面积=" + cyl.surfaceArea());
        // 预期：体积≈125.66→126，表面积≈150.80→151
    }

}

abstract class Column {
    private final int h;

    public Column(int h) {
        if (h < 0) throw new IllegalArgumentException("高度不能为负");
        this.h = h;
    }

    public abstract int surfaceArea();

    public abstract int bulk();

    protected int getH() {
        return h;
    }
}

class triangle extends Column {
    private int a;
    private int b;
    private int c;

    public triangle(int h, int l1, int l2, int l3) {
        super(h);
        if (a <= 0 || b <= 0 || c <= 0)
            throw new IllegalArgumentException("三角形边长必须为正整数");
        // ✅ 简单三角形存在性检查（满足两边之和 > 第三边）
        if (a + b <= c || a + c <= b || b + c <= a)
            throw new IllegalArgumentException("三边无法构成三角形: " + a + "," + b + "," + c);
        this.a = l1;
        this.b = l2;
        this.c = l3;
    }

    @Override
    public int surfaceArea() {
        double s = (a + b + c) / 2.0;
        double sqrt = Math.sqrt(s * (s - a) * (s - b) * (s - c));
        double v = sqrt * 2;
        return (int) Math.round((a + b + c) * getH() + v);
    }

    @Override
    public int bulk() {
        double s = (a + b + c) / 2.0; //关键：除以 2.0 → 得 double
        double baseArea = Math.sqrt(s * (s - a) * (s - b) * (s - c));
        return (int) Math.round(baseArea * getH()); //四舍五入取整

    }
}

class Rectangle extends Column {
    private int width;
    private int high;

    public Rectangle(int h, int width, int high) {
        super(h);
        this.width = width;
        this.high = high;
    }

    @Override
    public int surfaceArea() {
        return width * high * 2 + (width + high) * getH();
    }

    @Override
    public int bulk() {
        return 0;
    }
}

class Circle extends Column {
    private final int radius;


    public Circle(int h, int r) {
        super(h);
        this.radius = r;
    }

    @Override
    public int surfaceArea() {
        double baseArea = Math.PI * radius * radius;
        double lateralArea = 2 * Math.PI * radius * getH();
        return (int) Math.round(2 * baseArea + lateralArea);
    }

    @Override
    public int bulk() {
        return (int) Math.round(Math.PI * radius * radius * getH());
    }
}
