package com.pawemie.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Computer {

    private final String name;
    private final LocalDate dateOfBooking;
    private final BigDecimal costUSD;
    private final BigDecimal costPLN;

    public Computer(String name, LocalDate dateOfBooking, BigDecimal costUSD, BigDecimal costPLN) {
        this.name = name;
        this.dateOfBooking = dateOfBooking;
        this.costUSD = costUSD;
        this.costPLN = costPLN;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBooking() {
        return dateOfBooking;
    }

    public BigDecimal getCostUSD() {
        return costUSD;
    }

    public BigDecimal getCostPLN() {
        return costPLN;
    }
}
