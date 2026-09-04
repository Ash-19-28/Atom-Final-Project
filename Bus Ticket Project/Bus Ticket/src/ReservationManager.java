import java.util.ArrayList;

public class ReservationManager {
    public static final int TOTAL_SEATS = 4;
    private ArrayList<Passenger> confirmedList = new ArrayList<>();
    private CustomQueue waitlist = new CustomQueue();
    private int ticketCounter = 1;

    public int getSeatsLeft() {
        return TOTAL_SEATS - confirmedList.size();
    }

    public String bookTicket(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Passenger name cannot be blank.");
        }
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidNameException("Name must only contain letters.");
        }

        int ticketId = ticketCounter++;

        if (confirmedList.size() < TOTAL_SEATS) {
            int seatNumber = findAvailableSeat();
            Passenger passenger = new Passenger(ticketId, name, seatNumber);
            confirmedList.add(passenger);
            return "BOOKED: " + name + " (Seat #" + seatNumber + ", Ticket #" + ticketId + ")\n";
        } else {
            Passenger passenger = new Passenger(ticketId, name, -1);
            waitlist.enqueue(passenger);
            return "WAITLIST: " + name + " (Pos #" + waitlist.getSize() + ", Ticket #" + ticketId + ")\n";
        }
    }

    public String cancelTicket(int ticketId) throws InvalidTicketException {
        if (ticketId <= 0) {
            throw new InvalidTicketException("Ticket ID must be greater than 0.");
        }

        Passenger cancelledPassenger = null;
        int indexToRemove = -1;

        for (int i = 0; i < confirmedList.size(); i++) {
            if (confirmedList.get(i).ticketId == ticketId) {
                cancelledPassenger = confirmedList.get(i);
                indexToRemove = i;
                break;
            }
        }

        if (cancelledPassenger != null) {
            int freedSeat = cancelledPassenger.seatNumber;
            confirmedList.remove(indexToRemove);
            String output = "CANCELLED: Ticket #" + ticketId + " (" + cancelledPassenger.name + ")\n";

            if (!waitlist.isEmpty()) {
                Passenger promoted = waitlist.dequeue();
                promoted.seatNumber = freedSeat;
                confirmedList.add(promoted);
                output += "AUTO-PROMOTED: " + promoted.name + " to Seat #" + freedSeat + "!\n";
            } else {
                output += "Seat #" + freedSeat + " is now vacant.\n";
            }
            return output;
        } else {
            return "Ticket ID #" + ticketId + " not found in confirmed list.\n";
        }
    }

    public String getConfirmedText() {
        if (confirmedList.isEmpty()) {
            return "No confirmed passengers.\n";
        }
        StringBuilder sb = new StringBuilder("--- Confirmed Passengers ---\n");
        for (Passenger p : confirmedList) {
            sb.append("Seat #").append(p.seatNumber).append(" | Ticket #").append(p.ticketId)
                    .append(" | ").append(p.name).append("\n");
        }
        return sb.toString();
    }

    public String getWaitlistText() {
        return "--- Waitlist Queue ---\n" + waitlist.getDisplayList();
    }

    private int findAvailableSeat() {
        for (int seat = 1; seat <= TOTAL_SEATS; seat++) {
            boolean taken = false;
            for (Passenger p : confirmedList) {
                if (p.seatNumber == seat) {
                    taken = true;
                    break;
                }
            }
            if (!taken) {
                return seat;
            }
        }
        return -1;
    }
}