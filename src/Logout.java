import javax.swing.JOptionPane;

public class Logout {

    private Dashboard dashboard;
    private Database data;

    public Logout(Dashboard dashboard) {

        this.dashboard = dashboard;
        this.data = new Database();
    }

    public void log_out() {

        dashboard.logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                dashboard,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION && data.logout()) {

                    dashboard.dispose();

                    Dashboard.start();

                }
        });
    }
}