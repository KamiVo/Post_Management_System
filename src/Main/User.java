package Main;

public class User {
    private final int id;
    private String name;
    private String hometown;
    private int age;

    public User(int id, String name, String hometown, int age) {
        this.id = id;
        this.name = name;
        this.hometown = hometown;
        this.age = age;
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

}
