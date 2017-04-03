package cinema;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static cinema.MessageType.*;

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
    public Message onMessage(Message message, Session session) throws CinemaException, IOException {
        Message response = new Message();
        Seat seat;
        switch (message.getType()) {
            case IN_INIT:
                room.init(message.getRows(), message.getColumns());
                response.create(OUT_ROOMSIZE, room).sendTo(sessions);
                break;
            case IN_ROOMSIZE:
                return response.create(OUT_ROOMSIZE, room);
            case IN_UPDATESEATS:
                for (int i = 1; i <= room.getRows(); i++)
                    for (int j = 1; j <= room.getColumns(); j++)
                        response.create(OUT_SEATSTATUS, room.getSeat(i, j)).sendTo(session);
                break;
            case IN_LOCK:
                seat = room.getSeat(message.getRow(), message.getColumn());
                int lockId = room.lock(seat);
                response.create(OUT_SEATSTATUS, seat).sendTo(sessions);
                return response.create(OUT_LOCKRESULT, lockId);
            case IN_UNLOCK:
                seat = room.changeLock(message.getLockId(), SeatStatus.FREE);
                response.create(OUT_SEATSTATUS, seat).sendTo(sessions);
                break;
            case IN_RESERVE:
                seat = room.changeLock(message.getLockId(), SeatStatus.RESERVED);
                response.create(OUT_SEATSTATUS, seat).sendTo(sessions);
                break;
        }
        return null;
    }

    @OnError
    public void onError(Throwable exception, Session session) throws IOException {
        new Message(ERROR, exception).sendTo(session);
        if (!(exception instanceof CinemaException))
            exception.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("WebSocket " + session.getId() + " closed.");
    }


}
