package cinema;

public class Lock {

    private String id;
    private Seat seat;

    public String getId() {
        return id;
    }

    public Lock setId(String id) {
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
}
