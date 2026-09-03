
import java.util.ArrayList;
import java.util.Scanner;

    class Passenger {
        int ticketId;
        String name;
        int seatNumber;

        public Passenger(int ticketId, String name, int seatNumber) {
            this.ticketId = ticketId;
            this.name = name;
            this.seatNumber = seatNumber;
        }
    }

    class Node {
        Passenger data;
        Node next;

        public Node(Passenger data) {
            this.data = data;
            this.next = null;
        }
    }

    class CustomQueue {
        private Node front;
        private Node rear;
        private int size;

        public CustomQueue() {
            this.front = null;
            this.rear = null;
            this.size = 0;
        }

        public void enqueue(Passenger p) {
            Node newNode = new Node(p);
            if (rear == null) {
                front = rear = newNode;
            } else {
                rear.next = newNode;
                rear = newNode;
            }
            size++;
        }

        public Passenger dequeue() {
            if (isEmpty()) {
                return null;
            }
            Passenger p = front.data;
            front = front.next;
            if (front == null) {
                rear = null;
            }
            size--;
            return p;
        }

        public boolean isEmpty() {
            return front == null;
        }

        public int getSize() {
            return size;
        }

        public void display() {
            if (isEmpty()) {
                System.out.println("Waitlist is empty.");
                return;
            }
            Node current = front;
            int position = 1;
            while (current != null) {
                System.out.println("Position " + position + " | Ticket ID: " + current.data.ticketId + " | Name: " + current.data.name);
                current = current.next;
                position++;
            }
        }
    }

    class ReservationManager {
        public static final int TOTAL_SEATS = 4;
        private ArrayList<Passenger> confirmedList = new ArrayList<>();
        private CustomQueue waitlist = new CustomQueue();
        private int ticketCounter = 1;

        public int getSeatsLeft() {
            return TOTAL_SEATS - confirmedList.size();
        }

        public void bookTicket(String name) {
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

        public void cancelTicket(int ticketId) {
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

     class TerminalRunner {
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

                if (!scanner.hasNextInt()) {
                    System.out.println("Please enter a valid number.");
                    scanner.next();
                    continue;
                }

                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter passenger name: ");
                        String name = scanner.nextLine().trim();
                        manager.bookTicket(name);
                        break;
                    case 2:
                        System.out.print("Enter Ticket ID to cancel: ");
                        if (scanner.hasNextInt()) {
                            int ticketId = scanner.nextInt();
                            manager.cancelTicket(ticketId);
                        } else {
                            System.out.println("Invalid Ticket ID.");
                            scanner.next();
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
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 5);

            scanner.close();
        }
    }
