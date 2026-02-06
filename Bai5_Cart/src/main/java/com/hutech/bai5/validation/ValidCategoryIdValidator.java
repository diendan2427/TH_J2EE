package com.hutech.bai5.validation;

import com.hutech.bai5.repository.CategoryRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Bài 5: Validator implementation cho @ValidCategoryId
 */
@Component
public class ValidCategoryIdValidator implements ConstraintValidator<ValidCategoryId, String> {
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void initialize(ValidCategoryId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String categoryId, ConstraintValidatorContext context) {
        if (categoryId == null || categoryId.isEmpty()) {
            return true; // Cho phép null, validation khác sẽ check required
        }
        return categoryRepository.existsById(categoryId);
    }
}
