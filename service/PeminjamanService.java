/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.peminjamanbarang.service;

import com.mycompany.peminjamanbarang.model.Peminjaman;
import com.mycompany.peminjamanbarang.dao.PeminjamanDAO;
import com.mycompany.peminjamanbarang.dao.GenericDAO;
import java.sql.SQLException;
import java.util.List;

/**
 * PeminjamanService dengan implementasi OOP yang lengkap
 * Inheritance: extends BaseService<Peminjaman, Integer>
 * Polymorphism: override template methods dari BaseService
 * Encapsulation: private methods untuk business logic
 * 
 * @author dzikr
 */
public class PeminjamanService extends BaseService<Peminjaman, Integer> {
    
    private final PeminjamanDAO peminjamanDAO;

    public PeminjamanService() {
        this.peminjamanDAO = new PeminjamanDAO();
    }

    // Implementation dari abstract method - Polymorphism
    @Override
    protected void validateBeforeSave(Peminjaman peminjaman) {
        // Validasi business logic tambahan
        if (peminjaman.getUserId() <= 0) {
            throw new RuntimeException("User ID harus valid untuk membuat peminjaman");
        }
        
        if (peminjaman.getBarangId() <= 0) {
            throw new RuntimeException("Barang ID harus valid untuk membuat peminjaman");
        }
        
        // Cek apakah user sudah meminjam barang yang sama dan belum dikembalikan
        List<Peminjaman> activePeminjaman = peminjamanDAO.findActiveByUserId(peminjaman.getUserId());
        for (Peminjaman active : activePeminjaman) {
            if (active.getBarangId() == peminjaman.getBarangId()) {
                throw new RuntimeException("User sudah meminjam barang ini dan belum mengembalikannya");
            }
        }
    }

    @Override
    protected void validateBeforeUpdate(Peminjaman peminjaman) {
        // Validasi bahwa peminjaman masih aktif saat akan diupdate
        Peminjaman existing = peminjamanDAO.findById(peminjaman.getId());
        if (existing == null) {
            throw new RuntimeException("Peminjaman tidak ditemukan");
        }
        
        // Jika status diubah menjadi "Dikembalikan", pastikan tanggal kembali di-set
        if ("Dikembalikan".equalsIgnoreCase(peminjaman.getStatus()) && 
            peminjaman.getTanggalKembali() == null) {
            throw new RuntimeException("Tanggal kembali harus di-set saat mengembalikan barang");
        }
    }

    @Override
    protected void performBusinessLogicAfterSave(Peminjaman peminjaman) {
        // Logic setelah save, misalnya notifikasi, log, etc.
        System.out.println("Peminjaman berhasil dibuat: " + peminjaman.getDisplayInfo());
    }

    @Override
    protected void performBusinessLogicAfterUpdate(Peminjaman peminjaman) {
        // Logic setelah update, misalnya notifikasi, log, etc.
        System.out.println("Peminjaman berhasil diupdate: " + peminjaman.getDisplayInfo());
    }

    // Implementation dari abstract CRUD methods - Polymorphism
    @Override
    protected void doSave(Peminjaman peminjaman) {
        peminjamanDAO.save(peminjaman);
    }

    @Override
    protected void doUpdate(Peminjaman peminjaman) {
        peminjamanDAO.update(peminjaman);
    }

    @Override
    protected void doDelete(Integer id) {
        peminjamanDAO.delete(id);
    }

    @Override
    protected Peminjaman doFindById(Integer id) {
        return peminjamanDAO.findById(id);
    }

    @Override
    protected List<Peminjaman> doFindAll() {
        return peminjamanDAO.findAll();
    }

    // Business logic methods khusus untuk PeminjamanService
    public List<Peminjaman> getPeminjamanByUserId(int userId) {
        try {
            return peminjamanDAO.findByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error mengambil data peminjaman user: " + e.getMessage(), e);
        }
    }

    public List<Peminjaman> getActivePeminjamanByUserId(int userId) {
        try {
            return peminjamanDAO.findActiveByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error mengambil data peminjaman aktif user: " + e.getMessage(), e);
        }
    }

    public List<Peminjaman> getPeminjamanByStatus(String status) {
        try {
            return peminjamanDAO.findByStatus(status);
        } catch (Exception e) {
            throw new RuntimeException("Error mengambil data peminjaman berdasarkan status: " + e.getMessage(), e);
        }
    }

    // Method untuk proses pengembalian
    public void kembalikanBarang(int peminjamanId, String catatan) {
        try {
            Peminjaman peminjaman = peminjamanDAO.findById(peminjamanId);
            if (peminjaman == null) {
                throw new RuntimeException("Peminjaman tidak ditemukan");
            }

            if (!peminjaman.isDipinjam()) {
                throw new RuntimeException("Barang sudah dikembalikan atau status tidak valid");
            }

            // Update peminjaman dengan data pengembalian
            peminjaman.kembalikan(catatan);
            
            // Simpan perubahan
            update(peminjaman);
            
        } catch (Exception e) {
            throw new RuntimeException("Error mengembalikan barang: " + e.getMessage(), e);
        }
    }

    // Method untuk membuat peminjaman dari pengajuan yang disetujui
    public void createFromApprovedRequest(int userId, int barangId, String namaPeminjam, 
                                        String namaBarang, String kodeBarang) {
        try {
            peminjamanDAO.createFromApprovedRequest(userId, barangId, namaPeminjam, namaBarang, kodeBarang);
        } catch (Exception e) {
            throw new RuntimeException("Error membuat peminjaman dari pengajuan: " + e.getMessage(), e);
        }
    }

    // Method untuk mengecek apakah user bisa meminjam barang tertentu
    // (Dihapus karena duplikat, gunakan versi dengan throws SQLException di bawah)

    // Method untuk mengecek apakah user bisa meminjam barang tertentu
    public boolean canUserBorrowItem(int userId, int barangId) {
        try {
            List<Peminjaman> activePeminjaman = peminjamanDAO.findActiveByUserId(userId);
            
            // Cek apakah user sudah meminjam barang yang sama
            for (Peminjaman peminjaman : activePeminjaman) {
                if (peminjaman.getBarangId() == barangId) {
                    return false; // User sudah meminjam barang ini
                }
            }
            
            // Bisa juga ditambah logic lain, misalnya batas maksimal peminjaman
            // Untuk saat ini, return true jika tidak ada konflik
            return true;
            
        } catch (Exception e) {
            throw new RuntimeException("Error checking user borrowing eligibility: " + e.getMessage(), e);
        }
    }

    // Method untuk mendapatkan statistik peminjaman
    public int getTotalActivePeminjaman() {
        try {
            List<Peminjaman> activePeminjaman = peminjamanDAO.findByStatus("Dipinjam");
            return activePeminjaman.size();
        } catch (Exception e) {
            throw new RuntimeException("Error getting total active peminjaman: " + e.getMessage(), e);
        }
    }

    public int getTotalPeminjamanByUserId(int userId) {
        try {
            List<Peminjaman> userPeminjaman = peminjamanDAO.findByUserId(userId);
            return userPeminjaman.size();
        } catch (Exception e) {
            throw new RuntimeException("Error getting total peminjaman by user: " + e.getMessage(), e);
        }
    }
}
