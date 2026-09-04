import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ReservationUI {
    private JFrame frame;
    private JLabel statusLabel;
    private JTextArea log;
    private JTextField nameInput;
    private JTextField cancelInput;
    private JButton bookBtn;
    private JButton cancelBtn;
    private JButton viewConfBtn;
    private JButton viewWaitBtn;
    private ReservationManager manager;

    public ReservationUI(ReservationManager manager) {
        this.manager = manager;
        initComponents();
        initListeners();
    }

    private void initComponents() {
        frame = new JFrame("Reservation System");
        frame.setSize(360, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(6, 6));

        statusLabel = new JLabel("Seats Left: " + manager.getSeatsLeft() + " / " + ReservationManager.TOTAL_SEATS, JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
        frame.add(statusLabel, BorderLayout.NORTH);

        log = new JTextArea();
        log.setEditable(false);
        log.setFont(new Font("Monospaced", Font.PLAIN, 12));
        frame.add(new JScrollPane(log), BorderLayout.CENTER);

        JPanel controls = new JPanel(new GridLayout(3, 1, 4, 4));

        JPanel bookRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        nameInput = new JTextField(10);
        bookBtn = new JButton("Book");
        bookRow.add(new JLabel("Name:"));
        bookRow.add(nameInput);
        bookRow.add(bookBtn);

        JPanel cancelRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        cancelInput = new JTextField(6);
        cancelBtn = new JButton("Cancel");
        cancelRow.add(new JLabel("Ticket ID:"));
        cancelRow.add(cancelInput);
        cancelRow.add(cancelBtn);

        JPanel viewRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        viewConfBtn = new JButton("Confirmed");
        viewWaitBtn = new JButton("Waitlist");
        viewRow.add(viewConfBtn);
        viewRow.add(viewWaitBtn);

        controls.add(bookRow);
        controls.add(cancelRow);
        controls.add(viewRow);
        frame.add(controls, BorderLayout.SOUTH);
    }

    private void initListeners() {
        bookBtn.addActionListener(e -> {
            try {
                String res = manager.bookTicket(nameInput.getText());
                log.append(res);
                nameInput.setText("");
                statusLabel.setText("Seats Left: " + manager.getSeatsLeft() + " / " + ReservationManager.TOTAL_SEATS);
            } catch (InvalidNameException ex) {
                log.append("BOOKING ERROR: " + ex.getMessage() + "\n");
            }
        });

        cancelBtn.addActionListener(e -> {
            String text = cancelInput.getText().trim();
            try {
                if (text.isEmpty()) {
                    throw new EmptyFieldException("Please enter a ticket number to cancel.");
                }
                try {
                    int id = Integer.parseInt(text);
                    String res = manager.cancelTicket(id);
                    log.append(res);
                    cancelInput.setText("");
                    statusLabel.setText("Seats Left: " + manager.getSeatsLeft() + " / " + ReservationManager.TOTAL_SEATS);
                } catch (NumberFormatException nfe) {
                    throw new InvalidTicketException("Ticket ID must be a numeric integer.");
                }
            } catch (EmptyFieldException | InvalidTicketException ex) {
                log.append("CANCEL ERROR: " + ex.getMessage() + "\n");
            }
        });

        viewConfBtn.addActionListener(e -> log.append(manager.getConfirmedText()));
        viewWaitBtn.addActionListener(e -> log.append(manager.getWaitlistText()));
    }

    public void show() {
        frame.setVisible(true);
    }
}