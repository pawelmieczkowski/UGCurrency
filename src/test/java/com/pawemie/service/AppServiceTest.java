package com.pawemie.service;

import com.pawemie.exceptions.RateFetchException;
import com.pawemie.exceptions.ValidationException;
import com.pawemie.model.Computer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AppServiceTest {

    private DatabaseService databaseService;
    private NbpService nbpService;
    private XmlService xmlService;
    private AppService appService;

    @BeforeEach
    void setUp() {
        databaseService = mock(DatabaseService.class);
        nbpService = mock(NbpService.class);
        xmlService = mock(XmlService.class);
        appService = new AppService(databaseService, nbpService, xmlService);
    }

    @Test
    void shouldSaveComputerSuccessfully() throws ValidationException, RateFetchException {
        LocalDate date = LocalDate.of(2026, 3, 22);
        BigDecimal exchangeRate = BigDecimal.valueOf(4.5);
        when(nbpService.getRate(date)).thenReturn(exchangeRate);

        appService.save("MyComputer", date, "1000");

        ArgumentCaptor<Computer> captor = ArgumentCaptor.forClass(Computer.class);
        verify(xmlService).save(captor.capture());
        verify(databaseService).save(captor.capture());

        Computer savedComputer = captor.getValue();
        assertEquals("MyComputer", savedComputer.getName());
        assertEquals(date, savedComputer.getDateOfBooking());
        assertEquals(BigDecimal.valueOf(1000), savedComputer.getCostUSD());
        assertEquals(BigDecimal.valueOf(4500.00).setScale(2, RoundingMode.HALF_UP), savedComputer.getCostPLN());
    }

    @Test
    void shouldThrowValidationExceptionForInvalidName() {
        LocalDate date = LocalDate.now();

        assertThrows(ValidationException.class,
                () -> appService.save("", date, "1000"));
    }
}