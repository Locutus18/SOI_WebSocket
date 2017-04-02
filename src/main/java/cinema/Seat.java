package cinema;

class Seat {

    private int row, column;
    private SeatStatus status;

    int getRow() {
        return row;
    }

    Seat setRow(int row) {
        this.row = row;
        return this;
    }

    int getColumn() {
        return column;
    }

    Seat setColumn(int column) {
        this.column = column;
        return this;
    }

    SeatStatus getStatus() {
        return status;
    }

    Seat setStatus(SeatStatus status) {
        this.status = status;
        return this;
    }
}
