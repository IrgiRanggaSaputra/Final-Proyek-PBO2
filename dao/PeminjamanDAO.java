/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.peminjamanbarang.dao;

import com.mycompany.peminjamanbarang.model.Peminjaman;
import com.mycompany.peminjamanbarang.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PeminjamanDAO dengan implementasi OOP yang lengkap
 * Polymorphism: implements GenericDAO<Peminjaman, Integer>
 * Encapsulation: private methods untuk helper functions
 * 
 * @author dzikr
 */
public class PeminjamanDAO implements GenericDAO<Peminjaman, Integer> {

    // Implementation dari GenericDAO - Polymorphism
    @Override
    public void save(Peminjaman peminjaman) {
        String sql = "INSERT INTO peminjaman (user_id, barang_id, nama_peminjam, nama_barang, " +
                    "kode_barang, tanggal_pinjam, status, catatan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            setPeminjamanParameters(ps, peminjaman);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error saving peminjaman: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Peminjaman peminjaman) {
        if (!peminjaman.hasValidId()) {
            throw new RuntimeException("Cannot update peminjaman: Invalid ID");
        }
        
        String sql = "UPDATE peminjaman SET tanggal_kembali = ?, status = ?, catatan = ? WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Set tanggal kembali
            if (peminjaman.getTanggalKembali() != null) {
                ps.setTimestamp(1, Timestamp.valueOf(peminjaman.getTanggalKembali()));
            } else {
                ps.setNull(1, Types.TIMESTAMP);
            }
            
            ps.setString(2, peminjaman.getStatus());
            ps.setString(3, peminjaman.getCatatan());
            ps.setInt(4, peminjaman.getId());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No peminjaman found with ID: " + peminjaman.getId());
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating peminjaman: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        if (id <= 0) {
            throw new RuntimeException("Cannot delete peminjaman: Invalid ID");
        }
        
        String sql = "DELETE FROM peminjaman WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("No peminjaman found with ID: " + id);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting peminjaman: " + e.getMessage(), e);
        }
    }

    @Override
    public Peminjaman findById(Integer id) {
        if (id <= 0) {
            throw new RuntimeException("Cannot find peminjaman: Invalid ID");
        }
        
        String sql = "SELECT * FROM peminjaman WHERE id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return createPeminjamanFromResultSet(rs);
            }
            
            return null;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding peminjaman by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Peminjaman> findAll() {
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT * FROM peminjaman ORDER BY tanggal_pinjam DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(createPeminjamanFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all peminjaman: " + e.getMessage(), e);
        }
        
        return list;
    }

    @Override
    public boolean exists(Integer id) {
        return findById(id) != null;
    }

    // Method khusus untuk PeminjamanDAO
    public List<Peminjaman> findByUserId(int userId) {
        if (userId <= 0) {
            throw new RuntimeException("Cannot find peminjaman: Invalid user ID");
        }
        
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT * FROM peminjaman WHERE user_id = ? ORDER BY tanggal_pinjam DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(createPeminjamanFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding peminjaman by user ID: " + e.getMessage(), e);
        }
        
        return list;
    }

    public List<Peminjaman> findByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new RuntimeException("Cannot find peminjaman: Invalid status");
        }
        
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT * FROM peminjaman WHERE status = ? ORDER BY tanggal_pinjam DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(createPeminjamanFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding peminjaman by status: " + e.getMessage(), e);
        }
        
        return list;
    }

    public List<Peminjaman> findActiveByUserId(int userId) {
        if (userId <= 0) {
            throw new RuntimeException("Cannot find active peminjaman: Invalid user ID");
        }
        
        List<Peminjaman> list = new ArrayList<>();
        String sql = "SELECT * FROM peminjaman WHERE user_id = ? AND status = 'Dipinjam' ORDER BY tanggal_pinjam DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(createPeminjamanFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding active peminjaman: " + e.getMessage(), e);
        }
        
        return list;
    }

    // Method untuk membuat peminjaman dari pengajuan yang disetujui
    public void createFromApprovedRequest(int userId, int barangId, String namaPeminjam, 
                                        String namaBarang, String kodeBarang) {
        Peminjaman peminjaman = new Peminjaman(
            0, // ID akan di-generate oleh database
            userId,
            barangId,
            namaPeminjam,
            namaBarang,
            kodeBarang,
            java.time.LocalDateTime.now()
        );
        
        save(peminjaman);
    }

    // Private helper methods - Encapsulation
    private void setPeminjamanParameters(PreparedStatement ps, Peminjaman peminjaman) throws SQLException {
        ps.setInt(1, peminjaman.getUserId());
        ps.setInt(2, peminjaman.getBarangId());
        ps.setString(3, peminjaman.getNamaPeminjam());
        ps.setString(4, peminjaman.getNamaBarang());
        ps.setString(5, peminjaman.getKodeBarang());
        ps.setTimestamp(6, Timestamp.valueOf(peminjaman.getTanggalPinjam()));
        ps.setString(7, peminjaman.getStatus());
        ps.setString(8, peminjaman.getCatatan());
    }

    private Peminjaman createPeminjamanFromResultSet(ResultSet rs) throws SQLException {
        return new Peminjaman(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getInt("barang_id"),
            rs.getString("nama_peminjam"),
            rs.getString("nama_barang"),
            rs.getString("kode_barang"),
            rs.getTimestamp("tanggal_pinjam") != null ? 
                rs.getTimestamp("tanggal_pinjam").toLocalDateTime() : null,
            rs.getTimestamp("tanggal_kembali") != null ? 
                rs.getTimestamp("tanggal_kembali").toLocalDateTime() : null,
            rs.getString("status"),
            rs.getString("catatan")
        );
    }
}
