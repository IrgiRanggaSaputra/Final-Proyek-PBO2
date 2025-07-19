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

![tampilan aplikasi](C:\Users\Irgi\Downloads\injemkeun.png)
## Demo Proyek
<ul>
  <li>Github: <a href="">Github</a></li>
  <li>Youtube: <a href="">Youtube</a></li>
</ul>
