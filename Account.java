package com.stupra;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public interface Account {
    Integer getBalance();

    void withdraw(Integer amount);
}

class DecimalAccountCas implements DecimalAccount {
    private AtomicReference<BigDecimal> balance;

    public DecimalAccountCas(BigDecimal balance) {
        this.balance = new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while (true){
            BigDecimal prev = balance.get();
            BigDecimal next = prev.subtract(amount);
            if (balance.compareAndSet(prev,next)){
                break;
            }
        }
    }

    public static void main(String[] args) {
        DecimalAccount.demo( new DecimalAccountCas(new BigDecimal("10000")));
    }
}

interface DecimalAccount {
    //获取余额
    BigDecimal getBalance();

    //取款
    void withdraw(BigDecimal amount);

    static void demo(DecimalAccount account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(BigDecimal.TEN);
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(account.getBalance());
    }
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
