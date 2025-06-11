package com.ak.store.search.util;

public abstract class ProductFields {
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String CURRENT_PRICE = "current_price";
    public static final String DISCOUNT_PERCENTAGE = "discount_percentage";
    public static final String CATEGORY_ID = "category_id";
    public static final String AMOUNT_SALES = "amount_sales";
    public static final String AMOUNT_REVIEWS = "amount_reviews";
    public static final String GRADE = "grade";
    public static final String IS_AVAILABLE = "is_available";
    public static final String CHARACTERISTICS = "characteristics";

    public static abstract class Characteristics {
        public static final String ID = "characteristics.id";
        public static final String TEXT_VALUE = "characteristics.text_value";
        public static final String NUMERIC_VALUE = "characteristics.numeric_value";
    }
}