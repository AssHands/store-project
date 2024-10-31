package com.ak.store.queryGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        Map<String, ? super Object> map = new HashMap<>();
        map.put("abc", 5);
        map.put("kk","xyz");
        map.put("k","j");

        for(var e : map.entrySet()) {
            System.out.println(e.getKey().getClass().getName());
            System.out.println(e.getValue().getClass().getName());
        }
    }
}
