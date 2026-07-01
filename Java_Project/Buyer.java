import java.util.*;

public class Buyer extends User {
    private List<String> cart = new ArrayList<>();

    public Buyer(String name, String email, String password, String phone, String address) {
        super(name, email, password, phone, address);
        cart.addAll(DB.getCart(email)); // load from DB on login
    }

    public void addtoCart(String item) {
        cart.add(item);
        DB.addCartItem(email, item);
    }

    public List<String> getCart() { return cart; }

    @Override public String getRole() { return "Buyer"; }

    @Override public void getDashboard() {
        System.out.println("Buyer Dashboard - Cart items: " + cart.size());
    }
}