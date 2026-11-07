public class RegularPark extends ParkingType {
    public RegularPark() {
        super("Regular Parking", 200);
    }
    public double computeBaseFee() { return 25.0; }

    public double computeExtraFee() { return 0; }
}