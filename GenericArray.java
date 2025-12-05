package com.stupra;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 设计一个泛型数组类，并创建数组类的对象
 */
public class GenericArray<T> {
    private T[] arr;
    private int size;
    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public GenericArray(Class<T> clazz, int cap) {
        this.arr = (T[]) Array.newInstance(clazz, cap);
        this.clazz = clazz;
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public void expand() {
        int newCap = arr.length * 2;
        T[] ans = (T[]) Array.newInstance(clazz, newCap);
//        int i = 0;
//        for (T a : arr) {
//            ans[i++] = a;
//        }
//        arr = ans;
        System.arraycopy(arr, 0, ans, 0, size);
        this.arr = ans;
    }

    public void add(T a) {
        if (size == arr.length) {
            //扩容
            expand();
        }
        arr[size] = a;
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= arr.length) {
            throw new IndexOutOfBoundsException();
        }
        return arr[index];
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "GenericArray{" +
                "arr=" + Arrays.toString(arr) +
                '}';
    }

    public static void main(String[] args) {
        Class<String> aClass = String.class;
        GenericArray<String> array = new GenericArray<>(aClass, 10);
        array.size();

    }
}
