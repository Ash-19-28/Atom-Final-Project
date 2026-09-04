public class CustomQueue {
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

    public String getDisplayList() {
        if (isEmpty()) {
            return "Waitlist is empty.\n";
        }
        StringBuilder sb = new StringBuilder();
        Node current = front;
        int pos = 1;
        while (current != null) {
            sb.append("WL Pos ").append(pos).append(" | Ticket #").append(current.data.ticketId)
                    .append(" | ").append(current.data.name).append("\n");
            current = current.next;
            pos++;
        }
        return sb.toString();
    }
}