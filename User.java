public class User {
    private final String name;
    private final String plate;

    public User(String name, String plate) {
        this.name = name;
        this.plate = plate;
    }

    public String getName() { return name; }
    public String getPlate() { return plate; }

    @Override public String toString() { return name; }
}
