package com.gabrielanceski.tccifrs.utils;

public class ObjectUtils {

    public static boolean nonNullOrEmpty(String str) {
        return str != null && !str.isEmpty() && !str.isBlank();
    }

}
