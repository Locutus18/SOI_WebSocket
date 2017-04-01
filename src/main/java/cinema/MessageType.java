package cinema;

public enum MessageType {
    IN_INIT("initRoom"),
    IN_ROOMSIZE("getRoomSize"),
    IN_UPDATESEATS("updateSeats"),
    IN_LOCK("lockSeat"),
    IN_UNLOCK("unlockSeat"),
    IN_RESERVE("reserveSeat"),
    OUT_ROOMSIZE("roomSize"),
    OUT_SEATSTATUS("seatStatus"),
    OUT_LOCKRESULT("lockResult"),
    ERROR("error");

    private final String value;

    MessageType(String sValue) {
        value = sValue;
    }

    public static MessageType fromString(String string) {
        for (MessageType type : MessageType.values())
            if (type.value.equalsIgnoreCase(string))
                return type;
        return null;
    }

    public String value() {
        return value;
    }

}
