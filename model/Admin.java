package com.mycompany.peminjamanbarang.model;

/**
 * Class Admin yang extends User - Inheritance
 * Implementasi Polymorphism dengan method overriding
 */
public class Admin extends User {
    
    // Constructor dengan inheritance
    public Admin(String username, String password) {
        super(username, password);
        setRole("admin"); // Set role otomatis
    }
    
    public Admin(int id, String nama, String username, String password) {
        super(id, nama, username, password, "admin");
    }
    
    // Method overriding - Polymorphism
    @Override
    public String getDisplayInfo() {
        return String.format("Admin[ID: %d, Nama: %s, Username: %s]", 
                           getId(), getNama(), getUsername());
    }
    
    // Method khusus untuk Admin
    public boolean canManageBarang() {
        return true; // Admin bisa mengelola barang
    }
    
    public boolean canApprovePermintaan() {
        return true; // Admin bisa approve permintaan
    }
    
    public boolean canViewAllData() {
        return true; // Admin bisa lihat semua data
    }
    
    // Method untuk permissions
    public String[] getPermissions() {
        return new String[]{
            "CREATE_BARANG", 
            "UPDATE_BARANG", 
            "DELETE_BARANG",
            "APPROVE_PERMINTAAN",
            "VIEW_ALL_PERMINTAAN",
            "VIEW_REPORTS"
        };
    }
}
