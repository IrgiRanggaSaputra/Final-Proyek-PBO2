package com.mycompany.peminjamanbarang.dao;

import java.util.List;

/**
 * Interface untuk operasi CRUD - Implementasi Polymorphism
 * Generic interface yang bisa digunakan untuk berbagai entity
 */
public interface GenericDAO<T, ID> {
    
    // Method untuk menyimpan entity
    void save(T entity);
    
    // Method untuk mengupdate entity
    void update(T entity);
    
    // Method untuk menghapus entity berdasarkan ID
    void delete(ID id);
    
    // Method untuk mencari entity berdasarkan ID
    T findById(ID id);
    
    // Method untuk mengambil semua entity
    List<T> findAll();
    
    // Method untuk mengecek apakah entity dengan ID tertentu ada
    boolean exists(ID id);
}
