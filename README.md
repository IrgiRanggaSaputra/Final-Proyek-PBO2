# Final Proyek Pemrograman Berorientasi Obyek 2
<ul>
  <li>Mata Kuliah: Pemrograman Berorientasi Obyek 2</li>
  <li>Dosen Pengampu: <a href="https://github.com/Muhammad-Ikhwan-Fathulloh">Muhammad Ikhwan Fathulloh</a></li>
</ul>

## Profil
<ul>
  <li>Nama: Irgi Rangga Saputra</li>
  <li>NIM: 23552011343</li>
  <li>Studi Kasus: Aplikasi Peminjaman Barang</li>
</ul>

## Judul Studi Kasus
<p>Aplikasi Peminjaman Barang (Injemkeun)</p>

## Penjelasan Studi Kasus
<p>Injemkeun adalah aplikasi desktop berbasis JavaFX yang dirancang untuk memudahkan proses peminjaman dan pengelolaan inventaris barang di lingkungan kampus. Aplikasi ini menerapkan konsep Object-Oriented Programming (OOP) secara komprehensif dengan implementasi 4 pilar OOP. Sistem ini memungkinkan mahasiswa untuk mengajukan permintaan peminjaman barang dan mengembalikan barang yang dipinjam, serta memudahkan admin dalam mengelola ketersediaan, persetujuan, dan monitoring seluruh proses peminjaman.</p>

## Fitur Utama
<li><strong>Login Multi-Role:</strong> Sistem autentikasi untuk admin dan mahasiswa dengan dashboard yang berbeda</li>
<li><strong>Manajemen Data Barang:</strong> Admin dapat menambahkan, mengedit, dan menghapus data barang dengan validasi lengkap</li>
<li><strong>Pengajuan Peminjaman:</strong> Mahasiswa dapat mengajukan permintaan peminjaman barang yang tersedia</li>
<li><strong>Persetujuan Admin:</strong> Admin dapat menyetujui atau menolak permintaan peminjaman dengan sistem notifikasi</li>
<li><strong>Sistem Pengembalian:</strong> Mahasiswa dapat mengembalikan barang yang dipinjam dengan catatan kondisi barang</li>
<li><strong>Riwayat Peminjaman:</strong> Tracking lengkap riwayat peminjaman dan pengembalian barang</li>
<li><strong>Pencarian dan Filter:</strong> Fitur pencarian barang berdasarkan nama, kode, dan status ketersediaan</li>
<li><strong>Validasi Data:</strong> Sistem validasi komprehensif untuk memastikan integritas data</li>

## Penjelasan 4 Pilar OOP dalam Studi Kasus

### 1. Inheritance (Pewarisan)
<p><b>Analogi: Pohon Keluarga</b></p>
<li>Anak mewarisi sifat dan kemampuan dari orang tua, tetapi bisa memiliki keunikan sendiri</li>

```java

public abstract class BaseEntity {
    protected int id;
    public abstract boolean isValid();
    public abstract String getDisplayInfo();
}

public abstract class User extends BaseEntity {
    protected String nama, username, password, role;
    public boolean authenticate(String user, String pass) {
        return username.equals(user) && password.equals(pass);
    }
}

public class Admin extends User {
    private String adminLevel;
    
    public Admin(int id, String nama, String username, String password, String adminLevel) {
        super(); 
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.role = "admin";
        this.adminLevel = adminLevel;
    }
    
    @Override
    public boolean isValid() {
        return hasValidId() && nama != null && adminLevel != null;
    }
}

public class Mahasiswa extends User {
    private String nim;
    
    // Mewarisi semua method dari User dan BaseEntity
    // Tapi punya implementasi dan data khusus mahasiswa
}
```
<li><strong>Benefit:</strong> Admin dan Mahasiswa otomatis punya kemampuan login dari User, dan validasi ID dari BaseEntity</li>
<li><strong>Code Reuse:</strong> Method authenticate() cukup ditulis sekali di User, bisa dipakai Admin dan Mahasiswa</li>

