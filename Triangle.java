package com.stupra;

public class Triangle {
    private Point point1;
    private Point point2;
    private Point point3;

    public Triangle(Point point1, Point point2, Point point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    //求三角形周长
    public double perimeter() {
        double l1 = getLen(point1, point2);
        double l2 = getLen(point1, point3);
        double l3 = getLen(point2, point3);
        return l1 + l2 + l3;
    }

    private double getLen(Point point1, Point point2) {
        int x = point1.x - point2.x;
        int y = point1.y - point2.y;
        return Math.sqrt(x * x + y * y);
    }

    //求面积
    public double area() {
        double a = getLen(point1, point2);
        double b = getLen(point2, point3);
        double c = getLen(point1, point3);

        double s = (a + b + c) / 2;

        return Math.sqrt(s * (s - a) * (s - b) * (s - c));


    }

    //对三角形进行平移
    public Triangle remove(int x, int y) {
        return getTran(x, y);
    }

    private Triangle getTran(int x, int y) {
        int newPx = this.point1.x + x;
        int newPy = this.point1.y + y;

        Point point1 = new Point(newPx, newPy);
        int newPx1 = this.point2.x + x;
        int newPy1 = this.point2.y + y;

        Point point2 = new Point(newPx1, newPy1);
        int newPx3 = this.point3.x + x;
        int newPy3 = this.point3.y + y;
        Point point = new Point(newPx3, newPy3);
        return new Triangle(point1, point2, point);

    }

    public Triangle rotate(double angle) {
        Point p1 = rotatePoint(this.point1, angle);
        Point p2 = rotatePoint(this.point2, angle);
        Point p3 = rotatePoint(this.point3, angle);
        return new Triangle(p1, p2, p3);
    }

    private Point rotatePoint(Point p, double angle) {
        double radian = Math.toRadians(angle);
        double cos = Math.cos(radian);
        double sin = Math.sin(radian);

        long x = p.getX(), y = p.getY();
        int newX = (int) Math.round(x * cos - y * sin);
        int newY = (int) Math.round(x * sin + y * cos);
        return new Point(newX, newY);
    }


    @Override
    public String toString() {
        return "Triangle{" +
                "point1=" + point1 +
                ", point2=" + point2 +
                ", point3=" + point3 +
                '}';
    }

    static class Point {
        private int x;

        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
