package cinema;

import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class CinemaEncoder implements Encoder.Text<JsonObject> {

    @Override
    public String encode(JsonObject message) throws EncodeException {
        //TODO
        return null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {

    }
}
