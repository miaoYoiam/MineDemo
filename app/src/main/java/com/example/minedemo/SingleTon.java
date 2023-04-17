package com.example.minedemo;

/**
 * Day：2022/3/30 5:10 下午
 *
 * @author zhanglei
 */
public class SingleTon {
    private static SingleTon singleTon = new SingleTon();
    public static int count1;
    public static int count2 = 0;

    private SingleTon() {
        count1++;
        count2++;
    }

    public static SingleTon getInstance() {
        return singleTon;
    }
}
