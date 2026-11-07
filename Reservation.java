public class Reservation {
    private final String reservationId;
    private final Vehicle vehicle;
    private final ParkingType service;
    private final String scheduled;

    public Reservation(String reservationId, Vehicle vehicle, ParkingType service,
                       String scheduled) {
        this.reservationId = reservationId;
        this.vehicle = vehicle;
        this.service = service;
        this.scheduled = scheduled == null ? "" : scheduled;
    }

    public String getReservationId() {
        return reservationId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingType getService() {
        return service;
    }

    public String getScheduled() {
        return scheduled;
    }

    @Override public String toString() {
        return reservationId + " - " + vehicle.getPlateNumber()
                + " (" + service.getLabel() + ")";
    }
}
