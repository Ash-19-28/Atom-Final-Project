import java.util.ArrayList;

class ReservationManager {
    public static final int TOTAL_SEATS = 4;
    private ArrayList<Passenger> confirmedList = new ArrayList<>();
    private CustomQueue waitlist = new CustomQueue();
    private int ticketCounter = 1;

    public int getSeatsLeft() {
        return TOTAL_SEATS - confirmedList.size();
    }

    public void bookTicket(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Passenger name cannot be blank.");
        }
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidNameException("Passenger name must only contain letters.");
        }

        int ticketId = ticketCounter++;

        if (confirmedList.size() < TOTAL_SEATS) {
            int seatNumber = findAvailableSeat();
            Passenger passenger = new Passenger(ticketId, name, seatNumber);
            confirmedList.add(passenger);
            System.out.println("Booking Confirmed! Ticket ID: " + ticketId + ", Name: " + name + ", Seat: " + seatNumber);
        } else {
            Passenger passenger = new Passenger(ticketId, name, -1);
            waitlist.enqueue(passenger);
            System.out.println("Seats are full. Added to waitlist at position " + waitlist.getSize());
        }
    }

    public void cancelTicket(int ticketId) throws InvalidTicketException {
        if (ticketId <= 0) {
            throw new InvalidTicketException("Ticket ID must be a positive integer.");
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
            System.out.println("Ticket " + ticketId + " cancelled for " + cancelledPassenger.name + ".");

            if (!waitlist.isEmpty()) {
                Passenger promotedPassenger = waitlist.dequeue();
                promotedPassenger.seatNumber = freedSeat;
                confirmedList.add(promotedPassenger);
                System.out.println("AUTO-PROMOTION: " + promotedPassenger.name + " promoted to Seat " + freedSeat + " (Ticket ID: " + promotedPassenger.ticketId + ")");
            } else {
                System.out.println("Seat " + freedSeat + " is now vacant.");
            }
        } else {
            System.out.println("Ticket ID not found in confirmed list.");
        }
    }

    public void viewConfirmed() {
        if (confirmedList.isEmpty()) {
            System.out.println("No confirmed passengers.");
            return;
        }
        System.out.println("\n--- Confirmed Passengers ---");
        for (Passenger p : confirmedList) {
            System.out.println("Seat: " + p.seatNumber + " | Ticket ID: " + p.ticketId + " | Name: " + p.name);
        }
    }

    public void viewWaitlist() {
        System.out.println("\n--- Waitlist Queue ---");
        waitlist.display();
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