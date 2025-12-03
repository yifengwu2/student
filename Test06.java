package com.stupra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

abstract class Application {
    public abstract void working();

}

class AppUtil {
    public static void start(List<Application> apps) {
        for (Application app : apps) {
            app.working();
        }
    }
}

//电视机
class Television extends Application {
    @Override
    public void working() {
        System.out.println("电视机在播放节目");
    }
}

//洗衣机
class WashMachine extends Application {
    @Override
    public void working() {
        System.out.println("洗衣机在洗衣服");
    }
}

class Refrigerator extends Application {
    @Override
    public void working() {
        System.out.println("电冰箱在制冷");
    }
}

class Microwave extends Application {
    @Override
    public void working() {
        System.out.println("微波炉再加热");
    }
}
public class Test06{

}
