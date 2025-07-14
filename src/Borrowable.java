public interface Borrowable {
    boolean borrowItem(String memberId);
    boolean returnItem();
}
