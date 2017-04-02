package cinema;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.*;
import java.io.StringReader;

import static cinema.MessageType.*;

public class Message implements Encoder.Text<Message>, Decoder.Text<Message> {

    private JsonObject json;

    public Message() {
        super();
    }

    private Message(JsonObject jsonMessage) {
        json = jsonMessage;
    }

    Message createRoomSizeMessage(Room room) {
        json = Json.createObjectBuilder()
                .add("type", OUT_ROOMSIZE.toString())
                .add("rows", room.getRows())
                .add("columns", room.getColumns())
                .build();
        return this;
    }

    Message createSeatStatusMessage(Seat seat) {
        json = Json.createObjectBuilder()
                .add("type", OUT_SEATSTATUS.toString())
                .add("row", seat.getRow())
                .add("column", seat.getColumn())
                .add("status", seat.getStatus().toString())
                .build();
        return this;
    }

    Message createLockResultMessage(int lockId) {
        json = Json.createObjectBuilder()
                .add("type", OUT_LOCKRESULT.toString())
                .add("lockId", "lock" + lockId)
                .build();
        return this;
    }

    Message createErrorMessage(Throwable error) {
        json = Json.createObjectBuilder()
                .add("type", ERROR.toString())
                .add("message", error.getMessage())
                .build();
        return this;
    }

    MessageType getType() {
        return MessageType.fromString(json.getString("type"));
    }

    int getRow() {
        return json.getInt("row");
    }

    int getRows() {
        return json.getInt("rows");
    }

    int getColumn() {
        return json.getInt("column");
    }

    int getColumns() {
        return json.getInt("columns");
    }

    int getLockId() {
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
        System.out.println("Inbound JSON:");
        System.out.println(stringMessage);
        return new Message(Json.createReader(new StringReader(stringMessage)).readObject());
    }

    @Override
    public String encode(Message message) throws EncodeException {
        System.out.println("Outbound JSON:");
        System.out.println(message.toString());
        return message.toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}
