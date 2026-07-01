import java.util.ArrayList;
import java.util.List;

public class Database {

    // ----tables-----
    private static final List<String> shop     = new ArrayList<>();
    private static final List<String> cart     = new ArrayList<>();
    private static final List<String> listings = new ArrayList<>();
    private static final List<String> sold     = new ArrayList<>();

    // ----shop-----
   public static List<String> getShop() { 
    return shop; 
   } 
  public static void addToShop(String item) { 
    shop.add(item); 
   }
  public static void removeFromShop(int i) { 
    shop.remove(i); 
   }

    // -----cart-----
  public static List<String> getCart() {
    return cart;
}
  public static boolean addToCart(String item) {
    if (cart.contains(item)) return false;
    cart.add(item);
    return true;
}
    public static void removeFromCart(int i) {
    cart.remove(i);
}

   public static void clearCart() {
    cart.clear();
}

// ----- listings -----
   public static List<String> getListings() {
    return listings;
}

  public static void addListing(String item) {
    listings.add(item);
}

  public static void removeListing(int i) {
    listings.remove(i);
}

// ----- sold -----
   public static List<String> getSold() {
    return sold;
}

  public static void markSold(int i) {
    sold.add(listings.remove(i));
}

   public static void restoreListing(int i) {
    listings.add(sold.remove(i));
}

// ----- session reset -----
    public static void reset() {
    shop.clear();
    cart.clear();
    listings.clear();
    sold.clear();
}

    public static void saveToFile() {
    try {
        java.io.PrintWriter w = new java.io.PrintWriter("data.txt");
        w.println("SHOP: " + shop);
        w.println("CART: " + cart);
        w.println("LISTINGS: " + listings);
        w.println("SOLD: " + sold);
        w.close();
        System.out.println("Data saved to data.txt");
    } catch (Exception e) {
        System.out.println("Save failed: " + e.getMessage());
    }
}
}
