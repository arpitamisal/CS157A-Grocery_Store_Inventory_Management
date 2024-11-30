import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Database {

    private static final String DB_URL = "jdbc:mysql://localhost/grocInventory";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Method to establish database connection
    public static Connection connectDb() {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
            return null;
        }
    }

    // Method to validate user login
    public static boolean signIn(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = connectDb();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            if (connection == null) {
                System.out.println("Unable to establish a database connection.");
                return false;
            }

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login Successful!");
                    return true;
                } else {
                    System.out.println("Invalid username or password.");
                    return false;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error executing login query.");
            e.printStackTrace();
            return false;
        }
    }

    // Main method to run the sign-in process
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (signIn(username, password)) {
                System.out.println("Welcome to the system!");
            } else {
                System.out.println("Please try again.");
            }
        }
    }
}
