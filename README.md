# Hotel Reservation System - Technical Test

## What I Built

A simple hotel reservation system that manages rooms, users, and bookings. The system handles room availability, validates dates, checks user balance, and prints everything in reverse order (newest first).

## How to Run

```bash
# Compile
mvn compile

# Run the demo
java -cp target/classes Demo
```

## Main Components

**Entities:**
- User - has ID and balance
- Room - has number, type (STANDARD/JUNIOR/SUITE), and price per night
- Booking - connects user to room with check-in/check-out dates
- RoomType - enum for the three room types

**Service Methods:**
- `setRoom()` - creates rooms (ignores if already exists)
- `setUser()` - creates users (ignores if already exists)
- `bookRoom()` - books a room with full validation
- `printAll()` - shows rooms and bookings (newest first)
- `printAllUsers()` - shows all users (newest first)

## Challenges I Faced

### 1. Date Overlap Logic

This was the trickiest part. At first I was thinking about all the different cases - booking starts during another, ends during, completely inside, etc. It was getting complicated.

Then I found this simple formula: `start1.before(end2) && start2.before(end1)`

This one line handles ALL overlap cases! Took me a while to understand why it works, but once I got it, everything clicked.

**Important thing:** Checkout day is free. So if one booking ends 07/07 and another starts 07/07, they don't conflict. The `.before()` method handles this correctly.

### 2. Calculating Nights

Another thing that took some time was figuring out how to calculate nights between two dates.

```java
long diffInMillis = checkOut.getTime() - checkIn.getTime();
int nights = (int) (diffInMillis / (1000 * 60 * 60 * 24));
```

The math is: get milliseconds difference, then divide by (1000ms × 60s × 60min × 24h) to get days.

### 3. Date Validation

Had to check several things:
- checkOut can't be before checkIn (reversed dates)
- checkIn can't be in the past
- checkIn can't equal checkOut (no 0-night bookings)

Used `date.before()` and `date.equals()` for these checks.

### 4. Printing in Reverse

The requirement said "latest to oldest" so I had to loop backwards. Almost made a mistake here - started with `i = list.size()` which would crash! The correct way is `i = list.size() - 1` because array indices start at 0.

### 5. Date Formatting

Dates print ugly by default: `Tue Jul 07 00:00:00 WEST 2026`

Used SimpleDateFormat to make them readable: `07/07/2026`

```java
SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
```

## Design Choices

**Why ArrayList?**
The test said to use ArrayList. In a real app I'd probably use a Map for faster lookups, but ArrayList works fine for this.

**Why ignore duplicates in setRoom/setUser?**
The requirement said setRoom shouldn't impact existing bookings. If we let people modify room prices, it would mess up bookings that already happened. So I made it ignore duplicate calls.

**Why throw exceptions in bookRoom?**
Unlike setRoom/setUser, bookRoom needs to fail loudly when something's wrong (user doesn't exist, not enough money, etc.). This is important business logic.

**Why format dates only when printing?**
I keep Date objects in the Booking class because I need them for calculations (comparing dates, calculating nights). Only convert to strings when printing.

## What I Learned

- Date overlap is simpler than it looks - one formula does everything
- Working with java.util.Date is a bit annoying (deprecated methods, timezone stuff)
- Validation order matters - check simple stuff first (null checks) before expensive operations (checking all bookings)
- Keeping data (Date objects) separate from presentation (formatted strings) makes code more flexible

## Time Spent

Around 4-5 hours:
- 1 hour understanding requirements and planning
- 2 hours coding (bookRoom was the hardest)
- 1 hour testing and fixing bugs
- 1 hour adding comments and docs

## Notes

- Used Java 17
- Followed the "simplest solution" guideline
- Didn't worry too much about perfect formatting (test said it's okay)
- Focused on making code work correctly and be easy to understand
