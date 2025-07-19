package com.mycompany.peminjamanbarang.example;

import com.mycompany.peminjamanbarang.model.*;
import com.mycompany.peminjamanbarang.service.BarangService;
import com.mycompany.peminjamanbarang.dao.GenericDAO;
import com.mycompany.peminjamanbarang.dao.ImprovedBarangDAO;
import java.util.List;

/**
 * Contoh penggunaan semua konsep OOP yang telah diimplementasi
 * Untuk demonstrasi Encapsulation, Inheritance, Polymorphism, dan Abstraction
 */
public class OOPDemonstration {
    
    public static void demonstrateOOPConcepts() {
        System.out.println("=== DEMONSTRASI KONSEP OOP ===\n");
        
        // 1. ENCAPSULATION
        demonstrateEncapsulation();
        
        // 2. INHERITANCE
        demonstrateInheritance();
        
        // 3. POLYMORPHISM
        demonstratePolymorphism();
        
        // 4. ABSTRACTION
        demonstrateAbstraction();
    }
    
    /**
     * Demonstrasi ENCAPSULATION
     * - Private fields dengan getter/setter
     * - Data hiding dan validation
     */
    private static void demonstrateEncapsulation() {
        System.out.println("1. ENCAPSULATION:");
        
        // Membuat object Barang
        Barang barang = new Barang("Laptop", "LPT001", "Baik");
        
        // Encapsulation - akses data melalui getter/setter
        System.out.println("Nama barang: " + barang.getNama()); // getter
        barang.setKondisi("Rusak"); // setter dengan validation
        System.out.println("Kondisi setelah diubah: " + barang.getKondisi());
        
        // Validation di setter (Encapsulation)
        barang.setNama(""); // Tidak akan berubah karena validasi
        System.out.println("Nama tetap: " + barang.getNama() + " (karena validasi)\n");
    }
    
    /**
     * Demonstrasi INHERITANCE
     * - Child class extends parent class
     * - super() dan method overriding
     */
    private static void demonstrateInheritance() {
        System.out.println("2. INHERITANCE:");
        
        // Inheritance - Admin extends User extends BaseEntity
        Admin admin = new Admin("admin1", "password123");
        admin.setNama("Administrator");
        
        // Inheritance - Mahasiswa extends User extends BaseEntity
        Mahasiswa mahasiswa = new Mahasiswa("mhs1", "pass123");
        mahasiswa.setNama("John Doe");
        mahasiswa.setNim("123456789");
        mahasiswa.setJurusan("Informatika");
        
        // Method dari parent class (BaseEntity)
        System.out.println("Admin hasValidId: " + admin.hasValidId());
        System.out.println("Mahasiswa hasValidId: " + mahasiswa.hasValidId());
        
        // Method dari User class
        System.out.println("Admin isAdmin: " + admin.isAdmin());
        System.out.println("Mahasiswa isMahasiswa: " + mahasiswa.isMahasiswa() + "\n");
    }
    
    /**
     * Demonstrasi POLYMORPHISM
     * - Interface implementation
     * - Method overriding
     * - Runtime polymorphism
     */
    private static void demonstratePolymorphism() {
        System.out.println("3. POLYMORPHISM:");
        
        // Polymorphism dengan interface - GenericDAO reference
        GenericDAO<Barang, Integer> dao = new ImprovedBarangDAO();
        
        // Polymorphism - different objects, same interface
        User admin = new Admin("admin", "pass");
        admin.setNama("Admin User");
        
        User mahasiswa = new Mahasiswa("student", "pass");
        mahasiswa.setNama("Student User");
        
        // Method overriding - same method name, different implementation
        System.out.println("Admin display: " + admin.getDisplayInfo());
        System.out.println("Mahasiswa display: " + mahasiswa.getDisplayInfo());
        
        // Runtime polymorphism
        BaseEntity[] entities = {
            new Barang("Mouse", "MS001", "Baik"),
            new Admin("admin2", "pass"),
            new Mahasiswa("student2", "pass")
        };
        
        System.out.println("\nRuntime Polymorphism:");
        for (BaseEntity entity : entities) {
            // Method yang dipanggil tergantung actual type (runtime)
            System.out.println("Entity: " + entity.getDisplayInfo());
            System.out.println("Valid: " + entity.isValid());
        }
        System.out.println();
    }
    
    /**
     * Demonstrasi ABSTRACTION
     * - Abstract class dan methods
     * - Template method pattern
     */
    private static void demonstrateAbstraction() {
        System.out.println("4. ABSTRACTION:");
        
        // Abstraction - menggunakan service layer
        BarangService barangService = new BarangService();
        
        // Template method dari BaseService (Abstraction)
        Barang barang = new Barang("Projector", "PRJ001", "Baik");
        
        try {
            // Template method yang menggunakan abstract methods
            barangService.save(barang);
            System.out.println("Barang berhasil disimpan menggunakan template method");
            
            // Business logic method
            List<Barang> availableBarang = barangService.findAvailableBarang();
            System.out.println("Jumlah barang available: " + availableBarang.size());
            
        } catch (Exception e) {
            System.out.println("Error dalam demonstrasi abstraction: " + e.getMessage());
        }
        
        System.out.println("\nAbstract methods yang diimplementasi:");
        System.out.println("- isValid() dari BaseEntity");
        System.out.println("- getDisplayInfo() dari BaseEntity");
        System.out.println("- validateBeforeSave() dari BaseService");
        System.out.println("- performBusinessLogicAfterSave() dari BaseService");
    }
    
    // Method main untuk testing
    public static void main(String[] args) {
        demonstrateOOPConcepts();
    }
}
