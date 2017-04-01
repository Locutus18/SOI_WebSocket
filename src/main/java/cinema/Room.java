package cinema;

import java.util.*;

public class Room {

    private int rows, columns;
    private List<Seat> seats;
    private SortedSet<Lock> locks;

    public Room() {
        seats = Collections.synchronizedList(new ArrayList<>());
        locks = Collections.synchronizedSortedSet(new TreeSet<>());
        rows = columns = 0;
    }

    public void init(int rows, int columns) {
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

    public Seat getSeat(int row, int column) throws CinemaException {
        if (row >= rows && column >= columns)
            throw new CinemaException("Seat does not exist.");
        return seats.get(row * columns + column);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Lock lock(int row, int column) throws CinemaException {
        Seat seat = getSeat(row, column);
        if (seat.getStatus() != SeatStatus.FREE)
            throw new CinemaException("Seat cannot be locked.");
        Lock lock = new Lock()
                .setId((locks.isEmpty()) ? 0 : locks.last().getId() + 1)
                .setSeat(seat.setStatus(SeatStatus.LOCKED));
        locks.add(lock);
        return lock;
    }
}

