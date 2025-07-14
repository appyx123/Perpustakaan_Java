public class Member {
    private String memberId;
    private String name;
    private String email;

    public Member(String memberId, String name, String email) {
        setMemberId(memberId);
        setName(name);
        setEmail(email);
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID member tidak boleh kosong!");
        }
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Format email tidak valid!");
        }
        this.email = email;
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
}
