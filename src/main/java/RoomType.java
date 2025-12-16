public enum RoomType {
    STANDARD,
    JUNIOR,
    SUITE;
    
    // Helper method to parse from string (case-insensitive)
    public static RoomType fromString(String type) {
        return RoomType.valueOf(type.toUpperCase());
    }
}
