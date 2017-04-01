package cinema;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

public class Message implements Encoder.Text<Message>, Decoder.Text<Message> {

    private JsonObject json;

    public Message() {
        json = Json.createObjectBuilder().build();
    }

    public Message(JsonObject jsonMessage) {
        json = jsonMessage;
    }

    public static Message createRoomSizeMessage(Room room) {
        return new Message(Json.createObjectBuilder()
                .add("type", MessageType.OUT_ROOMSIZE.toString())
                .add("rows", room.getRows())
                .add("columns", room.getColumns())
                .build());
    }

    public static Message createSeatStatusMessage(Seat seat) {
        return new Message(Json.createObjectBuilder()
                .add("type", MessageType.OUT_SEATSTATUS.toString())
                .add("row", seat.getRow())
                .add("column", seat.getColumn())
                .add("status", seat.getStatus().toString())
                .build());
    }

    public static Message createLockResultMessage(int lockId) {
        return new Message(Json.createObjectBuilder()
                .add("type", MessageType.OUT_LOCKRESULT.toString())
                .add("lockId", "lock" + lockId)
                .build());
    }

    public static Message createErrorMessage(Throwable error) {
        return new Message(Json.createObjectBuilder()
                .add("type", MessageType.ERROR.toString())
                .add("message", error.getMessage())
                .build());
    }

    public static void sendSeatStatusMessages(Session session, Room room) throws CinemaException, IOException {
        for (int i = 0; i < room.getRows(); i++)
            for (int j = 0; j < room.getColumns(); j++)
                session.getBasicRemote().sendText(createSeatStatusMessage(room.getSeat(i, j)).toString());
    }

    public static void broadcastSeatStatusMessage(Set<Session> sessions, Seat seat) throws IOException {
        for (Session session : sessions)
            session.getBasicRemote().sendText(createSeatStatusMessage(seat).toString());
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

    public int getLockId() {
        return Integer.valueOf(json.getString("lockId").substring(4));
    }

    @Override
    public String toString() {
        return json.toString();
    }

    @Override
    public boolean willDecode(String stringMessage) {
        try {
            Json.createReader(new StringReader(stringMessage)).readObject();
            return true;
        } catch (JsonException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Message decode(String stringMessage) throws DecodeException {
        return new Message(Json.createReader(new StringReader(stringMessage)).readObject());
    }

    @Override
    public String encode(Message message) throws EncodeException {
        return message.toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
