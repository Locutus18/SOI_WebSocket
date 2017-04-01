package cinema;

public enum SeatStatus {
    FREE("free"),
    LOCKED("locked"),
    RESERVED("reserved");

    private final String value;

    SeatStatus(String sValue) {
        value = sValue;
    }

    public String value() {
        return value;
    }
}