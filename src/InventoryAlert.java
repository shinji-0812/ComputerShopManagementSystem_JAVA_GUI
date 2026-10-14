public class InventoryAlert {
    String productName;
    String category;
    int quantity;
    boolean outOfStock;
    public InventoryAlert(String productName,
        String category,
        int quantity,
        boolean outOfStock)
    {
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.outOfStock = outOfStock;

    }
}
