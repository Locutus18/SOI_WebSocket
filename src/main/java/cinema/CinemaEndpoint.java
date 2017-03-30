package cinema;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/cinema")
public class CinemaEndpoint {

    private static final Cinema cinema = new Cinema();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket opened: " + session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        //TODO
        return null;
    }

    @OnError
    public void onError(Throwable error) {
        System.err.println(error.getMessage());
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Closing a WebSocket: " + session.getId());
    }


}
