package cinema;

import java.util.*;

class Room {

    private int rows, columns;
    private List<Seat> seats;
    private SortedMap<Integer, Seat> locks;

    Room() {
        seats = Collections.synchronizedList(new ArrayList<>());
        locks = Collections.synchronizedSortedMap(new TreeMap<>());
        rows = columns = 0;
    }


    Seat getSeat(int row, int column) throws CinemaException {
        if (row >= rows && column >= columns)
            throw new CinemaException("Seat does not exist.");
        return seats.get(row * columns + column);
    }

    int getRows() {
        return rows;
    }

    int getColumns() {
        return columns;
    }

    void init(int rows, int columns) throws CinemaException {
        if (rows < 0 || columns < 0)
            throw new CinemaException("Invalid size parameters.");
        locks.clear();
        seats.clear();
        this.rows = rows;
        this.columns = columns;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                seats.add(new Seat()
                        .setRow(i)
                        .setColumn(j)
                        .setStatus(SeatStatus.FREE));
    }

    int lock(Seat seat) throws CinemaException {
        if (!seats.contains(seat))
            throw new CinemaException("Invalid seat reference.");
        if (seat.getStatus() != SeatStatus.FREE)
            throw new CinemaException("Seat cannot be locked.");
        int lockId = (locks.isEmpty()) ? 0 : locks.lastKey() + 1;
        seat.setStatus(SeatStatus.LOCKED);
        locks.put(lockId, seat);
        return lockId;
    }

    Seat changeLock(int lockId, SeatStatus newStatus) throws CinemaException {
        if (!locks.containsKey(lockId))
            throw new CinemaException("Lock does not exist.");
        Seat seat = locks.get(lockId);
        if (seat.getStatus() != SeatStatus.LOCKED)
            throw new CinemaException("Seat is not locked.");
        seat.setStatus(newStatus);
        return seat;
    }
}

