import java.io.*;
import java.util.*;

public class DB {
    private static final String FILE = "buysell_data.ser";

    private static Map<String, Map<String, List<String>>> loadAll() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                Map<?, ?> raw = (Map<?, ?>) obj;
                Map<String, Map<String, List<String>>> result = new HashMap<>();
                for (Map.Entry<?, ?> e : raw.entrySet()) {
                    if (e.getKey() instanceof String && e.getValue() instanceof Map) {
                        Map<String, List<String>> inner = new HashMap<>();
                        Map<?, ?> rawInner = (Map<?, ?>) e.getValue();
                        for (Map.Entry<?, ?> ie : rawInner.entrySet()) {
                            if (ie.getKey() instanceof String && ie.getValue() instanceof List) {
                                List<String> items = new ArrayList<>();
                                for (Object item : (List<?>) ie.getValue()) {
                                    if (item instanceof String) items.add((String) item);
                                }
                                inner.put((String) ie.getKey(), items);
                            }
                        }
                        result.put((String) e.getKey(), inner);
                    }
                }
                return result;
            }
        } catch (Exception e) { /* first run or corrupt */ }
        return new HashMap<>();
    }

    private static void saveAll(Map<String, Map<String, List<String>>> all) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(all);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void init() { /* no setup needed */ }

    public static void addCartItem(String email, String item) {
        Map<String, Map<String, List<String>>> all = loadAll();
        all.computeIfAbsent("cart", k -> new HashMap<>())
           .computeIfAbsent(email, k -> new ArrayList<>()).add(item);
        saveAll(all);
    }

    public static void addListing(String email, String product) {
        Map<String, Map<String, List<String>>> all = loadAll();
        all.computeIfAbsent("listings", k -> new HashMap<>())
           .computeIfAbsent(email, k -> new ArrayList<>()).add(product);
        saveAll(all);
    }

    public static List<String> getCart(String email) {
        Map<String, List<String>> cart = loadAll().get("cart");
        return cart != null ? cart.getOrDefault(email, new ArrayList<>()) : new ArrayList<>();
    }

    public static List<String> getListings(String email) {
        Map<String, List<String>> listings = loadAll().get("listings");
        return listings != null ? listings.getOrDefault(email, new ArrayList<>()) : new ArrayList<>();
    }
}