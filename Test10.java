package com.stupra;

//泛型类，只接受Number的子类,找最大值，和最小值
class LtdGenerics<T extends Number> {
    private T[] arr;

    public LtdGenerics(T[] arr) {
        this.arr = arr;
    }

    public int findMax() {
        T m = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (m.doubleValue() < arr[i].doubleValue()) {
                m = arr[i];
            }
        }
        return (int) m;
    }

    public int findMin() {
        T m = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (m.doubleValue() > arr[i].doubleValue()) {
                m = arr[i];
            }
        }
        return (int) m;
    }
}

public class Test10 {
    public static void main(String[] args) {
        Integer[] integers = {19, 33, 4, 553, 35, 53, 55};
        LtdGenerics<Integer> integerLtdGenerics = new LtdGenerics<>(integers);
        int max = integerLtdGenerics.findMax();
        System.out.println(max);
    }

}
