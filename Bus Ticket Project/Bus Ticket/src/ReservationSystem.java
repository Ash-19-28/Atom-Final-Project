import javax.swing.*;
import java.awt.*;

public class ReservationSystem {
    public static void main(String[] args) {
        ReservationManager manager = new ReservationManager();

        JFrame frame = new JFrame("Reservation");
        frame.setSize(450, 520);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(6, 6));

        JLabel statusLabel = new JLabel("Seats Left: " + manager.getSeatsLeft() + " / " + ReservationManager.TOTAL_SEATS, JLabel.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        frame.add(statusLabel, BorderLayout.NORTH);

        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.BOLD, 15));
        frame.add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 4, 4));

        JPanel bookRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        JTextField nameField = new JTextField(10);
        JButton bookBtn = new JButton("Book");
        bookRow.add(new JLabel("Name:"));
        bookRow.add(nameField);
        bookRow.add(bookBtn);

        JPanel cancelRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        JTextField cancelField = new JTextField(6);
        JButton cancelBtn = new JButton("Cancel");
        cancelRow.add(new JLabel("ID:"));
        cancelRow.add(cancelField);
        cancelRow.add(cancelBtn);

        JPanel viewRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        JButton viewConfirmedBtn = new JButton("Confirmed");
        JButton viewWaitlistBtn = new JButton("Waitlist");
        viewRow.add(viewConfirmedBtn);
        viewRow.add(viewWaitlistBtn);

        bottomPanel.add(bookRow);
        bottomPanel.add(cancelRow);
        bottomPanel.add(viewRow);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        bookBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String msg = manager.bookTicket(name);
            logArea.append(msg + "\n");
            nameField.setText("");
            statusLabel.setText("Seats Left: " + manager.getSeatsLeft() + " / " + ReservationManager.TOTAL_SEATS);
        });

        cancelBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(cancelField.getText().trim());
                String msg = manager.cancelTicket(id);
                logArea.append(msg + "\n");
            } catch (NumberFormatException ex) {
                logArea.append("Enter a valid Ticket ID.\n");
            }
            cancelField.setText("");
            statusLabel.setText("Seats Left: " + manager.getSeatsLeft() + " / " + ReservationManager.TOTAL_SEATS);
        });

        viewConfirmedBtn.addActionListener(e -> {
            logArea.append(manager.getConfirmedText() + "\n");
        });

        viewWaitlistBtn.addActionListener(e -> {
            logArea.append(manager.getWaitlistText() + "\n");
        });

        frame.setVisible(true);
    }
}