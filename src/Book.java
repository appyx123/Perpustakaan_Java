import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Book extends Item implements Borrowable {
    private String author;
    private String isbn;

    public Book(String id, String title, String author, String isbn) {
        super(id, title);
        this.author = author;
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public void displayInfo() {
        System.out.println("=== INFORMASI BUKU ===");
        System.out.println("ID: " + id);
        System.out.println("Judul: " + title);
        System.out.println("Penulis: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Status: " + (isAvailable ? "Tersedia" : "Sedang dipinjam"));
        System.out.println("======================");
    }

    @Override
    public boolean borrowItem(String memberId) {
        if (isAvailable) {
            isAvailable = false;
            updateAvailabilityInDB();
            return true;
        }
        return false;
    }

    @Override
    public boolean returnItem() {
        if (!isAvailable) {
            isAvailable = true;
            updateAvailabilityInDB();
            return true;
        }
        return false;
    }

    public void saveToDB() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO books (id, title, author, isbn, is_available) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, title);
            stmt.setString(3, author);
            stmt.setString(4, isbn);
            stmt.setBoolean(5, isAvailable);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan buku: " + e.getMessage());
        }
    }

    public void updateAvailabilityInDB() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "UPDATE books SET is_available = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, isAvailable);
            stmt.setString(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal update status buku: " + e.getMessage());
        }
    }

    public static boolean validateIsbn(String isbn) {
        return isbn != null && isbn.matches("\\d{10}");
    }
}
