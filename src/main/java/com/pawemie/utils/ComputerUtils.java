package com.pawemie.utils;

import com.pawemie.exceptions.ValidationException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class ComputerUtils {

    public static void validateName(String name) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name cannot be empty");
        }
    }

    public static BigDecimal parseAndValidatePrice(String priceStr) throws ValidationException {
        try {
            BigDecimal price = new BigDecimal(priceStr);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("Price must not be negative");
            }
            return price;
        } catch (NumberFormatException e) {
            throw new ValidationException("Price must be a valid number");
        }
    }

    public static LocalDate adjustToBusinessDay(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();

        if (day == DayOfWeek.SATURDAY) {
            return date.minusDays(1);
        } else if (day == DayOfWeek.SUNDAY) {
            return date.minusDays(2);
        } else {
            return date;
        }
    }

}
