public class Vehicle {
    private final String plateNumber;
    private final String ownerName;
    private final String vehicleType;

    public Vehicle(String plateNumber, String ownerName, String vehicleType) {
        this.plateNumber = plateNumber;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }
    public String getVehicleType() {
        return vehicleType;
    }

    public String toString() {
        return plateNumber + " (" + vehicleType + ")";
    }
}
