package com.mycompany.peminjamanbarang.dao;

import com.mycompany.peminjamanbarang.model.PermintaanModel;
import com.mycompany.peminjamanbarang.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermintaanDAO {
    public static List<PermintaanModel> getPermintaanMenunggu() {
        List<PermintaanModel> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT p.id, u.nama AS nama_pengaju, b.nama AS nama_barang, b.kode AS kode_barang, p.status " +
                "FROM pengajuan p " +
                "JOIN users u ON p.user_id = u.id " +
                "JOIN barang b ON p.barang_id = b.id " +
                "WHERE p.status = 'Menunggu'")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PermintaanModel p = new PermintaanModel(
                    rs.getInt("id"),
                    rs.getString("nama_pengaju"),
                    rs.getString("nama_barang"),
                    rs.getString("kode_barang"),
                    rs.getString("status")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<PermintaanModel> getPermintaanRiwayat() {
        List<PermintaanModel> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT p.id, u.nama AS nama_pengaju, b.nama AS nama_barang, b.kode AS kode_barang, p.status " +
                "FROM pengajuan p " +
                "JOIN users u ON p.user_id = u.id " +
                "JOIN barang b ON p.barang_id = b.id " +
                "WHERE p.status IN ('Diacc', 'Ditolak')")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PermintaanModel p = new PermintaanModel(
                    rs.getInt("id"),
                    rs.getString("nama_pengaju"),
                    rs.getString("nama_barang"),
                    rs.getString("kode_barang"),
                    rs.getString("status")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateStatus(int id, String status) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE pengajuan SET status = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method untuk mengambil pengajuan berdasarkan user_id yang sedang login
    public static List<PermintaanModel> getPermintaanByUserId(int userId, String status) {
        List<PermintaanModel> list = new ArrayList<>();
        String query = "SELECT p.id, u.nama AS nama_pengaju, b.nama AS nama_barang, b.kode AS kode_barang, p.status, p.tanggal_pengajuan " +
                      "FROM pengajuan p " +
                      "JOIN users u ON p.user_id = u.id " +
                      "JOIN barang b ON p.barang_id = b.id " +
                      "WHERE p.user_id = ?";
        
        if (status != null && !status.isEmpty()) {
            if (status.equals("Menunggu")) {
                query += " AND p.status = 'Menunggu'";
            } else if (status.equals("Riwayat")) {
                query += " AND p.status IN ('Diacc', 'Ditolak')";
            }
        }
        
        query += " ORDER BY p.tanggal_pengajuan DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PermintaanModel p = new PermintaanModel(
                    rs.getInt("id"),
                    rs.getString("nama_pengaju"),
                    rs.getString("nama_barang"),
                    rs.getString("kode_barang"),
                    rs.getString("status"),
                    rs.getTimestamp("tanggal_pengajuan") != null ? 
                        rs.getTimestamp("tanggal_pengajuan").toLocalDateTime() : null
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
