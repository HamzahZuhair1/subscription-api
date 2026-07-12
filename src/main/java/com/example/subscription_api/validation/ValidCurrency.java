package com.example.subscription_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrency {
    String message() default "Invalid currency code. Must be a valid ISO 4217 currency (e.g., JOD, USD)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}