package com.ak.store.catalogue.model.annotation;

import com.ak.store.catalogue.validator.UniqueElementsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;



import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueElementsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueElements {
    String message() default "List contains duplicate elements";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
