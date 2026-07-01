public class SellerThread extends Thread {
    private Seller seller;
    private String product;

    public SellerThread(Seller seller, String product) {
        this.seller = seller;
        this.product = product;
    }

    public void run() {
        try {
            Thread.sleep(500);
            seller.addProduct(product);
        } catch (Exception e) { System.out.println(e); }
    }
}