
import javax.swing.JOptionPane;

public class Logout {

    private final Dashboard dashboard;
    private final Database data;

    public Logout(Dashboard dashboard, Database data) {
        this.dashboard = dashboard;
        this.data = data;
    }

    public void log_out() {

        dashboard.logoutButton.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                dashboard,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            if (data.logout()) {

                dashboard.dispose();
                Dashboard.start();

            } else {

                JOptionPane.showMessageDialog(
                    dashboard,
                    "Logout failed. Please check your Database.logout() method.",
                    "Logout Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
