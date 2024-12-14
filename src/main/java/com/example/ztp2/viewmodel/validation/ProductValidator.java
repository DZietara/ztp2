package com.example.ztp2.viewmodel.validation;

import com.example.ztp2.model.ProductRequest;

import java.math.BigDecimal;

public class ProductValidator {

    public static Specification<ProductRequest> isValidName() {
        return productRequest -> {
            if (productRequest.getName() == null || productRequest.getName().trim().isEmpty()) {
                return ValidationResult.failure("Name cannot be empty.");
            }
            if (!productRequest.getName().matches("^[a-zA-Z0-9 ]+$")) {
                return ValidationResult.failure("Name contains invalid characters.");
            }
            if (productRequest.getName().length() < 3 || productRequest.getName().length() > 20) {
                return ValidationResult.failure("Name must be between 3 and 20 characters.");
            }
            return ValidationResult.success();
        };
    }

    public static Specification<ProductRequest> isValidPrice() {
        return productRequest -> {
            if (productRequest.getPrice() == null) {
                return ValidationResult.failure("Invalid price format.");
            }
            if (productRequest.getPrice().compareTo(new BigDecimal("0.01")) < 0) {
                return ValidationResult.failure("The minimum price must be at least 0.01 PLN.");
            }

            int integerDigits = productRequest.getPrice().precision() - productRequest.getPrice().scale();
            int fractionDigits = productRequest.getPrice().scale();

            if (integerDigits > 10) {
                return ValidationResult.failure("The price must have up to 10 digits before the decimal point.");
            }

            if (fractionDigits > 2) {
                return ValidationResult.failure("The price must have exactly 2 digits after the decimal point.");
            }
            return ValidationResult.success();
        };
    }

    public static Specification<ProductRequest> isValidQuantity() {
        return productRequest -> {
            if (productRequest.getQuantity() == null) {
                return ValidationResult.failure("Invalid quantity format");
            }
            if (productRequest.getQuantity() < 0) {
                return ValidationResult.failure("The quantity must be at least 0");
            }
            return ValidationResult.success();
        };
    }

    public static ValidationResult validate(ProductRequest productRequest) {
        Specification<ProductRequest> fullSpecification = isValidName()
                .and(isValidPrice())
                .and(isValidQuantity());

        return fullSpecification.isSatisfiedBy(productRequest);
    }
}