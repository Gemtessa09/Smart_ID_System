import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_id_db?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void createTable() {
        // SQL statement to create the table
        String sql = "CREATE TABLE table1 (" +
                "reg_number VARCHAR(50) PRIMARY KEY," +
                "full_name VARCHAR(100)," +
                "date_of_birth VARCHAR(10)," +
                "birth_place VARCHAR(100)," +
                "nationality VARCHAR(50)," +
                "region VARCHAR(50)," +
                "zone VARCHAR(50)," +
                "phone_number VARCHAR(20)," +
                "sex VARCHAR(10)," +
                "issue_date VARCHAR(10)," +
                "expiry_date VARCHAR(10)," +
                "photo_path VARCHAR(255)" +
                ")";

        try {
            // Establish connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();

            // Execute the create table statement
            stmt.executeUpdate(sql);
            System.out.println("Table 'table1' created successfully!");

            // Close resources
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
            // If table already exists, it will throw an exception
            if (e.getErrorCode() == 1050) {
                System.out.println("Table 'table1' already exists in the database.");
            }
        }
    }

    public static void main(String[] args) {
        // Make sure the database 'smart_id_db' exists before running
        createTable();
    }
}