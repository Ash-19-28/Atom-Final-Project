import java.util.ArrayList;
import java.util.Scanner;

public class ReservationManager {
    public static final int TOTAL_SEATS = 4;
    private ArrayList<Passenger> confirmedList = new ArrayList<>();
    private CustomQueue waitlist = new CustomQueue();
    private int ticketCounter = 1;

    public int getSeatsLeft() {
        return TOTAL_SEATS - confirmedList.size();
    }

    public String bookTicket(String name) {
        if (name.isEmpty()) {
            return "Name cannot be empty.";
        }

        int ticketId = ticketCounter++;

        if (confirmedList.size() < TOTAL_SEATS) {
            int seatNumber = findAvailableSeat();
            Passenger passenger = new Passenger(ticketId, name, seatNumber);
            confirmedList.add(passenger);
            return "BOOKED: " + name + " (Seat #" + seatNumber + ", Ticket #" + ticketId + ")";
        } else {
            Passenger passenger = new Passenger(ticketId, name, -1);
            waitlist.enqueue(passenger);
            return "WAITLIST: " + name + " (Pos #" + waitlist.getSize() + ", Ticket #" + ticketId + ")";
        }
    }
    public String cancelTicket(int ticketId) {
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
            String result = "CANCELLED: Ticket #" + ticketId + " (" + cancelledPassenger.name + ")\n";

            if (!waitlist.isEmpty()) {
                Passenger promotedPassenger = waitlist.dequeue();
                promotedPassenger.seatNumber = freedSeat;
                confirmedList.add(promotedPassenger);
                result += "AUTO-PROMOTED: " + promotedPassenger.name + " to Seat #" + freedSeat + "!";
            } else {
                result += "Seat #" + freedSeat + " is now vacant.";
            }
            return result;
        } else {
            return "Ticket ID #" + ticketId + " not found.";
        }
    }

    public int findAvailableSeat() {
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
        waitlist.display();
    }
    public String getConfirmedText() {
        if (confirmedList.isEmpty()) {
            return "No confirmed passengers.";
        }
        StringBuilder sb = new StringBuilder("--- Confirmed Passengers ---\n");
        for (Passenger p : confirmedList) {
            sb.append("Seat: ").append(p.seatNumber)
                    .append(" | Ticket ID: ").append(p.ticketId)
                    .append(" | Name: ").append(p.name).append("\n");
        }
        return sb.toString().trim();
    }

    public String getWaitlistText() {
        return waitlist.getWaitlistText();
    }
}

