import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Member {
    private String memberId;
    private String name;
    private String email;

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public static boolean validateEmail(String email) {
        return email != null && email.contains("@") && email.indexOf("@") > 0 && email.indexOf("@") < email.length() - 1;
    }

    public void displayMemberInfo() {
        System.out.println("=== INFORMASI MEMBER ===");
        System.out.println("ID Member: " + memberId);
        System.out.println("Nama: " + name);
        System.out.println("Email: " + email);
        System.out.println("========================");
    }

    public static Member findById(String id) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM members WHERE member_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Member(
                        rs.getString("member_id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.out.println("Gagal mencari member: " + e.getMessage());
        }
        return null;
    }

    public void saveToDB() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO members (member_id, name, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, memberId);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal menyimpan member: " + e.getMessage());
        }
    }
}
