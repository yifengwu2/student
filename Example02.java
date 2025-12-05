package com.stupra;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

interface Usb {
    void read();

    void write(String message);
}

@Slf4j
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

        list.add(phone);
        list.add(harddisk);
        Computer1 computer = new Computer1(list);
        computer.writeTo(phone, "hello");
        computer.read();

    }

}
