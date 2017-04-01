package cinema;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/cinema", encoders = {Message.class}, decoders = {Message.class})
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
        Message response = null;
        Seat seat;
        switch (message.getType()) {
            case IN_INIT:
                room.init(message.getRows(), message.getColumns());
                return null;
            case IN_ROOMSIZE:
                return Message.createRoomSizeMessage(room);
            case IN_UPDATESEATS:
                Message.sendSeatStatusMessages(session, room);
                return null;
            case IN_LOCK:
                seat = room.getSeat(message.getRow(), message.getColumn());
                response = Message.createLockResultMessage(room.lock(seat));
                break;
            case IN_UNLOCK:
                seat = room.unlock(message.getLockId());
                break;
            case IN_RESERVE:
                seat = room.reserve(message.getLockId());
                break;
            default:
                return null;
        }
        Message.broadcastSeatStatusMessage(sessions, seat);
        return response;
    }

    @OnError
    public void onError(Throwable exception, Session session) throws IOException {
        System.err.println(exception.getMessage());
        session.getBasicRemote().sendText(Message.createErrorMessage(exception).toString());
        exception.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("WebSocket " + session.getId() + " closed.");
    }


}
