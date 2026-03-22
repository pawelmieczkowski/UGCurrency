package com.pawemie.service;

import com.pawemie.model.Computer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlServiceTest {

    @TempDir
    Path tempDir;

    private XmlService xmlService;
    private Path testFile;

    @BeforeEach
    void setUp() {
        testFile = tempDir.resolve("faktura-test.xml");
        xmlService = new XmlService(testFile.toString());
    }

    @Test
    void shouldCreateFile() {
        xmlService.save(createComputer());

        assertTrue(Files.exists(testFile));
    }

    @Test
    void shouldWriteData() throws Exception {
        xmlService.save(createComputer());

        String content = Files.readString(testFile);

        assertTrue(content.contains("TestPC"));
        assertTrue(content.contains("2024-01-01"));
    }

    @Test
    void shouldAppendMultipleComputers() throws Exception {
        xmlService.save(createComputer());
        xmlService.save(createComputer());

        String content = Files.readString(testFile);

        int count = content.split("<komputer>").length - 1;
        assertEquals(2, count);
    }

    @Test
    void shouldKeepXmlStructure() throws Exception {
        xmlService.save(createComputer());

        String content = Files.readString(testFile);

        assertTrue(content.startsWith("<faktura>"));
        assertTrue(content.endsWith("</faktura>"));
    }

    private Computer createComputer() {
        Computer computer = new Computer("TestPC", LocalDate.of(2024, 1, 1),
                new BigDecimal("1000.0"), new BigDecimal("4000.0"));
        return computer;
    }

}