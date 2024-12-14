package com.example.ztp2.viewmodel.validation;

public interface Specification<T> {
    ValidationResult isSatisfiedBy(T t);

    default Specification<T> and(Specification<T> other) {
        return t -> {
            ValidationResult result = this.isSatisfiedBy(t);
            return result.isValid() ? other.isSatisfiedBy(t) : result;
        };
    }

    default Specification<T> or(Specification<T> other) {
        return t -> {
            ValidationResult result = this.isSatisfiedBy(t);
            return result.isValid() ? result : other.isSatisfiedBy(t);
        };
    }
}