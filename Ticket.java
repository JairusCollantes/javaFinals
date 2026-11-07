public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingType service;
    private final double totalFee;
    private boolean exited;

    public Ticket(String ticketId, Vehicle vehicle, ParkingType service) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.service = service;
        this.totalFee = service.computeTotalFee();
        this.exited = false;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingType getService() {
        return service;
    }

    public double getTotalFee() {
        return totalFee;
    }
    public boolean isExited() {
        return exited;
    }

    public void markExited() {
        this.exited = true;
    }

    @Override public String toString() {
        return ticketId + " - " + vehicle.getPlateNumber()
                + " (" + service.getLabel() + ")";
    }
}