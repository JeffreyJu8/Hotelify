import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;

import java.util.concurrent.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.*;


@WebServlet("/multithreading")
public class multithreading extends HttpServlet{
	private static final String DATABASE_URL = "jdbc:mysql://localhost/mydb";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "mysql";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    
    
   
    
    // "Fix this get response and make it compatible with the getRecentHotel() function" 21 lines, ChatGPT, 11/29/23 edition. 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ExecutorService executorService = Executors.newFixedThreadPool(10); // Create a thread pool

        try {
            Future<String> awaited_str = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return getRecentHotel(); // Perform the database operation in this callable
                }
            });

            String hotelData = awaited_str.get(); // Retrieve the result once the task is complete

            response.setContentType("application/json");
            response.getWriter().write(hotelData);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions properly, maybe send an error response
        } finally {
            executorService.shutdown(); // Shutdown the executor service
        }
    }
    

    public String getRecentHotel() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        PreparedStatement st1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        
        

        Map<String, Object> hotelData = new HashMap<>();

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);

            String query = "SELECT hotelID FROM reviews WHERE reviewDate = (SELECT MAX(reviewDate) FROM reviews);";            
            st = conn.prepareStatement(query);
            rs = st.executeQuery();

            String hotelID = "";
            if (rs.next()) {
                hotelID = rs.getString("hotelID");
                hotelData.put("hotelID", hotelID);
            }
            
            
          
            
            String query2 = "SELECT hotelName FROM reviews WHERE hotelID = '" + hotelID + "';";
            st1 = conn.prepareStatement(query2);
            rs1 = st1.executeQuery();
            System.out.println("rs1 " + rs1);
            
            if(rs1.next()) {
            	System.out.println("enters");
            	System.out.println( rs1.getString("hotelName"));
            	String hotelName = rs1.getString("hotelName");
            	hotelData.put("hotelName", hotelName);
            }
          
            
        } finally {
            // Close resources
            if (rs != null) {
                rs.close();
            }
            if (rs1 != null) {
                rs1.close();
            }
            if (st != null) {
                st.close();
            }
            if (st1 != null) {
            	st1.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        // Convert hotel data to JSON format
        Gson gson = new Gson();
        return gson.toJson(hotelData);
    }
}


