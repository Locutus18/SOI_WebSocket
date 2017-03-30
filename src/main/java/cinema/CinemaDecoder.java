package cinema;

import org.codehaus.jettison.json.JSONObject;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class CinemaDecoder implements Decoder.Text<JSONObject> {

    @Override
    public JSONObject decode(String s) throws DecodeException {
        return null;
    }

    @Override
    public boolean willDecode(String s) {
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
