package com.stupra;

import java.util.concurrent.atomic.AtomicInteger;

public interface Account {
    Integer getBalance();

    void withdraw(Integer amount);
}

class AccountCas implements Account {
    private AtomicInteger balance;

    public AccountCas() {
        this.balance = new AtomicInteger();
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
//        while (true) {
//            //获取最新值
//            int prev = balance.get();
//            //要修改的值
//            int next = prev - amount;
//            //真正修改
//            if (balance.compareAndSet(prev, next)) {
//                break;
//            }
//        }
        balance.addAndGet(-1 * amount);
    }
}
