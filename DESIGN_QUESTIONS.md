# Design Questions - My Answers

## Question 1: Is putting all functions in the same Service class recommended?

**My answer: No, not really.**

### Why Not?

This violates the Single Responsibility Principle (one of the SOLID principles). The Service class is doing too many different things:
- Managing users
- Managing rooms  
- Managing bookings
- Printing everything

When one class does too much, it becomes:
- Harder to maintain (change one thing, might break another)
- Harder to test (have to test everything together)
- Harder to understand (too much code in one place)
- Harder to extend later

### What Would Be Better

Split it into separate services:

```
UserService:
- setUser()
- printAllUsers()
- findUserById()

RoomService:
- setRoom()
- findRoomByNumber()

BookingService:
- bookRoom()
- isRoomAvailable()
- datesOverlap()

PrintService:
- printAll()
```

Each service has one clear job.

### Benefits

- Each class is simpler and focused
- Can test each service separately
- Changes to user logic don't affect booking logic
- Different people can work on different services
- Can reuse services in other parts of the app

### Why I Kept It Simple Here

For this test I kept everything in one Service class because:
- The requirement said "go for the simplest solution"
- It's a small test (only 5 methods)
- Time constraint (30 minutes)
- Focus was on functionality working correctly

In a real project at work, I'd definitely split this up.

---

## Question 2: setRoom() shouldn't impact bookings - What's another way?

**Current approach:**
```java
public void setRoom(int roomNumber, RoomType type, int price) {
    if (roomExists(roomNumber)) {
        return; // Just ignore it
    }
    rooms.add(new Room(roomNumber, type, price));
}
```

This works because we don't let anyone modify existing rooms. Once created, it stays the same.

### Other Ways I Thought About

#### Option 1: Throw an Exception

```java
if (roomExists(roomNumber)) {
    throw new IllegalArgumentException("Room already exists");
}
```

**Good:** Makes it very clear you can't modify rooms
**Bad:** Caller has to handle the exception

#### Option 2: Separate Create and Update

```java
public void createRoom(...) {
    // Only creates new rooms
}

public void updateRoom(...) {
    // Only updates if no bookings exist
    if (hasBookings(roomNumber)) {
        throw new Exception("Can't update - has bookings");
    }
}
```

**Good:** Clear what each method does
**Bad:** More complex, need to check for bookings

#### Option 3: Store Room Info in Booking

This is probably the best for a real system:

```java
public class Booking {
    private User user;
    private Room room;
    private Date checkIn;
    private Date checkOut;
    
    // Store the price at booking time
    private int bookedPrice;
    
    public Booking(User user, Room room, ...) {
        this.user = user;
        this.room = room;
        // Remember the price when booking was made
        this.bookedPrice = room.getPricePerNight();
    }
}
```

**Good:** 
- Bookings remember their original price
- Can change room prices without affecting past bookings
- This is how real hotel systems work

**Bad:** 
- More complex
- Store extra data

### What I'd Use in Production

Option 3 (store snapshot) because:
- Hotels need to change prices over time
- Past bookings should keep their original price
- More flexible

### Why I Used Current Approach

For this test, ignoring duplicates is simpler and meets the requirement "shouldn't impact bookings". No need to over-complicate it.


## Summary

**Question 1:** One big Service class isn't ideal - violates Single Responsibility. Better to split into UserService, RoomService, BookingService. But for this test, keeping it simple was fine.

**Question 2:** Current approach (ignore duplicates) works. For production, storing room snapshot in Booking would be better so you can change prices without affecting past bookings.


