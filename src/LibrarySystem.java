import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LibrarySystem {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();
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
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> borrowBook();
                    case 3 -> returnBook();
                    case 4 -> displayAllBooks();
                    case 5 -> {
                        System.out.println("Keluar dari sistem. Terima kasih!");
                        running = false;
                    }
                    default -> System.out.println("Pilihan tidak valid!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Masukkan angka saja!");
                scanner.nextLine();
            }

            if (running) {
                System.out.println("\nTekan Enter untuk lanjut...");
                scanner.nextLine();
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
            if (title.isEmpty()) throw new IllegalArgumentException("Judul tidak boleh kosong!");

            System.out.print("Author: ");
            String author = scanner.nextLine().trim();
            if (author.isEmpty()) throw new IllegalArgumentException("Author tidak boleh kosong!");

            System.out.print("ISBN (10 digit): ");
            String isbn = scanner.nextLine().trim();
            if (!Book.validateIsbn(isbn)) throw new IllegalArgumentException("ISBN harus 10 digit angka!");

            books.add(new Book(id, title, author, isbn));
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
            Member member = findMemberById(memberId);
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
        if (books.isEmpty()) {
            System.out.println("Belum ada buku.");
            return;
        }

        for (int i = 0; i < books.size(); i++) {
            System.out.println("Buku " + (i + 1) + ":");
            books.get(i).displayInfo();
        }
    }

    private Book findBookById(String id) {
        for (Book b : books) {
            if (b.getId().equalsIgnoreCase(id)) return b;
        }
        return null;
    }

    private Member findMemberById(String id) {
        for (Member m : members) {
            if (m.getMemberId().equalsIgnoreCase(id)) return m;
        }
        return null;
    }

    private void initializeSampleData() {
        members.add(new Member("M001", "Rafly", "Rafli@example.com"));
        members.add(new Member("M002", "Rifqy", "Rifqy@example.com"));
    }
}
