import java.util.Calendar;
import java.util.Date;

public class Demo {
    public static void main(String[] args) {

        Service service = new Service();

        // Create 3 rooms
        service.setRoom(1, RoomType.STANDARD, 1000);
        service.setRoom(2, RoomType.JUNIOR, 2000);
        service.setRoom(3, RoomType.SUITE, 3000);

        // Create 2 users  
        service.setUser(1, 5000);
        service.setUser(2, 10000);

        Calendar calendar = Calendar.getInstance();

        System.out.println("=== Testing Bookings ===\n");

        // Test 1: Should FAIL - insufficient balance
        calendar.set(2026, Calendar.JUNE, 30, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date checkIn1 = calendar.getTime();
        calendar.set(2026, Calendar.JULY, 7, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date checkOut1 = calendar.getTime();
        
        try {
            service.bookRoom(1, 2, checkIn1, checkOut1);
            System.out.println("Test 1 FAILED");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 1 PASSED: " + e.getMessage());
        }

        // Test 2: Should FAIL - invalid dates
        calendar.set(2026, Calendar.JULY, 7, 0, 0, 0);
        Date checkIn2 = calendar.getTime();
        calendar.set(2026, Calendar.JUNE, 30, 0, 0, 0);
        Date checkOut2 = calendar.getTime();
        
        try {
            service.bookRoom(1, 2, checkIn2, checkOut2);
            System.out.println("Test 2 FAILED");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 2 PASSED: " + e.getMessage());
        }

        // Test 3: Should SUCCEED
        calendar.set(2026, Calendar.JULY, 7, 0, 0, 0);
        Date checkIn3 = calendar.getTime();
        calendar.set(2026, Calendar.JULY, 8, 0, 0, 0);
        Date checkOut3 = calendar.getTime();

        try {
            service.bookRoom(1, 1, checkIn3, checkOut3);
            System.out.println("Test 3 PASSED: User 1 booked Room 1");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3 FAILED: " + e.getMessage());
        }

        // Test 4: Should FAIL - room conflict
        calendar.set(2026, Calendar.JULY, 7, 0, 0, 0);
        Date checkIn4 = calendar.getTime();
        calendar.set(2026, Calendar.JULY, 9, 0, 0, 0);
        Date checkOut4 = calendar.getTime();

        try {
            service.bookRoom(2, 1, checkIn4, checkOut4);
            System.out.println("Test 4 FAILED");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 4 PASSED: " + e.getMessage());
        }

        // Test 5: Should SUCCEED
        calendar.set(2026, Calendar.JULY, 7, 0, 0, 0);
        Date checkIn5 = calendar.getTime();
        calendar.set(2026, Calendar.JULY, 8, 0, 0, 0);
        Date checkOut5 = calendar.getTime();
        
        try {
            service.bookRoom(2, 3, checkIn5, checkOut5);
            System.out.println("Test 5 PASSED: User 2 booked Room 3");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 5 FAILED: " + e.getMessage());
        }

        // Test setRoom on existing room
        System.out.println("\n=== Testing setRoom ===");
        service.setRoom(1, RoomType.SUITE, 10000);
        System.out.println("setRoom called - should be ignored");

        // Print results
        System.out.println("\n=== Final Results ===\n");
        service.printAll();
        System.out.println("\n===================\n");
        service.printAllUsers();
    }
}
