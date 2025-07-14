public class Book extends Item implements Borrowable {
    private String author;
    private String isbn;
    private String borrowedBy;

    public Book(String id, String title, String author, String isbn) {
        super(id, title);
        this.author = author;
        this.isbn = isbn;
        this.borrowedBy = null;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    @Override
    public void displayInfo() {
        System.out.println("=== INFORMASI BUKU ===");
        System.out.println("ID: " + id);
        System.out.println("Judul: " + title);
        System.out.println("Penulis: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Status: " + (isAvailable ? "Tersedia" : "Dipinjam oleh " + borrowedBy));
        System.out.println("======================");
    }

    @Override
    public boolean borrowItem(String memberId) {
        if (isAvailable) {
            isAvailable = false;
            borrowedBy = memberId;
            return true;
        }
        return false;
    }

    @Override
    public boolean returnItem() {
        if (!isAvailable) {
            isAvailable = true;
            borrowedBy = null;
            return true;
        }
        return false;
    }

    public static boolean validateIsbn(String isbn) {
        return isbn != null && isbn.matches("\\d{10}");
    }
}
