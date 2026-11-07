public abstract class ParkingType {
    private final String label;
    private final int capacity;

    public ParkingType(String label, int capacity) {
        this.label = label;
        this.capacity = capacity;
    }

    public String getLabel() {
        return label;
    }

    public int getCapacity() {
        return capacity;
    }
    public boolean isReservable() {
        return !this.label.equals("Regular Parking");
    }

    public abstract double computeBaseFee();
    public abstract double computeExtraFee();

    public double computeTotalFee() {
        double raw = computeBaseFee() + computeExtraFee();
        return Math.round(raw * 100.0) / 100.0;
    }
}