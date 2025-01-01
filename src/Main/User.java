package Main;

public class User {
    private final int id;
    private String name;
    private String hometown;
    private int age;
    private String role;

    public User(int id, String name, String hometown, int age, String role) {
        this.id = id;
        this.name = name;
        this.hometown = hometown;
        this.age = age;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}