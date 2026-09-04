import java.util.ArrayList;
import java.util.Scanner;










public class TerminalRunner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ReservationManager manager = new ReservationManager();
        int choice = 0;

        do {
            System.out.println("\n--- Bus/Train Reservation System ---");
            System.out.println("Seats Left: " + manager.getSeatsLeft() + " / " + ReservationManager.TOTAL_SEATS);
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. View Confirmed Passengers");
            System.out.println("4. View Waitlist");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            // Try-Catch 1: Validating menu choice range
            try {
                if (!scanner.hasNextInt()) {
                    System.out.println("Please enter a valid numeric value.");
                    scanner.next();
                    continue;
                }
                choice = scanner.nextInt();
                scanner.nextLine();

                if (choice < 1 || choice > 5) {
                    throw new MenuChoiceException("Choice out of range! Select a number between 1 and 5.");
                }
            } catch (MenuChoiceException e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter passenger name: ");
                    String name = scanner.nextLine();
                    // Try-Catch 2: Validating passenger name input
                    try {
                        manager.bookTicket(name);
                    } catch (InvalidNameException e) {
                        System.out.println("Booking Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter Ticket ID to cancel: ");
                    // Try-Catch 3: Validating ticket ID format and value
                    try {
                        if (!scanner.hasNextInt()) {
                            scanner.next();
                            throw new InvalidTicketException("Ticket ID must be an integer.");
                        }
                        int ticketId = scanner.nextInt();
                        scanner.nextLine();
                        manager.cancelTicket(ticketId);
                    } catch (InvalidTicketException e) {
                        System.out.println("Cancellation Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    manager.viewConfirmed();
                    break;

                case 4:
                    manager.viewWaitlist();
                    break;

                case 5:
                    System.out.println("Exiting system.");
                    break;
            }
        } while (choice != 5);

        scanner.close();
    }
}