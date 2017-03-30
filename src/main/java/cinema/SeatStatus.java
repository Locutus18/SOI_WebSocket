package cinema;

public enum SeatStatus {
        FREE("Free"),
        LOCKED("Locked"),
        RESERVED("Reserved");

        private final String value;
        SeatStatus(String v) {
            value = v;
        }

        public String value() {
            return value;
        }
    }