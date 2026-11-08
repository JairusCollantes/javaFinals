import java.util.ArrayList;
import java.util.List;

public class ParkingSystem {
    private final List<Ticket> tickets = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private int nextTicketNumber = 1001;
    private int nextReservationNumber = 6001;

    public List<Ticket> getTickets() {
        return tickets;
    }
    public List<Reservation> getReservations() {
        return reservations;
    }
    public List<User> getUsers() {
        return users;
    }

    public void addUser(User u) {
        users.add(u);
    }

    public boolean hasUser(String name) {
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public User findUser(String name) {
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(name)) {
                return u;
            }
        }
        return null;
    }

    public List<Ticket> getActiveTickets() {
        List<Ticket> out = new ArrayList<>();
        for (Ticket t : tickets)
            if (!t.isExited()) {
                out.add(t);
            }
        return out;
    }

    public List<Ticket> getTicketsByOwner(String ownerName) {
        List<Ticket> out = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getVehicle().getOwnerName().equalsIgnoreCase(ownerName)) {
                out.add(t);
            }
        }
        return out;
    }

    public int getActiveCount(ParkingType type) {
        int n = 0;
        for (Ticket t : tickets) {
            if (!t.isExited() && t.getService().getLabel().equals(type.getLabel())) {
                n++;
            }
        }
        for (Reservation r : reservations) {
            if (r.getService().getLabel().equals(type.getLabel())) {
                n++;
            }
        }
        return n;
    }

    public boolean removeReservation(String reservationId) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(reservationId)) {
                reservations.remove(i);
                return true;
            }
        }
        return false;
    }

    public int getAvailable(ParkingType type) {
        return type.getCapacity() - getActiveCount(type);
    }

    public void addTicket(Ticket t) {
        tickets.add(t);
    }
    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public String nextTicketId(){
        return "TKT-" + (nextTicketNumber++);
    }
    public String nextReservationId() {
        return "RSV-" + (nextReservationNumber++);
    }
}
