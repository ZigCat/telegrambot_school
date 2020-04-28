package com.github.bagasala.ormlite;

import java.time.LocalTime;

public class Test {
    public static void main(String[] args) {
        LocalTime localTime = LocalTime.now();
        System.out.println(LocalTime.of(1,9).toString().equals("0"+localTime.getHour()+":"+"0"+localTime.getMinute()));
    }
}