### 2. Encapsulation (Enkapsulasi)
<p><b>Analogy: Brankas Bank</b></p>
<li>Data penting disimpan aman di dalam "brankas" (private), hanya bisa diakses dengan "kunci" (method)</li>

```java
public class Barang extends BaseEntity {
    private String nama;     // Data tersembunyi (private)
    private String kode;     // Tidak bisa diakses langsung
    private String kondisi;  // Dilindungi dari perubahan sembarangan
    
    // "Kunci" untuk akses data - dengan validasi
    public void setNama(String nama) {
        if (nama != null && !nama.trim().isEmpty()) {
            this.nama = nama;  // Hanya set jika valid
        } else {
            throw new IllegalArgumentException("Nama barang tidak boleh kosong");
        }
    }
    
    public String getNama() {
        return nama;  // Akses read-only yang aman
    }
    
    // Business logic tersembunyi dalam method
    public boolean isAvailable() {
        return kondisi != null && !kondisi.equals("Rusak") && !kondisi.equals("Dipinjam");
    }
}

public class Peminjaman extends BaseEntity {
    private LocalDateTime tanggalKembali;
    private String status;
    private String catatan;
    
    
    public void kembalikan(String catatan) {
        this.tanggalKembali = LocalDateTime.now();
        this.status = "Dikembalikan";
        this.catatan = catatan != null ? catatan : "";
        
     
    }
}
```
<li><strong>Benefit:</strong> Data aman dari perubahan yang tidak diinginkan, business logic terpusat</li>
<li><strong>Validation:</strong> Setiap perubahan data melalui validasi yang ketat</li>

### 3. Polymorphism (Polimorfisme)
<p><b>Analogi: Seragam yang Sama, Tugas Berbeda</b></p>
<li>Polisi dan Dokter sama-sama pakai seragam, tapi cara kerja dan tugasnya berbeda</li>

```java

public interface GenericDAO<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();
}


public class ImprovedBarangDAO implements GenericDAO<Barang, Integer> {
    @Override
    public void save(Barang barang) {
        // Logic khusus untuk save barang ke database
        String sql = "INSERT INTO barang (nama, kode, kondisi) VALUES (?, ?, ?)";
        // Implementation details...
    }
}

public class PeminjamanDAO implements GenericDAO<Peminjaman, Integer> {
    @Override
    public void save(Peminjaman peminjaman) {
        // Logic khusus untuk save peminjaman ke database  
        String sql = "INSERT INTO peminjaman (user_id, barang_id, status) VALUES (?, ?, ?)";
        // Implementation details...
    }
}

// Polymorphism in action
public class DataManager {
    public void saveAnyEntity(BaseEntity entity, GenericDAO dao) {
        if (entity.isValid()) {           // Polymorphic call - behavior berbeda per entity
            dao.save(entity);             // Polymorphic call - implementation berbeda per DAO
            System.out.println(entity.getDisplayInfo()); // Polymorphic call - output berbeda per entity
        }
    }
}


BaseEntity[] entities = {
    new Barang("Laptop", "LPT001", "Baik"),
    new Peminjaman(1, 101, 201, "John", "Laptop", "LPT001"),
    new Admin(1, "Super Admin", "admin", "password", "Level 1")
};

for (BaseEntity entity : entities) {
    System.out.println(entity.getDisplayInfo()); // Output berbeda untuk setiap tipe
    // Barang[ID: 1, Nama: Laptop, Kode: LPT001, Kondisi: Baik]
    // Peminjaman[ID: 1, Peminjam: John, Barang: Laptop (LPT001), Status: Dipinjam]  
    // Admin[ID: 1, Nama: Super Admin, Role: admin, Level: Level 1]
}
```
<li><strong>Benefit:</strong> Satu method bisa handle berbagai tipe object, code lebih flexible</li>
<li><strong>Extensibility:</strong> Tambah entity baru tanpa ubah existing code</li>

### 4. Abstraction (Abstraksi)
<p><b>Analogi: Remote TV</b></p>
<li>Kita cukup tekan tombol "ON", tidak perlu tahu detail listrik, sinyal, dan komponen dalam TV</li>

