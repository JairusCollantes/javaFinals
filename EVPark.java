public class EVPark extends ParkingType{
    public static final double SPOT = 50;
    public EVPark() {
        super("EV Charging", 4);
    }
    public double computeBaseFee() { return SPOT; }
    public double computeExtraFee() { return 0.0; }
}

