package com.stupra;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Scanner;

public class Example04 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名字");
        String name = sc.nextLine();
        System.out.println("请输入密码");
        String password = sc.nextLine();
        System.out.println("请输入用户年龄");
        int age = Integer.parseInt(sc.nextLine().trim());
        try {
            if (age < 0) {
                throw new MyException("用户名为负数，请重新输入");
            }
        } catch (MyException e) {
            System.out.println(e.getMessage());
            while (true) {
                 age = Integer.parseInt(sc.nextLine().trim());
                if (age >= 0) {
                    break;
                }
                System.out.println("用户名为负数，请重新输入");
            }
        }
        User user = new User(name, age, password);
        Handler1 assemble = Util.assemble();
        if (assemble.handle(user)) {
            System.out.println(user);
        } else {
            System.out.println("查看用户失败");
        }
    }
}

@Setter
class User {
    private String name;
    private int age;
    private String password;

    public User(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    public boolean checkPassword(String input) {
        return Objects.equals(this.password, input);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

}

interface Handler1 {
    void setHandler(Handler1 handler1);

    boolean handle(User user);
}

class LoginHandler implements Handler1 {
    private Handler1 next;

    @Override
    public void setHandler(Handler1 handler1) {
        this.next = handler1;
    }

    @Override
    public boolean handle(User user) {
        if (!user.checkPassword("123")) { //走安全方法
            System.out.println("密码错误，拒绝访问");
            return false;
        }
        return next != null && next.handle(user);
    }
}

class RootHandler implements Handler1 {
    private Handler1 next;

    @Override
    public void setHandler(Handler1 handler1) {
        this.next = handler1;
    }

    @Override
    public boolean handle(User user) {
        return next != null && Objects.equals(user.getName(), "admin");
    }
}

class Util {
    public static Handler1 assemble() {
        LoginHandler loginHandler = new LoginHandler();
        RootHandler rootHandler = new RootHandler();
        loginHandler.setHandler(rootHandler);
        return loginHandler;
    }
}


class MyException extends Exception {
    private String message;

    public MyException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "MyException{" +
                "message='" + message + '\'' +
                '}';
    }

    @Override
    public String getMessage() {
        return message;
    }
}
