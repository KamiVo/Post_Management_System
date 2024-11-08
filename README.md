# User and Post Management System

## Description
A Java-based application for managing users and posts, including functionalities to add, update, delete, and display users and posts. The application features a console-based menu for easy navigation and interaction.

## Features
- Add, update, delete, and display users.
- Add, update, delete, and display posts.
- Console-based menu for user interaction.
- Data sorting for users and posts.

## Requirements
- Java 8 or higher
- IntelliJ IDEA or any other Java IDE

## Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/KamiVo/user-post-management-system.git
    ```
2. Open the project in IntelliJ IDEA.
3. Build the project to resolve dependencies.

## Usage
1. Run the `Main` class.
2. Follow the console menu to manage users and posts.

## Classes
- `Main`: Contains the main method and displays the menu.
- `Manage`: Handles the operations for managing users and posts.
- `User`: Represents a user with attributes like ID, name, hometown, and age.
- `Post`: Represents a post with attributes like ID, title, content, author, and date.

## Methods
### Manage Class
- `getChoice()`: Gets the user's menu choice.
- `addUser()`: Adds a new user.
- `updateUser()`: Updates an existing user.
- `deleteUser()`: Deletes a user.
- `addPost()`: Adds a new post.
- `updatePost()`: Updates an existing post.
- `deletePost()`: Deletes a post.
- `showUsers()`: Displays all users.
- `showPosts()`: Displays all posts.

## License
This project is licensed under the MIT License.
