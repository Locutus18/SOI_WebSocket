package cinema;

import javax.json.Json;
import javax.websocket.*;
import java.io.StringReader;

public class MessageCoder implements Encoder.Text<Message>, Decoder.Text<Message> {

    @Override
    public Message decode(String stringMessage) throws DecodeException {
        return new Message(Json.createReader(new StringReader(stringMessage)).readObject());
    }

    @Override
    public boolean willDecode(String stringMessage) {
        try {
            Json.createReader(new StringReader(stringMessage)).readObject();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
