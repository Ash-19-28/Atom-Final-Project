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
    public String getWaitlistText() {
        if (isEmpty()) {
            return "Waitlist is empty.";
        }
        StringBuilder sb = new StringBuilder("--- Waitlist Queue ---\n");
        Node current = front;
        int position = 1;
        while (current != null) {
            sb.append("Pos ").append(position++)
                    .append(" | Ticket ID: ").append(current.data.ticketId)
                    .append(" | Name: ").append(current.data.name).append("\n");
            current = current.next;
        }
        return sb.toString().trim();
    }
}