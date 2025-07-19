package com.mycompany.peminjamanbarang.service;

import com.mycompany.peminjamanbarang.model.Barang;
import com.mycompany.peminjamanbarang.dao.ImprovedBarangDAO;
import com.mycompany.peminjamanbarang.dao.GenericDAO;
import java.util.List;

/**
 * BarangService dengan Inheritance dari BaseService
 * Implementasi Polymorphism dan Encapsulation
 */
public class BarangService extends BaseService<Barang, Integer> {
    
    // Encapsulation - private DAO instance
    private final GenericDAO<Barang, Integer> barangDAO;
    
    // Constructor - Dependency Injection
    public BarangService() {
        this.barangDAO = new ImprovedBarangDAO(); // Polymorphism - interface reference
    }
    
    // Constructor dengan custom DAO (untuk testing/flexibility)
    public BarangService(GenericDAO<Barang, Integer> barangDAO) {
        this.barangDAO = barangDAO;
    }
    
    // Implementation dari abstract method - Polymorphism
    @Override
    protected void validateBeforeSave(Barang barang) {
        // Business logic validation khusus untuk save
        if (isKodeExists(barang.getKode())) {
            throw new IllegalArgumentException("Kode barang '" + barang.getKode() + "' sudah ada");
        }
    }
    
    // Implementation dari abstract method - Polymorphism
    @Override
    protected void validateBeforeUpdate(Barang barang) {
        // Business logic validation khusus untuk update
        if (!barangDAO.exists(barang.getId())) {
            throw new IllegalArgumentException("Barang dengan ID " + barang.getId() + " tidak ditemukan");
        }
        
        // Check jika kode sudah digunakan oleh barang lain
        List<Barang> allBarang = barangDAO.findAll();
        for (Barang b : allBarang) {
            if (b.getId() != barang.getId() && b.getKode().equals(barang.getKode())) {
                throw new IllegalArgumentException("Kode barang '" + barang.getKode() + "' sudah digunakan oleh barang lain");
            }
        }
    }
    
    // Implementation dari abstract method - Polymorphism
    @Override
    protected void performBusinessLogicAfterSave(Barang barang) {
        // Log atau audit setelah save
        System.out.println("Barang baru telah disimpan: " + barang.getDisplayInfo());
    }
    
    // Implementation dari abstract method - Polymorphism
    @Override
    protected void performBusinessLogicAfterUpdate(Barang barang) {
        // Log atau audit setelah update
        System.out.println("Barang telah diupdate: " + barang.getDisplayInfo());
    }
    
    // Implementation dari abstract method - Polymorphism
    @Override
    protected void doSave(Barang barang) {
        barangDAO.save(barang);
    }
    
    // Implementation dari abstract method - Polymorphism
    @Override
    protected void doUpdate(Barang barang) {
        barangDAO.update(barang);
    }
    
    // Implementation dari abstract method - Polymorphism
    @Override
    protected void doDelete(Integer id) {
        barangDAO.delete(id);
    }
    
    // Implementation dari abstract method - Polymorphism
    @Override
    protected Barang doFindById(Integer id) {
        return barangDAO.findById(id);
    }
    
    // Implementation dari abstract method - Polymorphism
    @Override
    protected List<Barang> doFindAll() {
        return barangDAO.findAll();
    }
    
    // Business method khusus untuk Barang
    public List<Barang> findBarangByKondisi(String kondisi) {
        if (barangDAO instanceof ImprovedBarangDAO) {
            return ((ImprovedBarangDAO) barangDAO).findByKondisi(kondisi);
        }
        
        // Fallback jika DAO tidak support method khusus
        return barangDAO.findAll().stream()
                .filter(b -> kondisi.equalsIgnoreCase(b.getKondisi()))
                .toList();
    }
    
    public List<Barang> findAvailableBarang() {
        return findBarangByKondisi("Baik");
    }
    
    // Helper method untuk check kode exists
    private boolean isKodeExists(String kode) {
        return barangDAO.findAll().stream()
                .anyMatch(b -> kode.equals(b.getKode()));
    }
    
    // Public method untuk get count
    public long getTotalBarang() {
        return barangDAO.findAll().size();
    }
    
    // Method untuk validate barang bisa dipinjam
    public boolean canBeBorrowed(Barang barang) {
        return barang != null && barang.isValid() && barang.isGoodCondition();
    }
}
