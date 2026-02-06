package com.hutech.bai5.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Bài 5: Custom Validator - Kiểm tra CategoryId hợp lệ
 */
@Documented
@Constraint(validatedBy = ValidCategoryIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategoryId {
    String message() default "Danh mục không hợp lệ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
