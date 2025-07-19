package com.mycompany.peminjamanbarang.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class PermintaanModel dengan implementasi Inheritance dan Encapsulation
 */
public class PermintaanModel extends BaseEntity {
    // Encapsulation - private fields
    private String namaPengaju;
    private String namaBarang;
    private String kodeBarang;
    private String status;
    private LocalDateTime tanggalPengajuan;

    // Constructor dengan inheritance
    public PermintaanModel(int id, String namaPengaju, String namaBarang, String kodeBarang, String status) {
        super(id); // Inheritance - memanggil constructor parent
        this.namaPengaju = namaPengaju;
        this.namaBarang = namaBarang;
        this.kodeBarang = kodeBarang;
        this.status = status;
    }

    public PermintaanModel(int id, String namaPengaju, String namaBarang, String kodeBarang, String status, LocalDateTime tanggalPengajuan) {
        super(id); // Inheritance - memanggil constructor parent
        this.namaPengaju = namaPengaju;
        this.namaBarang = namaBarang;
        this.kodeBarang = kodeBarang;
        this.status = status;
        this.tanggalPengajuan = tanggalPengajuan;
    }

    // Encapsulation - Getter methods
    public String getNamaPengaju() { return namaPengaju; }
    public String getNamaBarang() { return namaBarang; }
    public String getKodeBarang() { return kodeBarang; }
    public String getStatus() { return status; }
    public LocalDateTime getTanggalPengajuan() { return tanggalPengajuan; }

    // Encapsulation - Setter methods dengan validasi
    public void setNamaPengaju(String namaPengaju) {
        if (namaPengaju != null && !namaPengaju.trim().isEmpty()) {
            this.namaPengaju = namaPengaju;
        }
    }

    public void setStatus(String status) {
        if (isValidStatus(status)) {
            this.status = status;
        }
    }

    public void setTanggalPengajuan(LocalDateTime tanggalPengajuan) {
        this.tanggalPengajuan = tanggalPengajuan;
    }

    // Implementation dari abstract method - Polymorphism
    @Override
    public boolean isValid() {
        return namaPengaju != null && !namaPengaju.trim().isEmpty() &&
               namaBarang != null && !namaBarang.trim().isEmpty() &&
               kodeBarang != null && !kodeBarang.trim().isEmpty() &&
               isValidStatus(status);
    }

    // Implementation dari abstract method - Polymorphism
    @Override
    public String getDisplayInfo() {
        return String.format("Permintaan[ID: %d, Pengaju: %s, Barang: %s (%s), Status: %s]", 
                           getId(), namaPengaju, namaBarang, kodeBarang, status);
    }

    // Method tambahan khusus untuk PermintaanModel
    public String getTanggalPengajuanFormatted() {
        if (tanggalPengajuan != null) {
            return tanggalPengajuan.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        return "";
    }

    public boolean isPending() {
        return "Menunggu".equalsIgnoreCase(status);
    }

    public boolean isApproved() {
        return "Diacc".equalsIgnoreCase(status);
    }

    public boolean isRejected() {
        return "Ditolak".equalsIgnoreCase(status);
    }

    // Private helper method untuk validasi status
    private boolean isValidStatus(String status) {
        return status != null && 
               ("Menunggu".equalsIgnoreCase(status) || 
                "Diacc".equalsIgnoreCase(status) || 
                "Ditolak".equalsIgnoreCase(status));
    }

    // Override toString - Polymorphism
    @Override
    public String toString() {
        return getDisplayInfo();
    }
}