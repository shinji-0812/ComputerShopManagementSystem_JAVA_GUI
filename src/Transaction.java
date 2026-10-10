import java.util.Date;
    
    public class Transaction {

        String id;

        String customer;

        String product;

        int quantity;

        double total;

        String payment;

        String status;

        Date date;



        Transaction(

            String id, String customer, String product,

            int quantity, double total, String payment,

            String status, Date date

        ) {

            this.id = id;

            this.customer = customer;

            this.product = product;

            this.quantity = quantity;

            this.total = total;

            this.payment = payment;

            this.status = status;

            this.date = date;

        }

    }
