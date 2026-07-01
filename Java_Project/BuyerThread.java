public class BuyerThread extends Thread {
    private Buyer buyer;
    private String item;

    public BuyerThread(Buyer buyer, String item) {
        this.buyer = buyer;
        this.item = item;
    }

    public void run() {
        try {
            Thread.sleep(500);
            buyer.addtoCart(item);
        } catch (Exception e) { System.out.println(e); }
    }
}