# Hotel Reservation - Submission Summary

## What's Done

✓ All 5 methods implemented
✓ Full date validation (past dates, reversed dates, overlaps)
✓ Balance checking
✓ Room availability checking
✓ Printing in reverse order
✓ Date formatting (dd/MM/yyyy)
✓ Demo with test cases
✓ Comments explaining the logic

## How to Test

```bash
mvn compile
java -cp target/classes Demo
```

You'll see:
- Test results (which bookings passed/failed)
- Rooms data
- Bookings data
- Users data

## Tricky Parts I Solved

**Date Overlap:**
Figured out this formula handles all cases: `start1.before(end2) && start2.before(end1)`

**Calculating Nights:**
Had to convert milliseconds to days: `(checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24)`

**Reverse Printing:**
Loop from `size() - 1` down to `0` (not from `size()`!)

## Files Included

- `Service.java` - Main service (all methods)
- `User.java`, `Room.java`, `Booking.java` - Entities
- `RoomType.java` - Enum
- `Demo.java` - Test cases
- `README.md` - Full explanation
- `DESIGN_QUESTIONS.md` - Bonus questions answered

## Time Spent

Around 4-5 hours including testing and documentation.

## Notes

The date overlap logic took me the longest to figure out, but once I got it, everything else fell into place. Kept the code simple and focused on making it work correctly.
