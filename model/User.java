package com.mycompany.peminjamanbarang.model;

/**
 * Class User dengan implementasi Inheritance dan Encapsulation
 */
public class User extends BaseEntity {
    // Encapsulation - private fields
    private String nama;
    private String username;
    private String password;
    private String role;

    // Constructor dengan inheritance
    public User(String username, String password) {
        super(); // Inheritance - memanggil constructor parent
        this.username = username;
        this.password = password;
    }

    public User(int id, String nama, String username, String password, String role) {
        super(id); // Inheritance - memanggil constructor parent
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Encapsulation - Getter methods
    public String getNama() { return nama; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Encapsulation - Setter methods dengan validasi
    public void setNama(String nama) {
        if (nama != null && !nama.trim().isEmpty()) {
            this.nama = nama;
        }
    }

    public void setUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.username = username;
        }
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 3) {
            this.password = password;
        }
    }

    public void setRole(String role) {
        if ("admin".equalsIgnoreCase(role) || "mahasiswa".equalsIgnoreCase(role)) {
            this.role = role.toLowerCase();
        }
    }

    // Implementation dari abstract method - Polymorphism
    @Override
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
               password != null && password.length() >= 3 &&
               role != null && (role.equals("admin") || role.equals("mahasiswa"));
    }

    // Implementation dari abstract method - Polymorphism
    @Override
    public String getDisplayInfo() {
        return String.format("User[ID: %d, Nama: %s, Username: %s, Role: %s]", 
                           getId(), nama, username, role);
    }

    // Method tambahan khusus untuk User
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public boolean isMahasiswa() {
        return "mahasiswa".equalsIgnoreCase(role);
    }

    // Override toString - Polymorphism
    @Override
    public String toString() {
        return getDisplayInfo();
    }
}
