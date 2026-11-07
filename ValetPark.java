public class ValetPark extends ParkingType {
    public static final double EXTRA_FEE = 100;
    public ValetPark() {
        super("Valet Service", 10);
    }
    public double computeBaseFee() { return 25.0; }
    public double computeExtraFee() { return EXTRA_FEE; }
}
