package cinema;

public class Seat {

    private int row, column;
    private SeatStatus status;

    public int getRow() {
        return row;
    }

    public Seat setRow(int row) {
        this.row = row;
        return this;
    }

    public int getColumn() {
        return column;
    }

    public Seat setColumn(int column) {
        this.column = column;
        return this;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public Seat setStatus(SeatStatus status) {
        this.status = status;
        return this;
    }
}
