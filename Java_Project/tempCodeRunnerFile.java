import java.awt.*;
  import java.util.ArrayList;
  import java.util.List;
  import javax.swing.*;
  import javax.swing.border.*;

  public class Main {
      static final Color BG = new Color(0xFAF7F2);
      static final Color ORANGE = new Color(0xFF6B35);
      static final Color GREEN = new Color(0x27AE60);
      static final Color RED = new Color(0xE74C3C);

      static JFrame frame;

      // simple in-memory lists
      static List<String> shop = new ArrayList<>();
      static List<String> cart = new ArrayList<>();
      static List<String> listings = new ArrayList<>();
      static List<String> sold = new ArrayList<>();

      public static void main(String[] args) {
          frame = new JFrame("BuySell Market");
          frame.setSize(440, 580);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setLocationRelativeTo(null);
          showLogin();
          frame.setVisible(true);
      }

      // ----- LOGIN -----
      static void showLogin() {
          JPanel p = new JPanel();
          p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
          p.setBackground(BG);
          p.setBorder(new EmptyBorder(30, 30, 30, 30));

          JLabel logo = new JLabel("BuySell Market");
          logo.setFont(new Font("SansSerif", Font.BOLD, 22));
          logo.setForeground(ORANGE);
          logo.setAlignmentX(Component.CENTER_ALIGNMENT);

          JLabel tag = new JLabel("Buy smarter. Sell faster.");
          tag.setForeground(Color.GRAY);
          tag.setAlignmentX(Component.CENTER_ALIGNMENT);

          JComboBox<String> role = new JComboBox<>(new String[]{"Buyer", "Seller"});
          JTextField name = new JTextField();
          JTextField email = new JTextField();
          JPasswordField pass = new JPasswordField();
          JTextField phone = new JTextField();
          JTextField addr = new JTextField();

          JButton btn = new JButton("Enter Market");
          btn.setBackground(ORANGE);
          btn.setForeground(Color.WHITE);
          btn.setFocusPainted(false);
          btn.setAlignmentX(Component.CENTER_ALIGNMENT);

          btn.addActionListener(e -> {
              if (name.getText().trim().isEmpty()
                      || email.getText().trim().isEmpty()
                      || pass.getPassword().length == 0) {
                  JOptionPane.showMessageDialog(frame, "Please fill name, email and password.");
                  return;
              }
              // reset session data
              shop.clear(); cart.clear(); listings.clear(); sold.clear();
              String n = name.getText().trim();
              if (role.getSelectedIndex() == 0) buyerHome(n);
              else sellerHome(n);
          });

          p.add(logo);
          p.add(Box.createVerticalStrut(5));
          p.add(tag);
          p.add(Box.createVerticalStrut(20));
          p.add(field("Role:", role));
          p.add(field("Name:", name));
          p.add(field("Email:", email));
          p.add(field("Password:", pass));
          p.add(field("Phone:", phone));
          p.add(field("Address:", addr));
          p.add(Box.createVerticalStrut(15));
          p.add(btn);

          setScreen(p);
      }

      static JPanel field(String label, JComponent c) {
          JPanel row = new JPanel(new BorderLayout(8, 0));
          row.setBackground(BG);
          row.setBorder(new EmptyBorder(5, 0, 5, 0));
          row.setMaximumSize(new Dimension(400, 40));
          JLabel l = new JLabel(label);
          l.setPreferredSize(new Dimension(80, 25));
          row.add(l, BorderLayout.WEST);
          row.add(c, BorderLayout.CENTER);
          return row;
      }

      // ----- BUYER -----
      static void buyerHome(String name) {
          JPanel root = new JPanel(new BorderLayout(0, 10));
          root.setBackground(BG);
          root.setBorder(new EmptyBorder(15, 20, 15, 20));
          root.add(header("Buyer: " + name), BorderLayout.NORTH);

          JTabbedPane tabs = new JTabbedPane();
          tabs.addTab("Shop", buildShop());
          tabs.addTab("My Cart", buildCart());
          tabs.addChangeListener(e -> {
              int i = tabs.getSelectedIndex();
              tabs.setComponentAt(i, i == 0 ? buildShop() : buildCart());
          });

          root.add(tabs, BorderLayout.CENTER);
          setScreen(root);
      }

      static JPanel buildShop() {
          DefaultListModel<String> model = new DefaultListModel<>();
          for (String s : shop) model.addElement(s);
          JList<String> list = new JList<>(model);

          JTextField input = new JTextField();
          JButton add = new JButton("Add");
          add.addActionListener(e -> {
              String t = input.getText().trim();
              if (t.isEmpty()) return;
              shop.add(t);
              model.addElement(t);
              input.setText("");
          });

          JButton remove = colored(new JButton("Remove"), RED);
          JButton toCart = colored(new JButton("Add to Cart"), ORANGE);

          remove.addActionListener(e -> {
              int i = list.getSelectedIndex();
              if (i < 0) { JOptionPane.showMessageDialog(frame, "Select an item first!"); return; }
              shop.remove(i);
              model.remove(i);
          });

          toCart.addActionListener(e -> {
              String s = list.getSelectedValue();
              if (s == null) { JOptionPane.showMessageDialog(frame, "Select an item!"); return; }
              if (cart.contains(s)) {
                  JOptionPane.showMessageDialog(frame, "Already in cart.");
              } else {
                  cart.add(s);
                  JOptionPane.showMessageDialog(frame, "Added to cart.");
              }
          });

          return assemble(list, input, add, remove, toCart);
      }

      static JPanel buildCart() {
          DefaultListModel<String> model = new DefaultListModel<>();
          for (String s : cart) model.addElement(s);
          JList<String> list = new JList<>(model);

          JLabel status = new JLabel(cartStatus());

          JButton remove = new JButton("Remove");
          JButton clear = new JButton("Clear");
          JButton checkout = colored(new JButton("Checkout"), ORANGE);

          remove.addActionListener(e -> {
              int i = list.getSelectedIndex();
              if (i < 0) return;
              cart.remove(i);
              model.remove(i);
              status.setText(cartStatus());
          });
          clear.addActionListener(e -> {
              cart.clear();
              model.clear();
              status.setText(cartStatus());
          });
          checkout.addActionListener(e -> {
              if (cart.isEmpty()) { JOptionPane.showMessageDialog(frame, "Cart is empty!"); return; }
              JOptionPane.showMessageDialog(frame, "Order placed for " + cart.size() + " item(s)!");
              cart.clear();
              model.clear();
              status.setText(cartStatus());
          });

          JPanel btns = new JPanel(new GridLayout(1, 3, 5, 0));
          btns.setBackground(BG);
          btns.add(remove); btns.add(clear); btns.add(checkout);

          JPanel bottom = new JPanel(new BorderLayout(0, 5));
          bottom.setBackground(BG);
          bottom.add(status, BorderLayout.NORTH);
          bottom.add(btns, BorderLayout.CENTER);

          JPanel p = new JPanel(new BorderLayout(5, 5));
          p.setBackground(BG);
          p.add(new JScrollPane(list), BorderLayout.CENTER);
          p.add(bottom, BorderLayout.SOUTH);
          return p;
      }

      static String cartStatus() {
          return cart.isEmpty() ? "Cart is empty" : cart.size() + " item(s) in cart";
      }

      // ----- SELLER -----
      static void sellerHome(String name) {
          JPanel root = new JPanel(new BorderLayout(0, 10));
          root.setBackground(BG);
          root.setBorder(new EmptyBorder(15, 20, 15, 20));
          root.add(header("Seller: " + name), BorderLayout.NORTH);

          JTabbedPane tabs = new JTabbedPane();
          tabs.addTab("My Listings", buildListings());
          tabs.addTab("Sold", buildSold());
          tabs.addChangeListener(e -> {
              int i = tabs.getSelectedIndex();
              tabs.setComponentAt(i, i == 0 ? buildListings() : buildSold());
          });

          root.add(tabs, BorderLayout.CENTER);
          setScreen(root);
      }

      static JPanel buildListings() {
          DefaultListModel<String> model = new DefaultListModel<>();
          for (String s : listings) model.addElement(s);
          JList<String> list = new JList<>(model);

          JTextField input = new JTextField();
          JButton add = new JButton("Add");
          add.addActionListener(e -> {
              String t = input.getText().trim();
              if (t.isEmpty()) return;
              listings.add(t);
              model.addElement(t);
              input.setText("");
          });

          JButton remove = colored(new JButton("Remove"), RED);
          JButton soldBtn = colored(new JButton("Mark Sold"), GREEN);

          remove.addActionListener(e -> {
              int i = list.getSelectedIndex();
              if (i < 0) return;
              listings.remove(i);
              model.remove(i);
          });
          soldBtn.addActionListener(e -> {
              int i = list.getSelectedIndex();
              if (i < 0) return;
              sold.add(listings.get(i));
              listings.remove(i);
              model.remove(i);
              JOptionPane.showMessageDialog(frame, "Moved to Sold.");
          });

          return assemble(list, input, add, remove, soldBtn);
      }

      static JPanel buildSold() {
          DefaultListModel<String> model = new DefaultListModel<>();
          for (String s : sold) model.addElement(s);
          JList<String> list = new JList<>(model);

          JLabel status = new JLabel(sold.isEmpty() ? "No items sold yet" : sold.size() + " sold");

          JButton restore = new JButton("Restore");
          restore.addActionListener(e -> {
              int i = list.getSelectedIndex();
              if (i < 0) return;
              listings.add(sold.get(i));
              sold.remove(i);
              model.remove(i);
              status.setText(sold.isEmpty() ? "No items sold yet" : sold.size() + " sold");
          });

          JPanel bottom = new JPanel(new BorderLayout(0, 5));
          bottom.setBackground(BG);
          bottom.add(status, BorderLayout.NORTH);
          bottom.add(restore, BorderLayout.CENTER);

          JPanel p = new JPanel(new BorderLayout(5, 5));
          p.setBackground(BG);
          p.add(new JScrollPane(list), BorderLayout.CENTER);
          p.add(bottom, BorderLayout.SOUTH);
          return p;
      }

      // ----- helpers -----
      static JPanel assemble(JList<String> list, JTextField input, JButton add,
                             JButton b1, JButton b2) {
          JPanel top = new JPanel(new BorderLayout(5, 0));
          top.setBackground(BG);
          top.add(input, BorderLayout.CENTER);
          top.add(add, BorderLayout.EAST);

          JPanel btns = new JPanel(new GridLayout(1, 2, 5, 0));
          btns.setBackground(BG);
          btns.add(b1); btns.add(b2);

          JPanel bottom = new JPanel(new BorderLayout(0, 5));
          bottom.setBackground(BG);
          bottom.add(top, BorderLayout.NORTH);
          bottom.add(btns, BorderLayout.SOUTH);

          JPanel p = new JPanel(new BorderLayout(5, 5));
          p.setBackground(BG);
          p.add(new JScrollPane(list), BorderLayout.CENTER);
          p.add(bottom, BorderLayout.SOUTH);
          return p;
      }

      static JButton colored(JButton b, Color c) {
          b.setBackground(c);
          b.setForeground(Color.WHITE);
          b.setFocusPainted(false);
          return b;
      }

      static JPanel header(String title) {
          JPanel p = new JPanel(new BorderLayout());
          p.setBackground(BG);
          JLabel t = new JLabel(title);
          t.setFont(new Font("SansSerif", Font.BOLD, 16));
          JButton out = new JButton("Logout");
          out.setForeground(ORANGE);
          out.setBorderPainted(false);
          out.setContentAreaFilled(false);
          out.addActionListener(e -> showLogin());
          p.add(t, BorderLayout.WEST);
          p.add(out, BorderLayout.EAST);
          return p;
      }

      static void setScreen(JPanel p) {
          frame.setContentPane(p);
          frame.revalidate();
          frame.repaint();
      }
  }