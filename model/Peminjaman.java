/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.peminjamanbarang.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class Peminjaman dengan implementasi OOP yang lengkap
 * Inheritance: extends BaseEntity
 * Encapsulation: private fields dengan getter/setter
 * Polymorphism: override abstract methods dari BaseEntity
 * 
 * @author dzikr
 */
public class Peminjaman extends BaseEntity {
    // Encapsulation - private fields
    private int userId;
    private int barangId;
    private String namaPeminjam;
    private String namaBarang;
    private String kodeBarang;
    private LocalDateTime tanggalPinjam;
    private LocalDateTime tanggalKembali;
    private String status; // "Dipinjam", "Dikembalikan", "Terlambat"
    private String catatan;

    // Constructor untuk peminjaman baru
    public Peminjaman(int id, int userId, int barangId, String namaPeminjam, 
                     String namaBarang, String kodeBarang, LocalDateTime tanggalPinjam) {
        super(id); // Inheritance - memanggil constructor parent
        this.userId = userId;
        this.barangId = barangId;
        this.namaPeminjam = namaPeminjam;
        this.namaBarang = namaBarang;
        this.kodeBarang = kodeBarang;
        this.tanggalPinjam = tanggalPinjam;
        this.status = "Dipinjam";
        this.catatan = "";
    }

    // Constructor lengkap untuk data dari database
    public Peminjaman(int id, int userId, int barangId, String namaPeminjam, 
                     String namaBarang, String kodeBarang, LocalDateTime tanggalPinjam,
                     LocalDateTime tanggalKembali, String status, String catatan) {
        super(id); // Inheritance - memanggil constructor parent
        this.userId = userId;
        this.barangId = barangId;
        this.namaPeminjam = namaPeminjam;
        this.namaBarang = namaBarang;
        this.kodeBarang = kodeBarang;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalKembali = tanggalKembali;
        this.status = status != null ? status : "Dipinjam";
        this.catatan = catatan != null ? catatan : "";
    }

    // Encapsulation - Getter methods
    public int getUserId() { return userId; }
    public int getBarangId() { return barangId; }
    public String getNamaPeminjam() { return namaPeminjam; }
    public String getNamaBarang() { return namaBarang; }
    public String getKodeBarang() { return kodeBarang; }
    public LocalDateTime getTanggalPinjam() { return tanggalPinjam; }
    public LocalDateTime getTanggalKembali() { return tanggalKembali; }
    public String getStatus() { return status; }
    public String getCatatan() { return catatan; }

    // Encapsulation - Setter methods dengan validasi
    public void setTanggalKembali(LocalDateTime tanggalKembali) {
        this.tanggalKembali = tanggalKembali;
    }

    public void setStatus(String status) {
        if (isValidStatus(status)) {
            this.status = status;
        }
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan != null ? catatan : "";
    }

    // Implementation dari abstract method - Polymorphism
    @Override
    public boolean isValid() {
        return userId > 0 && barangId > 0 &&
               namaPeminjam != null && !namaPeminjam.trim().isEmpty() &&
               namaBarang != null && !namaBarang.trim().isEmpty() &&
               kodeBarang != null && !kodeBarang.trim().isEmpty() &&
               tanggalPinjam != null &&
               isValidStatus(status);
    }

    // Implementation dari abstract method - Polymorphism
    @Override
    public String getDisplayInfo() {
        return String.format("Peminjaman[ID: %d, Peminjam: %s, Barang: %s (%s), Status: %s]", 
                           getId(), namaPeminjam, namaBarang, kodeBarang, status);
    }

    // Method khusus untuk Peminjaman
    public String getTanggalPinjamFormatted() {
        if (tanggalPinjam != null) {
            return tanggalPinjam.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        return "";
    }

    public String getTanggalKembaliFormatted() {
        if (tanggalKembali != null) {
            return tanggalKembali.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        return "-";
    }

    // Method untuk mengecek status peminjaman
    public boolean isDipinjam() {
        return "Dipinjam".equalsIgnoreCase(status);
    }

    public boolean isDikembalikan() {
        return "Dikembalikan".equalsIgnoreCase(status);
    }

    public boolean isTerlambat() {
        return "Terlambat".equalsIgnoreCase(status);
    }

    // Method untuk business logic
    public void kembalikan(String catatan) {
        this.tanggalKembali = LocalDateTime.now();
        this.status = "Dikembalikan";
        this.catatan = catatan != null ? catatan : "";
    }

    public void markAsTerlambat() {
        this.status = "Terlambat";
    }

    // Helper method untuk validasi status
    private boolean isValidStatus(String status) {
        return status != null && 
               ("Dipinjam".equalsIgnoreCase(status) || 
                "Dikembalikan".equalsIgnoreCase(status) || 
                "Terlambat".equalsIgnoreCase(status));
    }

    // Method untuk menghitung durasi peminjaman
    public long getDurasiPeminjamanHari() {
        if (tanggalPinjam == null) return 0;
        
        LocalDateTime akhir = tanggalKembali != null ? tanggalKembali : LocalDateTime.now();
        return java.time.Duration.between(tanggalPinjam, akhir).toDays();
    }

    // Override toString - Polymorphism
    @Override
    public String toString() {
        return getDisplayInfo();
    }
}
