# Final Proyek Pemrograman Berorientasi Obyek 2
<ul>
  <li>Mata Kuliah: Pemrograman Berorientasi Obyek 2</li>
  <li>Dosen Pengampu: <a href="https://github.com/Muhammad-Ikhwan-Fathulloh">Muhammad Ikhwan Fathulloh</a></li>
</ul>

## Profil
<ul>
  <li>Nama: Irgi Rangga Saputra</li>
  <li>NIM: 23552011343 </li>
  <li>Studi Kasus: Aplikasi Peminjaman Barang</li>
</ul>

## Judul Studi Kasus
<p>Aplikasi Peminjaman Barang (Injemkeun)</p>

## Penjelasan Studi Kasus
<p>Injemkeun adalah aplikasi desktop sederhana yang dirancang untuk memudahkan proses peminjaman dan pengelolaan inventaris barang di lingkungan kampus, 
  khususnya untuk kebutuhan mahasiswa. Aplikasi ini memungkinkan mahasiswa untuk mengajukan permintaan peminjaman barang, 
  serta memudahkan admin dalam mengelola ketersediaan, persetujuan, dan pengembalian barang</p>

## Fitur Utama
<li>Pencarian Barang: Cari barang yang tersedia berdasarkan nama</li>
<li>Pengajuan Peminjaman: Mahasiswa dapat mengajukan peminjaman barang</li>
<li>Persetujuan Admin: Admin dapat menyetujui atau menolak perminataan peminjaman</li>
<li>Manajemen Data Barang: Admin dapat menambahkan, mengedit, dan mengedit data barang</li>
<li>Riwayat Peminjaman: Menyimpan riwayat peminjaman barang oleh mahasiswa</li>
<li>Login Berdasarkan Role: Terdapat dua jenis pengguna <code>admin</code> dan <code>mahasiswa</code></li>

## Penjelasan 4 Pilar OOP dalam Studi Kasus

### 1. Inheritance
<p><b>Analogi: Keluarga</b></p>
<li>Anak mewarisi sifat orang tua</li>

```java
public class Admin extends User {           
public class User extends BaseEntity {     


public Admin(String username, String password) {
    super(username, password);  
    setRole("admin");
}
```
<li>Admin "warisi" semua kemampuan User</li>
<li>User "warisi" semua kemampuan BaseEntity</li>


### 2. Encapsulation
<p><b>Analogi: Loker</b></p>

```java
private String password; 
public String getPassword() { return password; } 
```
<li>Password disimpan private seperti kunci pada loker</li>
<li>Hanya bisa diakses melalui method khusus (kunci)</li>


### 3. Polymorphism
<p><b>Analogi: Seragam yang sama, Tugas berbeda</b></p>

```java
User admin = new Admin();
User mahasiswa = new Mahasiswa();
```
<li> Admin dan Mahasiwa sama sama pakai "seragam" user</li>
<li>Tapi cara kerja nya berbeda beda</li>

### 4. Abstract
<p><b>Analogi: Blueprint/Denah</b></p>

```java
public abstract class BaseEntity {
    public abstract boolean isValid(); // Aturan wajib
}
```
<li>Setiap "rumah" (class) harus punya pintu (method isValid)</li>

## Penjelasan Sederhana Setiap Kelas dan Fungsinya

### Kelas Utama
<li><code>App.java</code></li>
<p><b>Fungsi:</b> Kelas utama untuk menjalankan aplikasi JavaFx</p>

```java
public class App extends Application {
    public static void main(String[] args) {
        launch(args); // Menjalankan aplikasi
    }
}
```
<li><b>Tugas:</b> Start aplikasi dan tampilkan halaman login</li>

### Kelas Model (Data/Entity)
1. <code>BaseEntity.java</code>
<p><b>Fungsi:</b> Kelas induk untuk semua data</p>
<li><b>Tugas:</b> Menyediakan ID dan Aturan dasar untuk semua data</li>

```java
public abstract class BaseEntity {
    protected int id;
    public abstract boolean isValid(); // Harus diisi anak-anaknya
}
```

2. <code>user.java</code>
<p><b>Fungsi:</b> Data pengguna dasar (username, password)</p>
<p><b>Tugas:</b> Menyimpan informasi login pengguna</p>

```java
public class User extends BaseEntity {
    private String username, password, role;
}
```


3. <code>Admin.java</code>
<p><b>Fungsi:</b> Pengguna dengan hak akses penuh</p>
<p><b>Tugas:</b> Bisa kelola barang dan approve permintaan</p>

```java
public class Admin extends User {
    public boolean canManageBarang() { return true; }
}
```

4. <code>Mahasiswa.java</code></li>
<p><b>Fungsi:</b> pengguna yang bisa pinjam barang</p>
<p><b>Tugas:</b> Bisa ajukan pinjam barang, lihat riwayat</p>

```java
public class Mahasiswa extends User {
    private String nim, jurusan;
}
```

5. <code>Barang.java</code></li>
<p><b>Fungsi:</b> Data barang yang dipinjam</p>
<p><b>Tugas:</b> Menyimpan info barang (nama, kode, kondisi)</p>

```java
public class Barang extends BaseEntity {
    private String nama, kode, kondisi;
}
```

6. <code>Permintaan.java</code></li>
<p><b>Fungsi:</b> Data permintaan pinjam barang</p>
<p><b>Tugas:</b> Mencatat siapa pinjam apa dan statusnya</p>

```java
public class PermintaanModel extends BaseEntity {
    private String namaPengaju, namaBarang, status;
}
```

## Database

```Sql
CREATE DATABASE IF NOT EXISTS peminjaman_db;
USE peminjaman_db;

-- Tabel USERS
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama VARCHAR(100),
    username VARCHAR(50),
    password VARCHAR(100),
    role ENUM('admin', 'mahasiswa')
);

-- Tabel BARANG
CREATE TABLE barang (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nama VARCHAR(100),
    kode VARCHAR(50),
    kondisi VARCHAR(50)
);

-- Tabel PENGAJUAN
CREATE TABLE pengajuan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    barang_id INT,
    tanggal_pengajuan DATETIME,
    status ENUM('Menunggu', 'Diacc', 'Ditolak'),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (barang_id) REFERENCES barang(id)
);

-- Tabel PERMINTAAN
CREATE TABLE permintaan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    barang_id INT,
    status ENUM('Menunggu', 'Diacc', 'Ditolak'),
    tanggal_pengajuan TIMESTAMP,
    tanggal_acc TIMESTAMP,
    keterangan TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (barang_id) REFERENCES barang(id)
);
```
<p><b>Penjelasan Singkat:</b></p>
<li>Semua tabel menggunakan relasi foreign key untuk menghubungkan data antar tabel <code>(user_id dan barang_id)</code></li>
<li>status pada pengajuan dan permintaan menggunakan ENUM agar hanya menerima tiga nilai: <code>'Menunggu', 'Diacc', atau 'Ditolak'</code></li>
<li>Tabel permintaan memiliki tambahan kolom tanggal_acc dan keterangan untuk kebutuhan approval dan catatan</li>




## Demo Proyek
<ul>
  <li>Github: <a href="">Github</a></li>
  <li>Youtube: <a href="">Youtube</a></li>
</ul>
