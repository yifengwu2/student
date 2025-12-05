package com.stupra;

import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class SimpleBlockingQueue {
    private final Deque<Message> queue = new LinkedList<>();
    private int capital;
    private static int id;

    public SimpleBlockingQueue(int capital) {
        if (capital < 0) {
            throw new IllegalArgumentException("容量错误");
        }
        this.capital = capital;
    }

    //获取id
//    public static int getId() {
//        return ++id;
//    }


    //放进消息队列
    public void put(Message msg) {
        synchronized (this) {
            while (queue.size() == capital) {
                try {
                    log.debug("队列已满,生产线程等待");
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            queue.addLast(msg);
            log.debug("已生产消息{}", msg);
            this.notifyAll();
        }
    }

    //从消息队列中取
    public Message take() {
        synchronized (this) {
            while (queue.isEmpty()) {
                try {
                    log.debug("队列为空，消费线程等待");
                    this.wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            Message message = queue.removeFirst();
            log.debug("消费的消息{}", message);
            this.notifyAll();
            return message;

        }
    }

    public static void main(String[] args) {
        SimpleBlockingQueue queue = new SimpleBlockingQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id, "值" + id));
            }, "生产者" + i).start();
        }
        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                Message take = queue.take();
            }
        }, "消费者").start();

    }

}

class Message {
    private int id;

    private Object mail;

    public Message(int id, Object mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                '}';
    }
}
