import java.util.*;

public class Seller extends User {
    private List<String> products = new ArrayList<>();

    public Seller(String name, String email, String password, String phone, String address) {
        super(name, email, password, phone, address);
        products.addAll(DB.getListings(email)); // load from DB on login
    }

    public void addProduct(String product) {
        products.add(product);
        DB.addListing(email, product);
    }

    public List<String> getProducts() { return products; }

    @Override public String getRole() { return "Seller"; }

    @Override public void getDashboard() {
        System.out.println("Seller Dashboard - Products: " + products.size());
    }
}