import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReservationManager manager = new ReservationManager();
            ReservationUI ui = new ReservationUI(manager);
            ui.show();
        });
    }
}