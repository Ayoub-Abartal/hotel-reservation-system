import java.util.Date;

public class Booking {
    private final User user;
    private final Room room;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Booking(User user, Room room, Date checkInDate, Date checkOutDate) {
        this.user = user;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public User getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }
    
    // Helper method to calculate number of nights
    public int getNumberOfNights() {
        long diffInMillis = checkOutDate.getTime() - checkInDate.getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }
    
    // Helper method to calculate total cost
    public int getTotalCost() {
        return getNumberOfNights() * room.getPricePerNight();
    }
}