```java
// Level 1: Abstract Entity
public abstract class BaseEntity {
    // Contract yang harus dipenuhi semua entity
    public abstract boolean isValid();
    public abstract String getDisplayInfo();
    
    // Implementation detail tersembunyi
    public boolean hasValidId() { return id > 0; }
}

// Level 2: Abstract Service Layer  
public abstract class BaseService<T extends BaseEntity, ID> {
    // Template method - algorithm structure yang sama untuk semua service
    public final void save(T entity) {
        validateBeforeSave(entity);    // Step 1 - details hidden
        doSave(entity);               // Step 2 - details hidden  
        performBusinessLogicAfterSave(entity); // Step 3 - details hidden
    }
    
    
    protected abstract void validateBeforeSave(T entity);
    protected abstract void doSave(T entity);
    protected abstract void performBusinessLogicAfterSave(T entity);
}

// Level 3: Concrete Implementation
public class PeminjamanService extends BaseService<Peminjaman, Integer> {
    // User hanya perlu tahu high-level operations
    public void kembalikanBarang(int peminjamanId, String catatan) {
        // Complexity hidden: database queries, validation, business rules
        Peminjaman peminjaman = dao.findById(peminjamanId);
        peminjaman.kembalikan(catatan);
        update(peminjaman);
    }
    
    public void createFromApprovedRequest(int userId, int barangId, String nama, String namaBarang, String kode) {
        // Complexity hidden: object creation, validation, persistence
        Peminjaman peminjaman = new Peminjaman(userId, barangId, nama, namaBarang, kode);
        save(peminjaman);
    }
}

// Level 4: UI Layer
@FXML
public class MahasiswaDashboardController {
    public void handleKembalikanButton() {
        // User just clicks button - all complexity hidden
        try {
            peminjamanService.kembalikanBarang(selectedId, catatanField.getText());
            showSuccessMessage("Barang berhasil dikembalikan");
            refreshTable();
        } catch (Exception e) {
            showErrorMessage("Gagal mengembalikan barang");
        }
    }
}
```
<li><strong>Benefit:</strong> User tidak perlu tahu detail implementasi, fokus pada business logic</li>
<li><strong>Complexity Management:</strong> Setiap layer menyembunyikan kompleksitas dari layer di atasnya</li>

## Penjelasan Sederhana Setiap Kelas dan Fungsinya

### Kelas Utama
<li><code>App.java</code></li>
<p><b>Fungsi:</b> Main class untuk menjalankan aplikasi JavaFX</p>
<p><b>Tugas:</b> Initialize aplikasi, load FXML, setup primary stage</p>

```java
public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Aplikasi Peminjaman Barang");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Entry point aplikasi
    }
}
```

### Kelas Model (Data/Entity)

#### 1. <code>BaseEntity.java</code>
<p><b>Fungsi:</b> Abstract parent class untuk semua entity</p>
<p><b>Tugas:</b> Menyediakan ID dan contract dasar (isValid, getDisplayInfo)</p>

```java
public abstract class BaseEntity {
    protected int id;
    
    
    public abstract boolean isValid();
    public abstract String getDisplayInfo();
    
    public boolean hasValidId() { return id > 0; }
    
    // Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
```

#### 2. <code>User.java</code>
<p><b>Fungsi:</b> Abstract class untuk semua jenis user</p>
<p><b>Tugas:</b> Menyediakan data dasar user (nama, username, password) dan method authentication</p>

```java
public abstract class User extends BaseEntity {
    protected String nama;
    protected String username;
    protected String password;
    protected String role;
    
   
    public boolean authenticate(String inputUsername, String inputPassword) {
        return username.equals(inputUsername) && password.equals(inputPassword);
    }
    
   
    public abstract String getRoleSpecificInfo();
}
```

#### 3. <code>Admin.java</code>
<p><b>Fungsi:</b> Entity untuk user dengan role admin</p>
<p><b>Tugas:</b> Mengelola barang, approve/reject permintaan, full access ke system</p>

