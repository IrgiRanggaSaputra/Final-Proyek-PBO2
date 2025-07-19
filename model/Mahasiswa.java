package com.mycompany.peminjamanbarang.model;

/**
 * Class Mahasiswa yang extends User - Inheritance
 * Implementasi Polymorphism dengan method overriding
 */
public class Mahasiswa extends User {
    
    // Encapsulation - private fields khusus mahasiswa
    private String nim;
    private String jurusan;
    
    // Constructor dengan inheritance
    public Mahasiswa(String username, String password) {
        super(username, password);
        setRole("mahasiswa"); // Set role otomatis
    }
    
    public Mahasiswa(int id, String nama, String username, String password, String nim, String jurusan) {
        super(id, nama, username, password, "mahasiswa");
        this.nim = nim;
        this.jurusan = jurusan;
    }
    
    // Encapsulation - Getter/Setter
    public String getNim() { return nim; }
    public String getJurusan() { return jurusan; }
    
    public void setNim(String nim) {
        if (nim != null && !nim.trim().isEmpty()) {
            this.nim = nim;
        }
    }
    
    public void setJurusan(String jurusan) {
        if (jurusan != null && !jurusan.trim().isEmpty()) {
            this.jurusan = jurusan;
        }
    }
    
    // Method overriding - Polymorphism
    @Override
    public String getDisplayInfo() {
        return String.format("Mahasiswa[ID: %d, Nama: %s, NIM: %s, Username: %s, Jurusan: %s]", 
                           getId(), getNama(), nim, getUsername(), jurusan);
    }
    
    // Method overriding untuk validasi - Polymorphism
    @Override
    public boolean isValid() {
        return super.isValid() && nim != null && !nim.trim().isEmpty();
    }
    
    // Method khusus untuk Mahasiswa
    public boolean canBorrowBarang() {
        return true; // Mahasiswa bisa meminjam barang
    }
    
    public boolean canViewOwnPermintaan() {
        return true; // Mahasiswa bisa lihat permintaan sendiri
    }
    
    public boolean canSubmitPermintaan() {
        return isValid(); // Hanya bisa submit jika data valid
    }
    
    // Method untuk permissions
    public String[] getPermissions() {
        return new String[]{
            "VIEW_BARANG",
            "SUBMIT_PERMINTAAN", 
            "VIEW_OWN_PERMINTAAN",
            "VIEW_OWN_RIWAYAT"
        };
    }
    
    // Method untuk generate full identifier
    public String getFullIdentifier() {
        return String.format("%s (%s)", getNama(), nim);
    }
}
