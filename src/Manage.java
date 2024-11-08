import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Manage {
    private final List<User> listUser = new ArrayList<>();
    private final List<Post> listPost = new ArrayList<>();
    private final Scanner sc = new Scanner(System.in);

    public int getChoice() {
        System.out.print("Enter your choice: ");
        return sc.nextInt();
    }

    public void addUser() {
        User user = new User(getIntInput("Enter user's ID: "), getStringInput("Enter Name: "), getStringInput("Enter Hometown: "), getIntInput("Enter Age: "));
        listUser.add(user);
        sortUsersById();
        System.out.println("User added successfully!");
    }

    public void updateUser() {
        int id = getIntInput("Enter user's ID: ");
        User user = findUserById(id);
        if (user != null) {
            choiceUpdateUsers();
            int choice = getChoice();
            switch (choice) {
                case 1 -> user.setName(getStringInput("Enter new Name: "));
                case 2 -> user.setHometown(getStringInput("Enter new Hometown: "));
                case 3 -> user.setAge(getIntInput("Enter new Age: "));
                case 4 -> {
                    user.setName(getStringInput("Enter new Name: "));
                    user.setHometown(getStringInput("Enter new Hometown: "));
                    user.setAge(getIntInput("Enter new Age: "));
                }
                default -> System.out.println("Invalid choice!");
            }
            System.out.println("User updated successfully!");
        } else {
            System.out.println("User not found!");
        }
    }

    public void deleteUser() {
        int id = getIntInput("Enter user's ID: ");
        listUser.removeIf(user -> user.getId() == id);
        listPost.removeIf(post -> post.getAuthor().getId() == id);
        System.out.println("User deleted successfully!");
    }

    public void addPost() {
        int id = getIntInput("Enter post's ID: ");
        String title = getStringInput("Enter Title: ");
        String content = getStringInput("Enter Content: ");
        int authorId = getIntInput("Enter Author's ID: ");
        String date = getStringInput("Enter Date: ");
        User author = findUserById(authorId);
        if (author != null) {
            listPost.add(new Post(id, title, content, author, date));
            sortPostsById();
            System.out.println("Post added successfully!");
        } else {
            System.out.println("Author not found!");
        }
    }

    public void updatePost() {
        int postId = getIntInput("Enter post's ID: ");
        int authorId = getIntInput("Enter author's ID: ");
        Post post = findPostById(postId);
        if (post != null && post.getAuthor().getId() == authorId) {
            choiceUpdatePost();
            int choice = getChoice();
            switch (choice) {
                case 1 -> post.setTitle(getStringInput("Enter new Title: "));
                case 2 -> post.setContent(getStringInput("Enter new Content: "));
                case 3 -> post.setDate(getStringInput("Enter new Date: "));
                case 4 -> {
                    post.setTitle(getStringInput("Enter new Title: "));
                    post.setContent(getStringInput("Enter new Content: "));
                    post.setDate(getStringInput("Enter new Date: "));
                }
                default -> System.out.println("Invalid choice!");
            }
            System.out.println("Post updated successfully!");
        } else {
            System.out.println("Post or Author not found!");
        }
    }

    public void deletePost() {
        int postId = getIntInput("Enter post's ID: ");
        listPost.removeIf(post -> post.getId() == postId);
        System.out.println("Post deleted successfully!");
    }

    public void showUsers() {
        if (listUser.isEmpty()) {
            System.out.println("No users to display.");
            return;
        }
        sortUsersById();
        //Draw table with columns: ID, Name, Hometown, Age
        printTable(new String[]{"ID", "Name", "Hometown", "Age"}, listUser.stream()
                .map(user -> new String[]{String.valueOf(user.getId()), user.getName(), user.getHometown(), String.valueOf(user.getAge())})
                .collect(Collectors.toList()));
    }

    public void showPosts() {
        if (listPost.isEmpty()) {
            System.out.println("No posts to display.");
            return;
        }
        sortPostsById();

        //Draw table with columns: ID, Title, Content, Author, Date
        printTable(new String[]{"ID", "Title", "Content", "Author", "Date"}, listPost.stream()
                .map(post -> new String[]{String.valueOf(post.getId()), post.getTitle(), post.getContent(), post.getAuthor().getName(), post.getDate()})
                .collect(Collectors.toList()));
    }

    private void sortUsersById() {
        listUser.sort(Comparator.comparingInt(User::getId));
    }

    private void sortPostsById() {
        listPost.sort(Comparator.comparingInt(Post::getId));
    }

    private void printTable(String[] headers, List<String[]> rows) {
        int[] columnWidths = Arrays.stream(headers).mapToInt(String::length).toArray();
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        printRow(headers, columnWidths);
        printSeparator(columnWidths);
        rows.forEach(row -> printRow(row, columnWidths));
    }

    private void printRow(String[] row, int[] columnWidths) {
        System.out.println("| " + IntStream.range(0, row.length)
                .mapToObj(i -> padRight(row[i], columnWidths[i]))
                .collect(Collectors.joining(" | ")) + " |");
    }

    private void printSeparator(int[] columnWidths) {
        System.out.println("+" + Arrays.stream(columnWidths)
                .mapToObj(width -> "-".repeat(width + 2))
                .collect(Collectors.joining("+")) + "+");
    }

    private String padRight(String text, int length) {
        return String.format("%-" + length + "s", text);
    }

    private String getStringInput(String prompt) {
        System.out.println(prompt);
        return sc.nextLine();
    }

    private int getIntInput(String prompt) {
        System.out.println(prompt);
        int input = sc.nextInt();
        sc.nextLine(); // consume newline
        return input;
    }

    private User findUserById(int id) {
        return listUser.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    private Post findPostById(int id) {
        return listPost.stream().filter(post -> post.getId() == id).findFirst().orElse(null);
    }

    public static void choiceUpdateUsers() {
        System.out.println("1. Update Name\n2. Update Hometown\n3. Update Age\n4. Update All\nEnter your choice: ");
    }

    public static void choiceUpdatePost() {
        System.out.println("1. Update Title\n2. Update Content\n3. Update Date\n4. Update All\nEnter your choice: ");
    }
}