```java
public class Admin extends User {
    private String adminLevel;
    
    public Admin(int id, String nama, String username, String password, String adminLevel) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.role = "admin";
        this.adminLevel = adminLevel;
    }
    
    @Override
    public boolean isValid() {
        return hasValidId() && nama != null && !nama.trim().isEmpty() 
               && username != null && adminLevel != null;
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("Admin[ID: %d, Nama: %s, Role: %s, Level: %s]", 
                           id, nama, role, adminLevel);
    }
    
    @Override
    public String getRoleSpecificInfo() {
        return "Admin Level: " + adminLevel;
    }
}
```

#### 4. <code>Mahasiswa.java</code>
<p><b>Fungsi:</b> Entity untuk user dengan role mahasiswa</p>
<p><b>Tugas:</b> Mengajukan peminjaman, mengembalikan barang, melihat riwayat</p>

```java
public class Mahasiswa extends User {
    private String nim;
    
    public Mahasiswa(int id, String nama, String username, String password, String nim) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.role = "mahasiswa";
        this.nim = nim;
    }
    
    @Override
    public boolean isValid() {
        return hasValidId() && nama != null && !nama.trim().isEmpty() 
               && username != null && nim != null && !nim.trim().isEmpty();
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("Mahasiswa[ID: %d, Nama: %s, NIM: %s, Role: %s]", 
                           id, nama, nim, role);
    }
    
    @Override
    public String getRoleSpecificInfo() {
        return "NIM: " + nim;
    }
}
```

#### 5. <code>Barang.java</code>
<p><b>Fungsi:</b> Entity untuk data barang yang bisa dipinjam</p>
<p><b>Tugas:</b> Menyimpan informasi barang (nama, kode, kondisi) dengan validasi</p>

```java
public class Barang extends BaseEntity {
    private String nama;
    private String kode;
    private String kondisi;
    
    public Barang(String nama, String kode, String kondisi) {
        this.nama = nama;
        this.kode = kode;
        this.kondisi = kondisi;
    }
    
    @Override
    public boolean isValid() {
        return hasValidId() && nama != null && !nama.trim().isEmpty() 
               && kode != null && !kode.trim().isEmpty() && kondisi != null;
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("Barang[ID: %d, Nama: %s, Kode: %s, Kondisi: %s]", 
                           id, nama, kode, kondisi);
    }
    
  
    public boolean isAvailable() {
        return kondisi != null && kondisi.equals("Baik");
    }
    
    // Encapsulated setter with validation
    public void setNama(String nama) {
        if (nama != null && !nama.trim().isEmpty()) {
            this.nama = nama;
        } else {
            throw new IllegalArgumentException("Nama barang tidak boleh kosong");
        }
    }
}
```

#### 6. <code>Peminjaman.java</code>
<p><b>Fungsi:</b> Entity untuk tracking peminjaman barang</p>
<p><b>Tugas:</b> Menyimpan data peminjaman dan mengelola lifecycle (pinjam → kembali)</p>

```java
public class Peminjaman extends BaseEntity {
    private int userId;
    private int barangId;
    private String namaPeminjam;
    private String namaBarang;
    private String kodeBarang;
    private LocalDateTime tanggalPinjam;
    private LocalDateTime tanggalKembali;
    private String status;
    private String catatan;
    
    public Peminjaman(int userId, int barangId, String namaPeminjam, String namaBarang, String kodeBarang) {
        this.userId = userId;
        this.barangId = barangId;
        this.namaPeminjam = namaPeminjam;
        this.namaBarang = namaBarang;
        this.kodeBarang = kodeBarang;
        this.tanggalPinjam = LocalDateTime.now();
        this.status = "Dipinjam";
        this.catatan = "";
    }
    
  
    public void kembalikan(String catatan) {
        this.tanggalKembali = LocalDateTime.now();
        this.status = "Dikembalikan";
        this.catatan = catatan != null ? catatan : "";
    }
    

    public boolean isDipinjam() {
        return "Dipinjam".equals(status);
    }
    
    public long getDurasiPeminjamanHari() {
        if (tanggalKembali != null) {
            return ChronoUnit.DAYS.between(tanggalPinjam, tanggalKembali);
        }
        return ChronoUnit.DAYS.between(tanggalPinjam, LocalDateTime.now());
    }
    
    @Override
    public boolean isValid() {
        return hasValidId() && userId > 0 && barangId > 0 
               && namaPeminjam != null && namaBarang != null && status != null;
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("Peminjaman[ID: %d, Peminjam: %s, Barang: %s (%s), Status: %s]", 
                           id, namaPeminjam, namaBarang, kodeBarang, status);
    }
}
```

