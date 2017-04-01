package cinema;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/cinema", encoders = {MessageCoder.class}, decoders = {MessageCoder.class})
public class CinemaEndpoint {

    private static final Room room = new Room();
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("WebSocket " + session.getId() + " opened.");
    }

    @OnMessage
    public Message onMessage(Message message, Session session) throws Exception {
        switch (message.getType()) {
            case IN_INIT:
                room.init(message.getRows(), message.getColumns());
                break;
            case IN_ROOMSIZE:
                return Message.sendRoomSize(room);
            case IN_UPDATESEATS:
                for (int i = 0; i < room.getRows(); i++)
                    for (int j = 0; j < room.getColumns(); j++)
                        session.getBasicRemote().sendText(Message.sendSeatStatus(room.getSeat(i, j)).toString());
                break;
            case IN_LOCK:
                Lock lock = room.lock(message.getRow(), message.getColumn());
                for (Session s : sessions)
                    s.getBasicRemote().sendText(Message.sendSeatStatus(lock.getSeat()).toString());
                return Message.sendLockResult(lock.getId());
            case IN_UNLOCK:
                //Todo
                break;
            case IN_RESERVE:
                //Todo
                break;
        }
        return new Message();
    }

    @OnError
    public void onError(Throwable exception, Session session) throws IOException {
        System.err.println(exception.getMessage());
        session.getBasicRemote().sendText(Message.sendError(exception).toString());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("WebSocket " + session.getId() + " closed.");
    }


}
