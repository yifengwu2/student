package com.stupra;

public class Line {
    //起点坐标
    private Point start;
    //终点坐标
    private Point end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    //获取线段长度
    public double getLen() {
        Point startpoint = getStart();
        Point endpoint = getEnd();
        int y = endpoint.y - startpoint.y;
        int x = endpoint.x - startpoint.x;
        int len = y * y + x * x;
        return Math.sqrt(len);
    }

    //平移线段
    public Line moveLine(int x, int y) {
        Point startpoint = getStart();
        Point endpoint = getEnd();

        Point newstartpoint = new Point(startpoint.getX() + x, startpoint.getY() + y);
        Point newendpoint = new Point(endpoint.getX() + x, endpoint.getY() + y);

        return new Line(newstartpoint, newendpoint);
    }

    //讲线段绕原点旋转
    public Line rotate(double angle) {
        Point start = this.getStart();
        Point end = this.getEnd();
        int x = start.getX();
        int y = start.getY();
        int x1 = end.getX();
        int y1 = end.getY();

        Point newstart = rota(x, y, angle);
        Point newend = rota(x1, y1, angle);

        return new Line(newstart, newend);
    }

    private Point rota(int x, int y, double angle) {
        double angle1 =  Math.toRadians(angle);
        int x1 = (int) (x * Math.cos(angle1) - y * Math.sin(angle1));
        int y1 = (int) (x * Math.sin(angle1) + y * Math.cos(angle1));
        return new Point(x1, y1);
    }


    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
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

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
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
