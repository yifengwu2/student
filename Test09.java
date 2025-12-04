package com.stupra;

import java.lang.reflect.Array;
import java.util.Iterator;

interface Generics<T> {
    T generator();
}

class ArrayGenerator<T> implements Generics<T>, Iterable<T> {
    private volatile T[] arr;
    private int size;
    private Class<T> aClass;
    private int cursor;
    private final Object lock = new Object();

    @SuppressWarnings("unchecked")
    public ArrayGenerator(Class<T> clazz, int cap) {
        this.aClass = clazz;
        this.arr = (T[]) Array.newInstance(clazz, cap);
        this.size = 0;
    }

    //添加
    public void add(T element) {
        synchronized (lock) {
            if (size == arr.length) {
                expand();
            }
            arr[size] = element;
            size++;
        }
    }

    //删除
    public T remove(int index) {
        synchronized (lock) {
            if (index < 0 || index >= size) {
                throw new IllegalArgumentException();
            }
            if (index == size - 1) {
                return arr[--size];
            }
            T t = arr[index];
            for (int i = index + 1; i < size; i++) {
                arr[i - 1] = arr[i];
            }
            return t;
        }
    }

    @Override
    public Iterator<T> iterator() {
        synchronized (lock) {
            return new Iterator<>() {
                int i = 0;

                @Override
                public boolean hasNext() {
                    return i < size;
                }

                @Override
                public T next() {
                    return arr[i++];
                }
            };
        }
    }

    //扩容
    @SuppressWarnings("unchecked")
    public synchronized void expand() {
        synchronized (lock) {
            int newcap = arr.length * 2;
            T[] newarr = (T[]) Array.newInstance(aClass, newcap);
            System.arraycopy(arr, 0, newarr, 0, size);
            arr = newarr;
        }
    }


    @Override
    public T generator() {
        synchronized (lock) {
            if (cursor >= size) {
                return null;
            }
            return arr[cursor++];
        }
    }

    public void reset() {
        throw new UnsupportedOperationException("reset() is not thread-safe.");
    }
}

class ArrayUtil {
    public static <T> void obtain(Generics<T> generics) {
        for (int i = 0; i < 3; i++) {
            System.out.print(generics.generator() + " ");
        }
    }
}

public class Test09 {
    public static void main(String[] args) {
        ArrayGenerator<String> strs = new ArrayGenerator<>(String.class, 3);
        strs.add("Beijing");
        strs.add("Shanghai");
        strs.add("Tianjin");
        for (String s : strs) {
            System.out.print(s + " ");
        }
//        ArrayUtil.obtain(strs);

        System.out.println();

        ArrayGenerator<Integer> array = new ArrayGenerator<>(Integer.class, 2);
        array.add(123);
        array.add(-456);
        array.add(789);
        ArrayUtil.obtain(array);

    }

}
