public class User {
    private final int userId;
    private int balance;

    public User(int userId, int balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance;
    }

    public void deductBalance(int amount) {
        this.balance -= amount;
    }

    @Override
    public boolean equals(Object object){
        if(this == object) return true;
        if(!(object instanceof User)) return false;
        User user = (User) object;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return userId;  
    }
}
