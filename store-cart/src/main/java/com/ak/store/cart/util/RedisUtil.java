package com.ak.store.cart.util;

public abstract class RedisUtil {
    public static Integer parseInt(Object value) {
        return Integer.parseInt(value.toString());
    }

    public static Long parseLong(Object value) {
        return Long.parseLong(value.toString());
    }
}