#### 7. <code>PermintaanModel.java</code>
<p><b>Fungsi:</b> Entity untuk permintaan peminjaman barang</p>
<p><b>Tugas:</b> Menyimpan data permintaan yang diajukan mahasiswa untuk di-approve admin</p>

```java
public class PermintaanModel extends BaseEntity {
    private String namaPengaju;
    private String namaBarang;
    private String kodeBarang;
    private String status;
    private LocalDateTime tanggalPengajuan;
    
    public PermintaanModel(String namaPengaju, String namaBarang, String kodeBarang) {
        this.namaPengaju = namaPengaju;
        this.namaBarang = namaBarang;
        this.kodeBarang = kodeBarang;
        this.status = "Menunggu";
        this.tanggalPengajuan = LocalDateTime.now();
    }
    

    public boolean isPending() {
        return "Menunggu".equals(status);
    }
    
    public boolean isApproved() {
        return "Diacc".equals(status);
    }
    
    public boolean isRejected() {
        return "Ditolak".equals(status);
    }
    
    @Override
    public boolean isValid() {
        return hasValidId() && namaPengaju != null && !namaPengaju.trim().isEmpty()
               && namaBarang != null && !namaBarang.trim().isEmpty() && status != null;
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("Permintaan[ID: %d, Pengaju: %s, Barang: %s (%s), Status: %s]", 
                           id, namaPengaju, namaBarang, kodeBarang, status);
    }
}
```

### Kelas DAO (Data Access Object)

#### Pattern: GenericDAO Interface
```java
public interface GenericDAO<T, ID> {
    void save(T entity);
    void update(T entity);
    void delete(ID id);
    T findById(ID id);
    List<T> findAll();
    boolean exists(ID id);
}
```

#### 1. <code>ImprovedBarangDAO.java</code>
<p><b>Fungsi:</b> Data access layer untuk entity Barang</p>
<p><b>Tugas:</b> CRUD operations untuk tabel barang</p>

#### 2. <code>PeminjamanDAO.java</code>
<p><b>Fungsi:</b> Data access layer untuk entity Peminjaman</p>
<p><b>Tugas:</b> CRUD operations untuk tabel peminjaman dengan business queries</p>

### Kelas Service (Business Logic)

#### Pattern: BaseService Template
```java
public abstract class BaseService<T extends BaseEntity, ID> {
 
    public final void save(T entity) {
        validateBeforeSave(entity);
        doSave(entity);
        performBusinessLogicAfterSave(entity);
    }
    
    protected abstract void validateBeforeSave(T entity);
    protected abstract void doSave(T entity);
    protected abstract void performBusinessLogicAfterSave(T entity);
}
```

#### 1. <code>BarangService.java</code>
<p><b>Fungsi:</b> Business logic untuk manajemen barang</p>
<p><b>Tugas:</b> Validasi, CRUD, dan business operations untuk barang</p>

#### 2. <code>PeminjamanService.java</code>
<p><b>Fungsi:</b> Business logic untuk proses peminjaman</p>
<p><b>Tugas:</b> Handle peminjaman, pengembalian, dan business rules</p>

### Kelas Controller (UI Logic)

#### 1. <code>LoginView.java</code>
<p><b>Fungsi:</b> Controller untuk halaman login</p>
<p><b>Tugas:</b> Handle authentication dan redirect ke dashboard sesuai role</p>

#### 2. <code>DashboardController.java</code>
<p><b>Fungsi:</b> Controller untuk dashboard admin</p>
<p><b>Tugas:</b> Manage barang, approve permintaan, view reports</p>

