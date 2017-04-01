package cinema;

public class Lock implements Comparable<Lock> {

    private Integer id;
    private Seat seat;

    public Lock() {
        id = 0;
    }

    public int getId() {
        return id;
    }

    public Lock setId(int id) {
        this.id = id;
        return this;
    }

    public Seat getSeat() {
        return seat;
    }

    public Lock setSeat(Seat seat) {
        this.seat = seat;
        return this;
    }

    @Override
    public int compareTo(Lock o) {
        return id.compareTo(o.id);
    }
}
