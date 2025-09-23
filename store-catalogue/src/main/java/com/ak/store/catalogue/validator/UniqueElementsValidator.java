package com.ak.store.catalogue.validator;

import com.ak.store.catalogue.model.annotation.UniqueElements;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.List;

public class UniqueElementsValidator implements ConstraintValidator<UniqueElements, List<?>> {
    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return list.size() == new HashSet<>(list).size();
    }
}