#### 3. <code>MahasiswaDashboardController.java</code>
<p><b>Fungsi:</b> Controller untuk dashboard mahasiswa</p>
<p><b>Tugas:</b> Ajukan permintaan, kembalikan barang, view riwayat</p>

## Database

```sql
CREATE DATABASE IF NOT EXISTS peminjaman_db;
USE peminjaman_db;

-- Tabel USERS
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'mahasiswa') NOT NULL,
    nim VARCHAR(20) NULL,  -- Untuk mahasiswa
    admin_level VARCHAR(50) NULL  -- Untuk admin
);

-- Tabel BARANG
CREATE TABLE barang (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama VARCHAR(100) NOT NULL,
    kode VARCHAR(50) UNIQUE NOT NULL,
    kondisi VARCHAR(50) NOT NULL DEFAULT 'Baik',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabel PERMINTAAN (Request dari mahasiswa)
CREATE TABLE permintaan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    barang_id INT NOT NULL,
    nama_pengaju VARCHAR(100) NOT NULL,
    nama_barang VARCHAR(100) NOT NULL,
    kode_barang VARCHAR(50) NOT NULL,
    status ENUM('Menunggu', 'Diacc', 'Ditolak') DEFAULT 'Menunggu',
    tanggal_pengajuan TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tanggal_proses TIMESTAMP NULL,
    keterangan TEXT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (barang_id) REFERENCES barang(id) ON DELETE CASCADE
);

-- Tabel PEMINJAMAN (Actual borrowing records)
CREATE TABLE peminjaman (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    barang_id INT NOT NULL,
    nama_peminjam VARCHAR(100) NOT NULL,
    nama_barang VARCHAR(100) NOT NULL,
    kode_barang VARCHAR(50) NOT NULL,
    tanggal_pinjam TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tanggal_kembali TIMESTAMP NULL,
    status ENUM('Dipinjam', 'Dikembalikan') DEFAULT 'Dipinjam',
    catatan TEXT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (barang_id) REFERENCES barang(id) ON DELETE CASCADE
);

-- Sample Data
INSERT INTO users (nama, username, password, role, admin_level) VALUES 
('Super Admin', 'admin', 'admin123', 'admin', 'Level 1');

INSERT INTO users (nama, username, password, role, nim) VALUES 
('John Doe', 'john', 'john123', 'mahasiswa', '12345678');

INSERT INTO barang (nama, kode, kondisi) VALUES 
('Laptop Dell', 'LPT001', 'Baik'),
('Proyektor Epson', 'PRJ001', 'Baik'),
('Kamera Canon', 'CAM001', 'Baik');
```

<p><b>Penjelasan Skema Database:</b></p>
<li><strong>Relational Design:</strong> Foreign key constraints memastikan data integrity</li>
<li><strong>ENUM Types:</strong> Status dibatasi pada nilai yang valid untuk konsistensi</li>
<li><strong>Timestamps:</strong> Tracking waktu untuk audit trail dan business logic</li>
<li><strong>Cascade Delete:</strong> Otomatis hapus related records saat parent dihapus</li>
<li><strong>Indexing:</strong> Primary keys dan foreign keys otomatis terindex untuk performance</li>

## Arsitektur dan Design Patterns

### **Layered Architecture**
```
┌─────────────────┐
│   UI Layer      │ ← Controllers (JavaFX)
├─────────────────┤
│ Service Layer   │ ← Business Logic (BaseService subclasses)
├─────────────────┤
│   DAO Layer     │ ← Data Access (GenericDAO implementations)
├─────────────────┤
│  Model Layer    │ ← Entities (BaseEntity subclasses)
└─────────────────┘
```
<li>Github: <a href="https://github.com/IrgiRanggaSaputra/Final-Proyek-PBO2">Github</a></li>
<li>Youtube: <a href="https://youtu.be/eibWyXAlFIo?si=zs0cM4_Chd_lty-E">Youtube</a></li>


<p align="center">
  <strong>Aplikasi ini mendemonstrasikan implementasi lengkap 4 pilar OOP</strong>
</p>
