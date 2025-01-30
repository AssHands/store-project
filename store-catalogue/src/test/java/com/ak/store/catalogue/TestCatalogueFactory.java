package com.ak.store.catalogue;

import com.ak.store.catalogue.model.entity.Category;
import com.ak.store.catalogue.model.entity.Characteristic;
import com.ak.store.catalogue.model.entity.RangeValue;
import com.ak.store.catalogue.model.entity.TextValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestCatalogueFactory {
    private static Characteristic createTextCharacteristic(Long id, String name, Long categoryId, String textValue) {
        return Characteristic.builder()
                .id(id)
                .isText(true)
                .category(Set.of(Category.builder().id(categoryId).build()))
                .name(name)
                .textValues(Set.of(
                        TextValue.builder()
                                .id(id)
                                .textValue(textValue)
                                .characteristic(Characteristic.builder().id(id).build())
                                .build()))
                .build();
    }

    private static Characteristic createNumericCharacteristic(Long id, String name, Long categoryId, int fromValue, int toValue) {
        return Characteristic.builder()
                .id(id)
                .isText(false)
                .category(Set.of(Category.builder().id(categoryId).build()))
                .name(name)
                .rangeValues(Set.of(
                        RangeValue.builder()
                                .id(id)
                                .fromValue(fromValue)
                                .toValue(toValue)
                                .characteristic(Characteristic.builder().id(id).build())
                                .build()))
                .build();
    }

    public static List<Characteristic> createTextCharacteristicList(Long categoryId, int count) {
        List<Characteristic> characteristics = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            characteristics.add(createTextCharacteristic((long) (i+1), "Characteristic " + (i + 1), categoryId, "test_value " + (i+1)));
        }
        return characteristics;
    }

    public static List<Characteristic> createNumericCharacteristicList(Long categoryId, int count) {
        List<Characteristic> characteristics = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            characteristics.add(createNumericCharacteristic((long) (i+1), "Characteristic " + (i + 1), categoryId, i, i + 5));
        }
        return characteristics;
    }

    public static List<Characteristic> createAllCharacteristicList(Long categoryId, int count) {
        if(count == 1) throw new RuntimeException("must: count > 1");

        List<Characteristic> characteristics = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if(i % 2 == 0)
                characteristics.add(createNumericCharacteristic((long) (i+1), "Characteristic " + (i + 1), categoryId, i, i + 5));
            else
                characteristics.add(createTextCharacteristic((long) (i+1), "Characteristic " + (i + 1), categoryId, "test_value " + (i+1)));
        }

        return characteristics;
    }

    public static Category createCategory(Long id, String name) {
        return Category.builder().id(id).name(name).build();
    }

    public static Category createCategory(Long id, String name, Long parentId) {
        return Category.builder().id(id).name(name).parentId(parentId).build();
    }
}