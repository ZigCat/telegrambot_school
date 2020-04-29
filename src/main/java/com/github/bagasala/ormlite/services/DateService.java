package com.github.bagasala.ormlite.services;

public class DateService {
    public static boolean isValidDate(String date){
        return date.length() == 10 && date.charAt(4) == ' ' && date.charAt(7) == ' ';
    }
}
