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

    private static void sendMessage(Session session, Message message) throws IOException {
        session.getBasicRemote().sendText(message.toString());
    }

    private static void broadcastMessage(Message message) throws IOException {
        for (Session session : sessions)
            sendMessage(session, message);
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("WebSocket " + session.getId() + " opened.");
    }

    @OnMessage
    public Message onMessage(Message message, Session session) throws CinemaException, IOException {
        Message response = new Message();
        Seat seat;
        switch (message.getType()) {
            case IN_INIT:
                room.init(message.getRows(), message.getColumns());
                broadcastMessage(response.createRoomSizeMessage(room));
                break;
            case IN_ROOMSIZE:
                return response.createRoomSizeMessage(room);
            case IN_UPDATESEATS:
                for (int i = 1; i <= room.getRows(); i++)
                    for (int j = 1; j <= room.getColumns(); j++)
                        sendMessage(session, response.createSeatStatusMessage(room.getSeat(i, j)));
                break;
            case IN_LOCK:
                seat = room.getSeat(message.getRow(), message.getColumn());
                int lockId = room.lock(seat);
                broadcastMessage(response.createSeatStatusMessage(seat));
                return response.createLockResultMessage(lockId);
            case IN_UNLOCK:
                broadcastMessage(response.createSeatStatusMessage(room.changeLock(message.getLockId(), SeatStatus.FREE)));
                break;
            case IN_RESERVE:
                seat = room.changeLock(message.getLockId(), SeatStatus.RESERVED);
                broadcastMessage(response.createSeatStatusMessage(seat));
                break;
        }
        return null;
    }

    @OnError
    public void onError(Throwable exception, Session session) throws IOException {
        System.err.println(exception.getMessage());
        sendMessage(session, new Message().createErrorMessage(exception));
        if (!(exception instanceof CinemaException))
            exception.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("WebSocket " + session.getId() + " closed.");
    }


}
