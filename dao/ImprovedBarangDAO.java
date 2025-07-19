package com.mycompany.peminjamanbarang.dao;

import com.mycompany.peminjamanbarang.model.Barang;
import com.mycompany.peminjamanbarang.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementasi DAO dengan Interface - Polymorphism
 * Class ini implements GenericDAO interface
 */
public class ImprovedBarangDAO implements GenericDAO<Barang, Integer> {
    
    // Polymorphism - Implementation method dari interface
    @Override
    public void save(Barang barang) {
        // Validasi menggunakan method dari BaseEntity
        if (!barang.isValid()) {
            throw new IllegalArgumentException("Data barang tidak valid");
        }
        
        String query = "INSERT INTO barang (nama, kode, kondisi) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, barang.getNama());
            stmt.setString(2, barang.getKode());
            stmt.setString(3, barang.getKondisi());
            stmt.executeUpdate();
            
            // Set ID yang di-generate database
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                barang.setId(rs.getInt(1));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error saving barang: " + e.getMessage(), e);
        }
    }
    
    // Polymorphism - Implementation method dari interface
    @Override
    public void update(Barang barang) {
        if (!barang.hasValidId()) {
            throw new IllegalArgumentException("ID harus valid untuk update");
        }
        
        if (!barang.isValid()) {
            throw new IllegalArgumentException("Data barang tidak valid");
        }
        
        String query = "UPDATE barang SET nama=?, kode=?, kondisi=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, barang.getNama());
            stmt.setString(2, barang.getKode());
            stmt.setString(3, barang.getKondisi());
            stmt.setInt(4, barang.getId());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating barang: " + e.getMessage(), e);
        }
    }
    
    // Polymorphism - Implementation method dari interface
    @Override
    public void delete(Integer id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID harus valid untuk delete");
        }
        
        String query = "DELETE FROM barang WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Barang dengan ID " + id + " tidak ditemukan");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting barang: " + e.getMessage(), e);
        }
    }
    
    // Polymorphism - Implementation method dari interface
    @Override
    public Barang findById(Integer id) {
        if (id <= 0) {
            return null;
        }
        
        String query = "SELECT * FROM barang WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBarang(rs);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding barang: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    // Polymorphism - Implementation method dari interface
    @Override
    public List<Barang> findAll() {
        List<Barang> list = new ArrayList<>();
        String query = "SELECT * FROM barang ORDER BY nama";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToBarang(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all barang: " + e.getMessage(), e);
        }
        
        return list;
    }
    
    // Polymorphism - Implementation method dari interface
    @Override
    public boolean exists(Integer id) {
        if (id <= 0) {
            return false;
        }
        
        String query = "SELECT COUNT(*) FROM barang WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error checking barang existence: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    // Method tambahan khusus untuk Barang
    public List<Barang> findByKondisi(String kondisi) {
        List<Barang> list = new ArrayList<>();
        String query = "SELECT * FROM barang WHERE kondisi = ? ORDER BY nama";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, kondisi);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSetToBarang(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding barang by kondisi: " + e.getMessage(), e);
        }
        
        return list;
    }
    
    // Method helper untuk mapping ResultSet ke Barang object
    private Barang mapResultSetToBarang(ResultSet rs) throws SQLException {
        return new Barang(
            rs.getInt("id"),
            rs.getString("nama"),
            rs.getString("kode"),
            rs.getString("kondisi")
        );
    }
    
    // Static factory method untuk backward compatibility
    public static List<Barang> getAllBarang() {
        ImprovedBarangDAO dao = new ImprovedBarangDAO();
        return dao.findAll();
    }
    
    public static void insertBarang(Barang barang) {
        ImprovedBarangDAO dao = new ImprovedBarangDAO();
        dao.save(barang);
    }
    
    public static void updateBarang(Barang barang) {
        ImprovedBarangDAO dao = new ImprovedBarangDAO();
        dao.update(barang);
    }
    
    public static void deleteBarang(int id) {
        ImprovedBarangDAO dao = new ImprovedBarangDAO();
        dao.delete(id);
    }
}
