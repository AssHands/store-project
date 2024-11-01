package com.ak.store.queryGenerator;

import com.ak.store.common.dto.ProductPreviewDTO;
import org.springframework.data.util.ParsingUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Map<String, ? super Object> map = new HashMap<>();
        map.put("abc", 5);
        map.put("kk","xyz");
        map.put("k","j");

        Class<?> clazz = ProductPreviewDTO.class;
        var a = clazz.getDeclaredFields();
        //System.out.println(clazz.getDeclaredFields());

        var b = Arrays.stream(clazz.getDeclaredFields())
                .map(field -> ParsingUtils.reconcatenateCamelCase(field.getName(), "_"))
                .toList();
        b.forEach(System.out::println);

        var t = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getType)
                .toList();

        var d= t.get(0);
    }
}
