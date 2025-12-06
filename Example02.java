package com.stupra;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

//usb接口
interface Usb {
    void read();

    void write(String message);
}

//处理器
interface Handler {
    //设置下一个
    void setNext(Handler handle);

    //处理成功转接给下一个
    boolean handle(LoginContext loginContext);
}

@Setter
@Getter
class LoginContext {
    private String name;
    private String password;
    private String role;
    private int loginTime;
    private boolean success;

    public LoginContext(String name, String password, String role, int loginTime, boolean success) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.loginTime = loginTime;
        this.success = success;
    }

}

@Slf4j(topic = "ConnectCheckHandler")
class ConnectCheckHandler implements Handler {
    private Handler next;

    @Override
    public void setNext(Handler handle) {
        this.next = handle;
    }

    @Override
    public boolean handle(LoginContext lcx) {
        if (lcx.getName() != null && !lcx.getPassword().trim().isEmpty()) {
            log.debug(" 连接检查通过：用户 {} 已就绪", lcx.getName());
            return next != null ? next.handle(lcx) : true;
        } else {
            log.warn("连接失败：用户名为空");
            return false;
        }
    }
}

@Slf4j(topic = "RootCheckHandler")
class RootCheckHandler implements Handler {
    private Handler next;

    @Override
    public void setNext(Handler handle) {
        this.next = handle;
    }

    @Override
    public boolean handle(LoginContext ctx) {
        if ("admin".equals(ctx.getRole())) {
            return next != null ? next.handle(ctx) : true;
        } else {
            log.warn("用户权限不足");
            return false;
        }
    }
}

@Slf4j
class TimeOutCheckHandler implements Handler {
    private Handler next;

    @Override
    public void setNext(Handler handle) {
        this.next = handle;
    }

    @Override
    public boolean handle(LoginContext loginContext) {
        if (loginContext.getLoginTime() > 10) {
            log.debug("请求超时，时间超过{}", loginContext.getLoginTime());
            return false;
        }
        return true;
    }
}


//usb工厂创建对象
@Slf4j(topic = "UsbFactory")
class UsbFactory {
    private static final Map<String, Usb> map = new HashMap<>();

    public static void creatUsb(String key) throws Exception {
        Class<?> aClass = null;
        try (InputStream in = UsbFactory.class.getResourceAsStream("/config.properties")) {
            if (in == null) {
                throw new FileNotFoundException("config.properties 未找到，请检查是否放在 src/main/resources/ 目录下");
            }
            Properties properties = new Properties();
            properties.load(in);
            log.debug("加载文件成功{}", properties);

            String className = properties.getProperty(key);
            if (className == null) {
                throw new IllegalArgumentException("配置文件中未找到 key: " + key);
            }
            log.debug("已获取到key{}", className);
            aClass = Class.forName("com.stupra." + className);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (aClass != null) {
            Usb usb = (Usb) aClass.getConstructor().newInstance();
            log.debug("创建对象成功{}", usb);
            map.put(key, usb);
        } else {
            throw new IllegalArgumentException("aClass为空");
        }
    }

    public Usb getUsb(String name) {
        if (!map.containsKey(name)) {
            throw new IllegalArgumentException("该元素不在列表中");
        }
        return map.get(name);
    }
}

//手机
class Phone implements Usb {
    private String name;

    private String content;

    public Phone() {
    }

    public Phone(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

    @Override
    public void read() {
        System.out.println("USB设备连接" + name + "成功");
        System.out.println("读取手机的内容是:" + getContent());
    }

    @Override
    public void write(String message) {
        System.out.println("向手机写操作成功");
        if (content != null) {
            setContent(content + message);
        } else {
            setContent(message);
        }

    }
}

//硬盘
class Harddisk implements Usb {
    private String name;
    private String contoent;

    public Harddisk() {
    }

    public Harddisk(String name) {
        this.name = name;
    }

    private String getName() {
        return name;
    }

    private String getContent() {
        return contoent;
    }

    private void setContent(String message) {
        this.contoent = message;
    }

    @Override
    public void read() {
        System.out.println("USB设备连接" + name + "成功");
        System.out.println("接入硬盘的内容" + contoent);
    }

    @Override
    public void write(String message) {
        System.out.println("向硬盘写操作成功");
        if (contoent != null) {
            this.setContent(contoent + message);
        } else {
            setContent(message);
        }
    }
}

//电脑完成对所有usb的操作
class Computer1 implements Usb {
    List<Usb> usbs;

    public Computer1(List<Usb> usbs) {
        this.usbs = usbs;
    }

    @Override
    public void read() {
        for (Usb usb : usbs) {
            usb.read();
        }
    }

    @Override
    public void write(String message) {
        for (Usb usb1 : usbs) {
            usb1.write(message);
        }
    }

    //支持精准写入
    public void writeTo(Usb usb, String message) {
        if (!usbs.contains(usb)) {
            throw new IllegalArgumentException("数据错误");
        }
        usb.write(message);
    }
}


public class Example02 {
    public static void main(String[] args) {
        List<Usb> list = new ArrayList<>();
        Phone phone = new Phone("iphone");
        Harddisk harddisk = new Harddisk("硬盘");

        //组装
        LoginContext ctx = new LoginContext("小明", "123", "admin", 4, false);
        ConnectCheckHandler conn = new ConnectCheckHandler();
        RootCheckHandler root = new RootCheckHandler();
        TimeOutCheckHandler time = new TimeOutCheckHandler();
        conn.setNext(root);
        root.setNext(time);


        list.add(phone);
        list.add(harddisk);
        Computer1 computer = new Computer1(list);
        if (conn.handle(ctx)) {
            System.out.println("登录/写入通过，执行业务...");
            computer.writeTo(phone, "hello");
        } else {
            System.out.println("被责任链拦截！");
        }
        computer.read();

    }

}
