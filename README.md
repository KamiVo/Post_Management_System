# ✨ User and Post Management System ✨

This project is a **User and Post Management System** built using Java and Swing for the GUI, with MySQL as the database. It allows administrators to manage users and posts, including adding, editing, viewing, and deleting users and posts.

---

## Table of Contents

1. [🎨 Features](#-features)
2. [📝 Installation](#-installation)
3. [🔧 Usage](#-usage)
4. [🛠️ Requirements](#-requirements)
5. [📊 Database Setup](#-database-setup)
6. [🗂 Project Structure](#-project-structure)
7. [📊 Classes and Methods](#-classes-and-methods)
8. [⚖️ License](#-license)

---

## 🎨 Features

- **User Authentication**: Login and registration functionality.
- **User Management**: Add, edit, view, and delete users.
- **Post Management**: Add, edit, view, and delete posts.
- **Role-Based Access Control**: Admin and user roles for access restrictions.
- **User-Friendly GUI**: Built with Java Swing for an interactive interface.

---

## 📝 Installation

### Step 1: Clone the Repository
```sh
git clone https://github.com/yourusername/user-post-management.git
cd user-post-management
```

### Step 2: Configure the Database
Set up the MySQL database and update the connection details in the `Main.LoginRegisterGUI` class.

### Step 3: Open the Project
Open the project in IntelliJ IDEA or your preferred Java IDE.

### Step 4: Build the Project
```sh
mvn clean install
```

---

## 🔧 Usage

1. Run the `Main.LoginRegisterGUI` class to launch the application.
2. Register a new user or log in with an existing user account.
3. Use the dashboard to navigate between user and post management functionalities.

---

## 🛠️ Requirements

- ☕ Java 8 or higher
- 🖥️ IntelliJ IDEA or any other Java IDE
- 🗄️ MySQL Database

---

## 📊 Database Setup

1. Create a MySQL database named `user_management`:
    ```sql
    CREATE DATABASE user_management;
    ```

2. Create the necessary tables:
    ```sql
    CREATE TABLE roles (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(50) NOT NULL
    );

    CREATE TABLE user (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) NOT NULL,
        email VARCHAR(50),
        password VARCHAR(50) NOT NULL,
        role_id INT,
        FOREIGN KEY (role_id) REFERENCES roles(id)
    );

    CREATE TABLE user_details (
        id INT PRIMARY KEY,
        hometown VARCHAR(50),
        age INT,
        FOREIGN KEY (id) REFERENCES user(id)
    );

    CREATE TABLE posts (
        id INT AUTO_INCREMENT PRIMARY KEY,
        title VARCHAR(100) NOT NULL,
        content TEXT NOT NULL,
        author_id INT,
        date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (author_id) REFERENCES user(id)
    );

    INSERT INTO roles (name) VALUES ('admin'), ('user');
    ```

3. Update the database connection details in the `Main.LoginRegisterGUI` class:
    ```java
    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_management", "(your username)", "(your password)");
    ```
    
---

## 🗂 Project Structure

### `src/Function/`
- **`addPost.java`**: Handles adding new posts.
- **`addUser.java`**: Handles adding new users.
- **`deletePost.java`**: Handles deleting posts.
- **`deleteUser.java`**: Handles deleting users.
- **`editPost.java`**: Handles editing post details.
- **`editUser.java`**: Handles editing user details.
- **`viewPost.java`**: Handles viewing posts.
- **`viewUser.java`**: Handles viewing users.

### `src/GUI/`
- **`ManagePostsGUI.java`**: GUI for managing posts.
- **`ManageUsersGUI.java`**: GUI for managing users.

### `src/Main/`
- **`Database.java`**: Manages database connections.
- **`LoginRegisterGUI.java`**: GUI for login and registration.
- **`MainDashboardGUI.java`**: Main dashboard GUI.
- **`Post.java`**: Post model class.
- **`User.java`**: User model class.
- **`UserRole.java`**: Handles user roles.

---

## 📊 Classes and Methods

### `Function.addPost`
- **`addPost(String title, String content, int authorId)`**: Adds a new post.
- **`isPostExists(Connection connection, String title)`**: Checks if a post with the same title exists.
- **`addPostToPostTable(Connection connection, String title, String content, int authorId)`**: Inserts a new post into the database.

### `Function.addUser`
- **`addUser(String username, String hometown, int age)`**: Adds a new user.
- **`isUsernameExists(Connection connection, String username)`**: Checks if a username already exists.
- **`addUserToUserTable(Connection connection, String username)`**: Inserts a new user into the user table.
- **`addUserToUserDetailsTable(Connection connection, int id, String hometown, int age)`**: Inserts user details into the `user_details` table.

### `Function.deleteUser`
- **`deleteUser(int id, boolean deleteAge, boolean deleteHometown, boolean deleteAll)`**: Deletes a user or specific details.
- **`isUserAdmin(int userId)`**: Checks if a user is an admin.
- **`isUserExist(int userId)`**: Checks if a user exists.

### `Function.editUser`
- **`editUser(int id, String newName, String newHometown, Integer newAge, boolean editAll)`**: Edits user details.
- **`isUserAdmin(int userId)`**: Checks if a user is an admin.
- **`isUserExist(int userId)`**: Checks if a user exists.

### `Function.viewPost`
- **`viewPost(int authorId)`**: Constructor to initialize the view.
- **`getPosts()`**: Retrieves posts from the database.

### `Function.viewUser`
- **`viewUser()`**: Constructor to initialize the view.
- **`getUsers()`**: Retrieves users from the database.

### `GUI.ManagePostsGUI`
- GUI for managing posts.

### `GUI.ManageUsersGUI`
- GUI for managing users.

### `Main.Database`
- **`getConnection()`**: Returns a database connection.

### `Main.LoginRegisterGUI`
- GUI for login and registration.
- **`initializeDBConnection()`**: Initializes the database connection.
- **`authenticateUser(String identifier, String password)`**: Authenticates a user.
- **`registerUser(String identifier, String password)`**: Registers a new user.

### `Main.MainDashboardGUI`
- Main dashboard GUI.

### `Main.Post`
- Post model class.

### `Main.User`
- User model class.

### `Main.UserRole`
- **`getUserRole(String username)`**: Retrieves the role of a user.

---

## ⚖️ License

This project is licensed under the [MIT License](LICENSE).

---

<img src="https://media.giphy.com/media/3oriO0OEd9QIDdllqo/giphy.gif" autoplay loop>
