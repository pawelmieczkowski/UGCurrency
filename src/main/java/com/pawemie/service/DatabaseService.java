package com.pawemie.service;

import com.pawemie.model.Computer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private final Connection conn;

    public DatabaseService() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:h2:./komputeryDB");

            Statement st = conn.createStatement();

            st.execute("""
                CREATE TABLE IF NOT EXISTS komputer(
                nazwa VARCHAR(255),
                data_ksiegowania DATE,
                koszt_usd DECIMAL(13,2),
                koszt_pln DECIMAL(13,2)
                )
                """);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize the database", e);
        }
    }

    public DatabaseService(Connection conn) {
        this.conn = conn;
    }

    public void save(Computer c) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO komputer VALUES(?,?,?,?)");

            ps.setString(1, c.getName());
            ps.setDate(2, Date.valueOf(c.getDateOfBooking()));
            ps.setBigDecimal(3, c.getCostUSD());
            ps.setBigDecimal(4, c.getCostPLN());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Computer> findAll() {
        List<Computer> list = new ArrayList<>();
        try {
            ResultSet rs =
                    conn.createStatement()
                            .executeQuery("SELECT * FROM komputer");
            while (rs.next()) {
                list.add(new Computer(
                        rs.getString(1),
                        rs.getDate(2).toLocalDate(),
                        rs.getBigDecimal(3),
                        rs.getBigDecimal(4)
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
