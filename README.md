# 📚 Sistem Perpustakaan Digital (Java CLI + MySQL)

Aplikasi berbasis **console (CLI)** untuk mengelola sistem peminjaman buku perpustakaan. Dibuat menggunakan **Java** dan terintegrasi dengan **MySQL** untuk penyimpanan data.

---

## 🧩 Fitur

- Tambah Buku ke database
- Pinjam Buku (mengubah status `is_available` menjadi `false`)
- Kembalikan Buku (mengubah status kembali ke `true`)
- Lihat Semua Buku yang tersimpan di database
- Exit Program (otomatis menghapus seluruh isi tabel `books` dan `members`)

---

## ⚙️ Teknologi & Tools

- Java (OOP + JDBC)
- MySQL (Database penyimpanan)
- IntelliJ IDEA (IDE)
- MySQL Connector/J (JDBC driver)

---

## 🏗️ Struktur Program

| File | Deskripsi |
|------|-----------|
| `Item.java` | Abstract class dasar untuk semua item |
| `Book.java` | Class buku, terhubung ke database dan hanya menggunakan `isAvailable` sebagai status |
| `Borrowable.java` | Interface untuk fitur pinjam & kembali buku |
| `Member.java` | Class data anggota perpustakaan, lengkap dengan validasi dan simpan ke DB |
| `LibrarySystem.java` | Main class yang menjalankan aplikasi dan berisi seluruh menu utama |
| `DatabaseUtil.java` | Utility class untuk koneksi database MySQL (harus dibuat) |

---

## 🛠️ Setup & Konfigurasi

1. **Tambahkan file JDBC MySQL driver ke project**  
   Gunakan: `mysql-connector-j-*.jar`

2. **Struktur tabel MySQL yang digunakan**:
   ```sql
   CREATE TABLE books (
       id VARCHAR(20) PRIMARY KEY,
       title VARCHAR(100),
       author VARCHAR(100),
       isbn VARCHAR(20),
       is_available TINYINT(1)
   );

   CREATE TABLE members (
       member_id VARCHAR(20) PRIMARY KEY,
       name VARCHAR(100),
       email VARCHAR(100)
   );
