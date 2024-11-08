public class Main {

    public static void showMenu() {
        System.out.println("===== Manage Posts and Users =====");
        String[] options = {
                "1. Add User", "2. Update User", "3. Delete User", "4. Show User",
                "5. Add Post", "6. Update Post", "7. Delete Post", "8. Show Post", "9. Exit"
        };

        //Draw menu table
        int width = 33; // Total width of the menu line
        for (String option : options) {
            int padding = (width - option.length()) / 2;
            String format = "|%" + padding + "s%s%" + (padding + (width - option.length()) % 2) + "s|\n";
            System.out.printf(format, "", option, "");
        }
        System.out.println("==================================");
    }

    public static void main(String[] args) {
        Manage mn = new Manage();
        while (true){
            // Show menu
            showMenu();

            // Choice from menu
            int choice = mn.getChoice();

            switch (choice) {
                case 1:
                    mn.addUser();
                    break;
                case 2:
                    mn.updateUser();
                    break;
                case 3:
                    mn.deleteUser();
                    break;
                case 4:
                    mn.showUsers();
                    break;
                case 5:
                    mn.addPost();
                    break;
                case 6:
                    mn.updatePost();
                    break;
                case 7:
                    mn.deletePost();
                    break;
                case 8:
                    mn.showPosts();
                    break;
                case 9:
                    System.exit(0);
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}