import java.util.ArrayList;
import java.util.Scanner;


public class ReservationSystem {
    private static final int TOTAL_SEATS = 4;
    private static ArrayList<Passenger> confirmedList = new ArrayList<>();
    private static CustomQueue waitlist = new CustomQueue();
    private static int ticketCounter = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            int seatsLeft = TOTAL_SEATS - confirmedList.size();
            System.out.println("\n--- Bus/Train Reservation System ---");
            System.out.println("Seats Left: " + seatsLeft + " / " + TOTAL_SEATS);
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. View Confirmed Passengers");
            System.out.println("4. View Waitlist");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                scanner.next();
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    bookTicket(scanner);
                    break;
                case 2:
                    cancelTicket(scanner);
                    break;
                case 3:
                    viewConfirmed();
                    break;
                case 4:
                    waitlist.display();
                    break;
                case 5:
                    System.out.println("Exiting the system.");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void bookTicket(Scanner scanner) {
        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
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

    private static void cancelTicket(Scanner scanner) {
        System.out.print("Enter Ticket ID to cancel: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid Ticket ID.");
            scanner.next();
            return;
        }

        int ticketId = scanner.nextInt();
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

    private static int findAvailableSeat() {
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

    private static void viewConfirmed() {
        if (confirmedList.isEmpty()) {
            System.out.println("No confirmed passengers.");
            return;
        }
        System.out.println("\n--- Confirmed Passengers ---");
        for (Passenger p : confirmedList) {
            System.out.println("Seat: " + p.seatNumber + " | Ticket ID: " + p.ticketId + " | Name: " + p.name);
        }
    }
}