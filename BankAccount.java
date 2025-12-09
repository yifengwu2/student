package com.stupra;

public class BankAccount {
    private static final Object lock = new Object();
    int balance = 1000;

    public int getBalance() {
        return balance;
    }

    public void transfer(BankAccount to, int amount) {
        synchronized (lock) {
            if (amount > this.balance) {
                System.out.println("转账失败");
            }
            balance -= amount;
            to.balance += amount;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BankAccount count1 = new BankAccount();
        BankAccount count2 = new BankAccount();
        Thread t1 = new Thread(() -> {
            count1.transfer(count2, 400);

        }, "t1");

        Thread t2 = new Thread(() -> {

            count2.transfer(count1, 300);

        }, "t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println(count1.getBalance());


    }
}
