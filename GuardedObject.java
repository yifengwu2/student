package com.stupra;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Slf4j(topic = "GuardedObject")
class GuardedObject {
    private int id;
    private Object response;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    //等待结果
    public Object get(long timeout) {
        synchronized (this) {
            log.debug("开始获取信息");
            long begin = System.currentTimeMillis();
            long parseTime = 0;
            while (response == null) {
                long waitTime = timeout - parseTime;
                if (waitTime <= 0) {
                    break;
                }
                log.debug("开始等待");
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                parseTime = System.currentTimeMillis() - begin;
            }
        }
        return response;
    }

    //传输结果
    public void complete(Object object) {
        synchronized (this) {
            response = object;
            this.notifyAll();
        }
    }

//    public static void removeGuardedObject(int id) {
//        Map<Integer, GuardedObject> boxes = Mailboxes.getBoxes();
//        boxes.move(id);
//    }
}

//中间层面，存guarded
class Mailboxes {
    private static int id;
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();

    public static GuardedObject createGuardedObject() {
        GuardedObject obj = new GuardedObject(getId());
        boxes.put(obj.getId(), obj);
        return obj;
    }

    public synchronized static int getId() {
        return ++id;
    }

    public static GuardedObject getGuardedObject(int id) {
        return boxes.get(id);
    }

    public static Map<Integer, GuardedObject> getBoxes() {
        return boxes;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }

    public static void removeGuardedObject(int id) {
        boxes.remove(id);
    }
}

@Slf4j
class People extends Thread {
    @Override
    public void run() {
        GuardedObject object = Mailboxes.createGuardedObject();
        log.debug("收信人id{}", object.getId());
        Object o = object.get(5000);
        log.debug("收信人id{},内容{}", object.getId(), o);
    }
}

class PostMan extends Thread {
    private int id;
    private String mail;

    public PostMan(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        if (guardedObject != null) {
            guardedObject.complete(mail + id);
            Mailboxes.removeGuardedObject(id);
        }

    }
}

class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Sleeper.sleep(1);
        for (Integer id : Mailboxes.getIds()) {
            new PostMan(id, "内容" + id).start();
        }
    }

    static class Sleeper {
        public static void sleep(int i) {
            try {
                Thread.sleep(i * 1000L);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
