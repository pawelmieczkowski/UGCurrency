package com.pawemie.service;

import com.pawemie.exceptions.RateFetchException;
import com.pawemie.exceptions.ValidationException;
import com.pawemie.model.Computer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static com.pawemie.utils.ComputerUtils.*;

public class AppService {

    private final DatabaseService databaseService;
    private final NbpService nbpService;
    private final XmlService xmlService;

    public AppService(DatabaseService databaseService, NbpService nbpService, XmlService xmlService) {
        this.databaseService = databaseService;
        this.nbpService = nbpService;
        this.xmlService = xmlService;
    }

    public void save(String name, LocalDate date, String priceUSD) throws ValidationException, RateFetchException {
        validateName(name);
        BigDecimal priceUSDValue = parseAndValidatePrice(priceUSD);
        LocalDate dateAdjusted = adjustToBusinessDay(date);

        BigDecimal exchangeRate = nbpService.getRate(dateAdjusted);
        BigDecimal pricePLN = priceUSDValue.multiply(exchangeRate)
                .setScale(2, RoundingMode.HALF_UP);

        Computer computer = new Computer(name, date, priceUSDValue, pricePLN);

        xmlService.save(computer);
        databaseService.save(computer);
    }

    public List<Computer> findAll() {
        return databaseService.findAll();
    }
}
