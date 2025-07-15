import java.sql.*;
import java.util.Scanner;

public class LibrarySystem {
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        LibrarySystem lib = new LibrarySystem();
        lib.initializeSampleData();
        lib.run();
    }

    private void run() {
        boolean running = true;

        while (running) {
            showMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> borrowBook();
                    case 3 -> returnBook();
                    case 4 -> displayAllBooks();
                    case 5 -> {
                        System.out.println("Keluar dari sistem. Semua data akan dihapus...");
                        resetDatabase();
                        running = false;
                    }
                    default -> System.out.println("Pilihan tidak valid!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka yang valid!");
            }
        }
    }


    private void showMenu() {
        System.out.println("\n=== PERPUSTAKAAN DIGITAL ===");
        System.out.println("1. Tambah Buku");
        System.out.println("2. Pinjam Buku");
        System.out.println("3. Kembalikan Buku");
        System.out.println("4. Lihat Semua Buku");
        System.out.println("5. Exit");
        System.out.print("Pilih: ");
    }

    private void addBook() {
        try {
            System.out.print("ID Buku: ");
            String id = scanner.nextLine().trim();
            if (id.isEmpty()) throw new IllegalArgumentException("ID tidak boleh kosong!");

            if (findBookById(id) != null) throw new IllegalArgumentException("ID sudah ada!");

            System.out.print("Judul: ");
            String title = scanner.nextLine().trim();
            System.out.print("Author: ");
            String author = scanner.nextLine().trim();
            System.out.print("ISBN (10 digit): ");
            String isbn = scanner.nextLine().trim();
            if (!Book.validateIsbn(isbn)) throw new IllegalArgumentException("ISBN tidak valid!");

            Book book = new Book(id, title, author, isbn);
            book.saveToDB();
            System.out.println("Buku berhasil ditambahkan!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void borrowBook() {
        try {
            System.out.print("ID Buku: ");
            String bookId = scanner.nextLine().trim();
            Book book = findBookById(bookId);
            if (book == null) throw new IllegalArgumentException("Buku tidak ditemukan!");
            if (!book.isAvailable()) throw new IllegalArgumentException("Buku sedang dipinjam!");

            System.out.print("ID Member: ");
            String memberId = scanner.nextLine().trim();
            Member member = Member.findById(memberId);
            if (member == null) throw new IllegalArgumentException("Member tidak ditemukan!");

            if (book.borrowItem(memberId)) {
                System.out.println("Buku berhasil dipinjam oleh " + member.getName());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void returnBook() {
        try {
            System.out.print("ID Buku: ");
            String bookId = scanner.nextLine().trim();
            Book book = findBookById(bookId);
            if (book == null) throw new IllegalArgumentException("Buku tidak ditemukan!");
            if (book.isAvailable()) throw new IllegalArgumentException("Buku belum dipinjam!");

            if (book.returnItem()) {
                System.out.println("Buku berhasil dikembalikan.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayAllBooks() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM books";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            int count = 1;
            while (rs.next()) {
                System.out.println("Buku " + (count++) + ":");
                Book book = new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn")
                );
                book.setAvailable(rs.getBoolean("is_available"));
                book.displayInfo();
            }

            if (count == 1) {
                System.out.println("Belum ada buku.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data buku: " + e.getMessage());
        }
    }

    private Book findBookById(String id) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Book book = new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn")
                );
                book.setAvailable(rs.getBoolean("is_available"));
                return book;
            }
        } catch (SQLException e) {
            System.out.println("Gagal mencari buku: " + e.getMessage());
        }
        return null;
    }

    private void resetDatabase() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql1 = "DELETE FROM books";
            String sql2 = "DELETE FROM members";

            conn.createStatement().executeUpdate(sql1);
            conn.createStatement().executeUpdate(sql2);

            System.out.println("Seluruh data berhasil direset (hapus total).");
        } catch (SQLException e) {
            System.out.println("Gagal mereset database: " + e.getMessage());
        }
    }


    private void initializeSampleData() {
        Member m1 = new Member("M001", "Rafly", "Rafly@example.com");
        Member m2 = new Member("M002", "Rifqy", "Rifqy@example.com");
        m1.saveToDB();
        m2.saveToDB();
    }
}
