import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class JDBCConnector {

    // Database URL and credentials
    private static final String DATABASE_URL = "jdbc:mysql://localhost/mydb";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "mysql";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Inserts a new user into the database.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @param email    the email of the new user
     * @throws ClassNotFoundException if the JDBC driver is not found
     * @throws SQLException           if a database access error occurs
     */
    public void insertNewUser(String username, String password, String email) throws ClassNotFoundException, SQLException, NoSuchAlgorithmException {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            // Load JDBC driver
            Class.forName(JDBC_DRIVER);

            // Establish connection to the database
            conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

            // Create SQL query
            String query = "INSERT INTO users (Username, Password, Email) VALUES (?, ?, ?)";
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String encodedSalt = Base64.getEncoder().encodeToString(salt);

            // Hash the password with SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String encodedHash = Base64.getEncoder().encodeToString(hashedPassword);
            // Prepare statement
            st = conn.prepareStatement(query);

            // Set parameters
            st.setString(1, username);
            st.setString(2, encodedHash + ":" + encodedSalt); // Store the hashed password and salt
            st.setString(3, email);

            // Execute update
            st.executeUpdate();
        } finally {
            // Close resources
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    public int registerUser(String username, String password, String email) {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        ResultSet rs = null;

        try {
            // Load JDBC driver and establish database connection
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

            // Check if the username exists
            String checkUser = "SELECT COUNT(*) FROM users WHERE Username = ?";
            checkStmt = conn.prepareStatement(checkUser);
            checkStmt.setString(1, username);
            rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return -1; // Username exists
            }

            // Check if the email exists
            String checkEmail = "SELECT COUNT(*) FROM users WHERE Email = ?";
            checkStmt = conn.prepareStatement(checkEmail);
            checkStmt.setString(1, email);
            rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return -2; // Email exists
            }

            // If username and email do not exist, insert the new user
            try {
				insertNewUser(username, password, email);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Retrieve the last inserted ID
            String getLastInsertedId = "SELECT LAST_INSERT_ID()";
            checkStmt = conn.prepareStatement(getLastInsertedId);
            rs = checkStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // Return the new user ID
            }

            return 0; // Placeholder for any other failure, should be handled appropriately
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0; // Return 0 if registration is unsuccessful due to unexpected issues
    }
    
    public int authenticateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
        	Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

            // Adjusted SQL query to also select the user ID
            String sql = "SELECT userID, password FROM users WHERE LOWER(username) = LOWER(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username.toLowerCase());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                String[] parts = rs.getString("password").split(":");
                String storedHash = parts[0];
                String storedSalt = parts[1];

                // Hash the input password using the stored salt
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(Base64.getDecoder().decode(storedSalt));
                byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
                String encodedHash = Base64.getEncoder().encodeToString(hashedPassword);

                if (encodedHash.equals(storedHash)) {
                    // Return user ID upon successful authentication
                    return rs.getInt("userID");
                } else {
                    return -2; // Password mismatch
                }
            } else {
                return -1; // Username not found
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -3; // Database or other exception
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Main method for demonstration purposes.
     *
     * @param args command line arguments
     */
//    public static void main(String[] args) {
//        JDBCConnector connector = new JDBCConnector();
//
//        try {
//            // Insert a new user (example)
//            connector.insertNewUser("newuser", "password123", "newuser@example.com");
//            System.out.println("User inserted successfully!");
//        } catch (ClassNotFoundException e) {
//            System.out.println("JDBC Driver not found: " + e.getMessage());
//        } catch (SQLException e) {
//            System.out.println("SQL Exception: " + e.getMessage());
//        }
//    }
}
