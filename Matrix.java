package com.stupra;

import java.util.Arrays;

public class Matrix {
    private int[][] grid;

    public Matrix(int[][] grid) {
        if (grid == null || grid.length == 0) {
            throw new IllegalArgumentException("矩阵不能为空");
        }
        // 深拷贝，防止外部篡改
        this.grid = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            this.grid[i] = grid[i].clone();
        }
    }

    //矩阵的加法
    public Matrix add(Matrix other) {
        if (this.grid.length != other.grid.length || other.grid[0].length != this.grid[0].length) {
            throw new IllegalArgumentException("矩阵维度不匹配，无法相加");
        }
        int[][] grid1 = this.grid;
        int[][] grid2 = other.grid;
        int row = grid1.length;
        int col = grid1[0].length;
        int[][] res = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int grid = grid1[i][j] + grid2[i][j];
                res[i][j] = grid;
            }
        }
        return new Matrix(res);
    }

    //矩阵的减法
    public Matrix Subtraction(Matrix other) {
        if (this.grid.length != other.grid.length || other.grid[0].length != this.grid[0].length) {
            throw new IllegalArgumentException("矩阵不匹配");
        }
        int row = this.grid.length;
        int col = this.grid[0].length;

        int[][] ints = new int[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int num = this.grid[i][j] - other.grid[i][j];
                ints[i][j] = num;
            }
        }
        return new Matrix(ints);
    }

    //矩阵的乘法
    public Matrix Multiplication(Matrix other) {
        int n = this.grid.length;
        int m = this.grid[0].length;
        int p = other.grid[0].length;
        int[][] res = new int[n][p];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < p; k++) {
                int sum = 0;
                for (int j = 0; j < m; j++) {
                    sum += this.grid[i][j] * other.grid[j][k];
                }
                res[i][k] = sum;
            }
        }
        return new Matrix(res);


    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                sb.append(grid[i][j]);
                if (j < grid[i].length - 1) sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
