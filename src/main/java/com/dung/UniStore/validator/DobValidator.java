package com.dung.UniStore.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobConstraint, Date> {

    private int min;

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) return true;

        // Chuyển đổi Date sang LocalDate
        LocalDate localDate = value.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Tính số năm giữa localDate và ngày hiện tại
        long years = ChronoUnit.YEARS.between(localDate, LocalDate.now());

        return years >= min;
    }

    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
}