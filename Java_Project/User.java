public abstract class User {
    protected String name, email, password, phone, address;

    public User(String name, String email, String password, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public void login(String email, String password) {
        System.out.println(name + " logged in as " + getRole());
    }

    public void logout() {
        System.out.println(name + " logged out.");
    }

    public abstract String getRole();
    public abstract void getDashboard();
}