package cinema;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

public class Message implements Encoder.Text<Message>, Decoder.Text<Message> {

    private JsonObject json;

    public Message() {
        super();
    }

    Message(JsonObject jsonMessage) {
        json = jsonMessage;
    }

    Message(MessageType type, Object parameter) {
        create(type, parameter);
    }

    Message create(MessageType type, Object parameter) {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("type", type.toString());
        switch (type) {
            case OUT_ROOMSIZE:
                Room room = (Room) parameter;
                builder.add("rows", room.getRows())
                        .add("columns", room.getColumns());
                break;
            case OUT_SEATSTATUS:
                Seat seat = (Seat) parameter;
                builder.add("row", seat.getRow())
                        .add("column", seat.getColumn())
                        .add("status", seat.getStatus().toString());
                break;
            case OUT_LOCKRESULT:
                builder.add("lockId", "lock" + (int) parameter);
                break;
            case ERROR:
                builder.add("message", ((Throwable) parameter).getMessage());
                break;
            default:
                json = null;
                return this;
        }
        json = builder.build();
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

    void sendTo(Session session) throws IOException {
        session.getBasicRemote().sendText(toString());
    }

    void sendTo(Collection<Session> sessions) throws IOException {
        for (Session session : sessions)
            sendTo(session);
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
