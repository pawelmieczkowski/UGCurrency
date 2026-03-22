package com.pawemie.service;

import com.pawemie.model.Computer;

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;

public class XmlService {

    private final String fileName;

    public XmlService(String fileName) {
        this.fileName = fileName;
    }

    public void save(Computer computer) {
        try {
            File file = new File(fileName);

            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("<faktura>\n</faktura>");
                }
            }
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                long length = raf.length();

                raf.setLength(length - "</faktura>".length());
                raf.seek(raf.length());

                raf.writeBytes("""
                        <komputer>
                        <nazwa>%s</nazwa>
                        <data_ksiegowania>%s</data_ksiegowania>
                        <koszt_USD>%f</koszt_USD>
                        <koszt_PLN>%f</koszt_PLN>
                        </komputer>
                        """.formatted(
                        computer.getName(),
                        computer.getDateOfBooking(),
                        computer.getCostUSD(),
                        computer.getCostPLN()
                ));
                raf.writeBytes("</faktura>");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}