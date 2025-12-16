import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Service {
    
    // Store all rooms in the system
    ArrayList<Room> rooms;
    // Store all users in the system
    ArrayList<User> users;
    // Store all bookings made
    ArrayList<Booking> bookings;

    public Service() {
        // Initialize empty lists for rooms, users, and bookings
        this.rooms = new ArrayList<>();
        this.users = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight) {
        // Check if room with this number already exists
        for (Room room : rooms){
            // If room exists, ignore this call (don't modify existing rooms)
            if( roomNumber == room.getRoomNumber()) return;
        }
        
        // Room doesn't exist, so create and add it to the list
        rooms.add(new Room(roomNumber, roomType, roomPricePerNight));
    }

    public void bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut) {
        // Step 1: Find the user who wants to book
        User user = findUserById(userId);
        if( user == null) throw new IllegalArgumentException("User not Found");

        // Step 2: Find the room to book
        Room room = findRoomByNumber(roomNumber);
        if( room ==  null ) throw new IllegalArgumentException("Room not Found");

        // Step 3: Validate dates
        // Check if checkOut is before checkIn (invalid)
        // Check if checkIn is in the past (can't book past dates)
        // Check if checkIn equals checkOut (0 nights booking not allowed)
        if( checkOut.before(checkIn) || checkIn.before(new Date()) || checkIn.equals(checkOut)) 
            throw new IllegalArgumentException("Invalid Date");

        // Step 4: Calculate number of nights and total cost
        // Get difference in milliseconds between dates
        long diffInMillis = checkOut.getTime() - checkIn.getTime();
        // Convert milliseconds to days (1 day = 1000ms * 60s * 60min * 24h)
        int nights = (int) (diffInMillis / (1000 * 60 * 60 * 24));
        // Calculate total cost: nights * price per night
        int totalCost = nights * room.getPricePerNight();

        // Step 5: Check if user has enough balance
        if(user.getBalance() < totalCost)  
            throw new IllegalArgumentException("Not Enough Balance!");
            
        // Step 6: Check if room is available (no overlapping bookings)
        if(!isRoomAvailable(room, checkIn, checkOut)) 
            throw new IllegalArgumentException("Room Unavailable!");

        // Step 7: All checks passed - create booking and deduct balance
        bookings.add(new Booking(user, room, checkIn, checkOut));
        user.deductBalance(totalCost);
    }

    public void printAll() {
        // Create date formatter to show dates as dd/MM/yyyy instead of full date string
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
        // Print all rooms from latest created to oldest
        System.out.println("Rooms Data:");
        System.out.println("|| Room Number || Type || Price Per Night ||");
        // Loop backwards through rooms list (latest to oldest)
        for(int i = rooms.size() - 1 ; i >= 0; i--){
            System.out.println("   " + rooms.get(i).getRoomNumber() + "   " 
                + rooms.get(i).getType() + "   " 
                + rooms.get(i).getPricePerNight());
        }

        // Print all bookings from latest created to oldest
        System.out.println("\nBookings Data:");
        System.out.println("|| User || Room || CheckIn || CheckOut ||");
        // Loop backwards through bookings list (latest to oldest)
        for(int i = bookings.size() - 1 ; i >= 0; i--){
            System.out.println("   " + bookings.get(i).getUser().getUserId() + "   " 
                + bookings.get(i).getRoom().getRoomNumber() + "   " 
                + sdf.format(bookings.get(i).getCheckInDate()) + "   " 
                + sdf.format(bookings.get(i).getCheckOutDate()));
        }
    }

    public void setUser(int userId, int balance) {
        // Check if user with this ID already exists
        for(User user : users){
            // If user exists, ignore this call (don't modify existing users)
            if(userId == user.getUserId()) return;
        }
        
        // User doesn't exist, so create and add it to the list
        users.add(new User(userId, balance));
    }

    public void printAllUsers() {
        // Print all users from latest created to oldest
        System.out.println("|| UserId || Balance ||");
        // Loop backwards through users list (latest to oldest)
        for(int i = users.size() - 1 ; i >= 0 ; i--){
            System.out.println("   " + users.get(i).getUserId() + "   " + users.get(i).getBalance());
        }
    }
    
    // Helper method: Find user by ID
    private User findUserById(int userId) {
        // Loop through all users
        for(User user : users){
            // If we find matching user ID, return that user
            if(user.getUserId() == userId) return user;
        }
        // User not found, return null
        return null;
    }
    
    // Helper method: Find room by room number
    private Room findRoomByNumber(int roomNumber) {
        // Loop through all rooms
        for(Room room : rooms){
            // If we find matching room number, return that room
            if(room.getRoomNumber() == roomNumber) return room;
        }
        // Room not found, return null
        return null;
    }
    
    // Helper method: Check if room is available for given dates
    private boolean isRoomAvailable(Room room, Date checkIn, Date checkOut) {
        // Loop through all existing bookings
        for(Booking booking : bookings){
            // Only check bookings for this specific room
            if(booking.getRoom().getRoomNumber() == room.getRoomNumber()){
                // Check if the new booking dates overlap with existing booking
                if(datesOverlap(checkIn, checkOut, 
                        booking.getCheckInDate(), booking.getCheckOutDate())){
                    // Dates overlap - room is NOT available
                    return false;
                }
            }
        }
        // No overlapping bookings found - room IS available
        return true;
    }
    
    // Helper method: Check if two date ranges overlap
    private boolean datesOverlap(Date start1, Date end1, Date start2, Date end2) {
        // Two date ranges overlap if:
        // start1 is before end2 AND start2 is before end1
        // This handles all overlap cases including partial overlaps
        return start1.before(end2) && start2.before(end1);
    }
}
