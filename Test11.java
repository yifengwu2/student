package com.stupra;

class Circle1 {
    private static final double PI = 3.14;
    private CirclePoint point;
    private double r;

    public Circle1(int x, int y, int r) {
        this.point = new CirclePoint(x, y);
        this.r = r;
    }

    public CirclePoint getPoint() {
        return point;
    }

    //移动圆
    public Circle1 move(int x, int y) {
        point.setX(point.getX() + x);
        point.setY(point.getY() + y);
        return this;
    }

    //计算圆的面积
    public double area() {
        return PI * r * r;
    }

    //计算周长
    public double len() {
        return PI * r * 2;
    }

    @Override
    public String toString() {
        return "Circle1{" +
                "point=" + point +
                ", r=" + r +
                '}';
    }

    private static class CirclePoint {
        private int x;
        private int y;

        public CirclePoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "CirclePoint{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        private int getX() {
            return x;
        }

        private int getY() {
            return y;
        }

        private void setX(int x) {
            this.x = x;
        }

        private void setY(int y) {
            this.y = y;
        }
    }
}

public class Test11 {
    public static void main(String[] args) {
        Circle1 circle1 = new Circle1(2, 3, 3);
        System.out.println(circle1.move(1, 1));


    }
}
