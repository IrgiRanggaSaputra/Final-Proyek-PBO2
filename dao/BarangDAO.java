package com.mycompany.peminjamanbarang.dao;

import com.mycompany.peminjamanbarang.model.Barang;

import java.sql.*;
import java.util.*;

public class BarangDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/peminjaman_db";
    private static final String USER = "root";
    private static final String PASS = "";

    public static List<Barang> getAllBarang() {
        List<Barang> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM barang");
            while (rs.next()) {
                list.add(new Barang(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("kode"),
                    rs.getString("kondisi")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insertBarang(Barang b) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO barang (nama, kode, kondisi) VALUES (?, ?, ?)");
            stmt.setString(1, b.getNama());
            stmt.setString(2, b.getKode());
            stmt.setString(3, b.getKondisi());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBarang(Barang b) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE barang SET nama=?, kode=?, kondisi=? WHERE id=?");
            stmt.setString(1, b.getNama());
            stmt.setString(2, b.getKode());
            stmt.setString(3, b.getKondisi());
            stmt.setInt(4, b.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBarang(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM barang WHERE id=?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
