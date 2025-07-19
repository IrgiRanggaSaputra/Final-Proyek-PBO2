package com.mycompany.peminjamanbarang.model;

/**
 * Class Barang dengan implementasi Inheritance dari BaseEntity
 * dan Encapsulation untuk data hiding
 */
public class Barang extends BaseEntity {
    // Encapsulation - private fields
    private String nama;
    private String kode;
    private String kondisi;

    // Constructor - memanfaatkan super() untuk inheritance
    public Barang(int id, String nama, String kode, String kondisi) {
        super(id); // Inheritance - memanggil constructor parent
        this.nama = nama;
        this.kode = kode;
        this.kondisi = kondisi;
    }

    public Barang(String nama, String kode, String kondisi) {
        super(); // Inheritance - memanggil constructor parent
        this.nama = nama;
        this.kode = kode;
        this.kondisi = kondisi;
    }

    // Encapsulation - Getter methods
    public String getNama() { return nama; }
    public String getKode() { return kode; }
    public String getKondisi() { return kondisi; }

    // Encapsulation - Setter methods dengan validasi
    public void setNama(String nama) { 
        if (nama != null && !nama.trim().isEmpty()) {
            this.nama = nama;
        }
    }
    
    public void setKode(String kode) { 
        if (kode != null && !kode.trim().isEmpty()) {
            this.kode = kode;
        }
    }
    
    public void setKondisi(String kondisi) { 
        if (kondisi != null && !kondisi.trim().isEmpty()) {
            this.kondisi = kondisi;
        }
    }

    // Implementation dari abstract method di BaseEntity - Polymorphism
    @Override
    public boolean isValid() {
        return nama != null && !nama.trim().isEmpty() &&
               kode != null && !kode.trim().isEmpty() &&
               kondisi != null && !kondisi.trim().isEmpty();
    }

    // Implementation dari abstract method di BaseEntity - Polymorphism
    @Override
    public String getDisplayInfo() {
        return String.format("Barang[ID: %d, Nama: %s, Kode: %s, Kondisi: %s]", 
                           getId(), nama, kode, kondisi);
    }

    // Method tambahan khusus untuk Barang
    public boolean isGoodCondition() {
        return "Baik".equalsIgnoreCase(kondisi) || "Good".equalsIgnoreCase(kondisi);
    }

    // Override toString untuk debugging - Polymorphism
    @Override
    public String toString() {
        return getDisplayInfo();
    }
}
