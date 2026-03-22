package com.pawemie.service;

import com.pawemie.model.Computer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseServiceTest {

    private Connection conn;
    private DatabaseService service;

    @BeforeEach
    void setup() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:test");

        conn.createStatement().execute("""
                    CREATE TABLE komputer(
                    nazwa VARCHAR(255),
                    data_ksiegowania DATE,
                    koszt_usd DECIMAL(13,2),
                    koszt_pln DECIMAL(13,2)
                    )
                """);

        service = new DatabaseService(conn);
    }

    @AfterEach
    void cleanup() throws Exception {
        conn.close();
    }

    @Test
    void shouldSaveAndRetrieveComputer() {
        Computer computer = new Computer("PC-1", LocalDate.of(2024, 1, 1),
                new BigDecimal("1000.00"), new BigDecimal("4000.00")
        );

        service.save(computer);

        List<Computer> result = service.findAll();

        assertEquals(1, result.size());

        Computer saved = result.get(0);

        assertEquals("PC-1", saved.getName());
        assertEquals(LocalDate.of(2024, 1, 1), saved.getDateOfBooking());
        assertEquals(new BigDecimal("1000.00"), saved.getCostUSD());
        assertEquals(new BigDecimal("4000.00"), saved.getCostPLN());
    }

    @Test
    void shouldReturnMultipleComputers() {
        Computer computer1 = new Computer("PC-1", LocalDate.now(), new BigDecimal("100"), new BigDecimal("400"));
        Computer computer2 = new Computer("PC-2", LocalDate.now(), new BigDecimal("200"), new BigDecimal("800"));
        service.save(computer1);
        service.save(computer2);

        List<Computer> result = service.findAll();

        assertEquals(2, result.size());
    }


    @Test
    void shouldReturnEmptyListWhenNoData() {
        List<Computer> result = service.findAll();

        assertTrue(result.isEmpty());
    }


}