
import java.util.ArrayList;

public class Computes {

    private final Database db;


    public double totalSales = 0;
    public double averageSales = 0;

    public int completedOrders = 0;
    public int productsSold = 0;
    public int canceledOrders = 0;


    public String[] productNames = {
        "Keyboard",
        "Mouse",
        "Audio",
        "Monitor",
        "Graphics Card",
        "CPU",
        "Motherboard",
        "RAM",
        "Storage",
        "Power Supply",
        "Case"
    };


    public double[] productSales = new double[11];


    public int[] finalProductSales = new int[11];


    public int[] topProducts = new int[4];
    public int[] topfour = new int[4];
    public String[] topfourNames = new String[4];

    public Computes(Database db) {
        this.db = db;
    }


    public void compute() {

        totalSales = db.getTotalSales();

        completedOrders = db.getOrdersCount("Completed");
        canceledOrders = db.getOrdersCount("Canceled");

        productsSold = db.productSold();

        averageSales = completedOrders > 0
            ? totalSales / completedOrders
            : 0;

        productSales = new double[productNames.length];
        finalProductSales = new int[productNames.length];


        for (int i = 0; i < productNames.length; i++) {
            productSales[i] =
                db.getProductSales(productNames[i]);
        }


        for (int i = 0; i < productSales.length; i++) {

            if (totalSales > 0) {
                finalProductSales[i] = (int) Math.round(
                    productSales[i] / totalSales * 100.0
                );
            } else {
                finalProductSales[i] = 0;
            }
        }
    }


    public void topSellingProducts() {

        ArrayList<String> names = db.getCompletedProductNames();

        // Reset the results
        topfour = new int[4];
        topfourNames = new String[4];

        int size = names.size();

        int[] quantities = new int[size];
        boolean[] selected = new boolean[size];


        for (int i = 0; i < size; i++) {
            quantities[i] = db.getTopSellingProducts(
                "Completed",
                names.get(i)
            );
        }


        for (int rank = 0; rank < 4; rank++) {

            int maxIndex = -1;

            for (int i = 0; i < size; i++) {

                if (selected[i]) {
                    continue;
                }

                if (quantities[i] <= 0) {
                    continue;
                }

                if (maxIndex == -1 ||
                    quantities[i] > quantities[maxIndex]) {

                    maxIndex = i;
                }
            }

            // No more products with completed sales
            if (maxIndex == -1) {
                break;
            }

            topfourNames[rank] = names.get(maxIndex);
            topfour[rank] = quantities[maxIndex];

            selected[maxIndex] = true;
        }

        // Copy quantities into the four-item display array
        topProducts = topfour.clone();
    }
}
