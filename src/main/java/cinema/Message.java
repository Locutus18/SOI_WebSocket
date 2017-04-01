package cinema;

import javax.json.Json;
import javax.json.JsonObject;

public class Message {

    private JsonObject json;

    public Message() {
        json = Json.createObjectBuilder().build();
    }

    public Message(JsonObject jsonMessage) {
        json = jsonMessage;
    }

    public static Message sendRoomSize(Room room) {
        return new Message(Json.createObjectBuilder()
                .add("type", MessageType.OUT_ROOMSIZE.value())
                .add("rows", room.getRows())
                .add("columns", room.getColumns())
                .build());
    }

    public static Message sendSeatStatus(Seat seat) {
        return new Message(Json.createObjectBuilder()
                .add("type", MessageType.OUT_SEATSTATUS.value())
                .add("row", seat.getRow())
                .add("column", seat.getColumn())
                .add("status", seat.getStatus().value())
                .build());
    }

    public static Message sendLockResult(int lockId) {
        return new Message(Json.createObjectBuilder()
                .add("type", MessageType.OUT_LOCKRESULT.value())
                .add("lockId", "lock" + lockId)
                .build());
    }

    public static Message sendError(Throwable error) {
        return new Message(Json.createObjectBuilder()
                .add("type", MessageType.ERROR.value())
                .add("message", error.getMessage())
                .build());
    }

    public MessageType getType() {
        return MessageType.fromString(json.getString("type"));
    }

    public int getRow() {
        return json.getInt("row");
    }

    public int getRows() {
        return json.getInt("rows");
    }

    public int getColumn() {
        return json.getInt("column");
    }

    public int getColumns() {
        return json.getInt("columns");
    }

    public String getLockId() {
        return json.getString("lockId");
    }

    @Override
    public String toString() {
        return json.toString();
    }
}
