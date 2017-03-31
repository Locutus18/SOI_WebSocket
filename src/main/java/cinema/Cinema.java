package cinema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Cinema {

    private int rows, columns;
    private List<Seat> seats;
    private Map<String, Seat> locks;

    public Cinema() {
        seats = new ArrayList<>();
        locks = new TreeMap<>();
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

    public Seat getSeat(int row, int column) {
        if (row < rows && column < columns)
            return seats.get(row * columns + column);
        return null;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}

