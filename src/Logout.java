public class Logout {

    private Dashboard dashboard;
    private Database data;

    public Logout(Dashboard dashboard) {

        this.dashboard = dashboard;
        this.data = new Database();
    }

    public void log_out() {

        dashboard.logoutButton.addActionListener(e -> {

            if (data.logout()) {

                dashboard.dispose();

                Dashboard.start();

            } else {

                System.out.println(
                    "Logout failed!"
                );
            }
        });
    }
}