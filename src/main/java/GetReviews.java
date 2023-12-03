

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class GetReviewsHotel
 */
@WebServlet("/get-reviews")
public class GetReviews extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetReviews() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//JDBC info - TODO: change to a real ones
        String url = "jdbc:mysql://localhost:3306/mydb";
        String dbUser = "root";
        String dbPassword = "mysql";
        String driver = "com.mysql.cj.jdbc.Driver";
        String indexType = request.getParameter("index");
        if(indexType.equals("hotel"))
        {
        	String hotelId = request.getParameter("hotelId");
        	try {
        		
        		Class.forName(driver);
        		Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
	            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM reviews WHERE hotelID = ?;");
        		pstmt.setString(1, hotelId);
        		ResultSet rs = pstmt.executeQuery();
        		
			 	JsonArray jArray = new JsonArray();
			 	while(rs.next()) {
			 		JsonObject review = new JsonObject();
			 		review = new JsonObject();
			 		review.addProperty("userID", rs.getString("userID"));
			 		review.addProperty("star_rating", rs.getInt("rating"));
			 		review.addProperty("review", rs.getString("comment"));
			 		review.addProperty("date", rs.getDate("reviewDate").toString());
			 		review.addProperty("cleanliness", rs.getBoolean("clean"));
			 		review.addProperty("pet_friendly", rs.getBoolean("petFriendly"));
			 		review.addProperty("eco_friendly", rs.getBoolean("ecoFriendly"));
			 		review.addProperty("excellent_service", rs.getBoolean("excellentService"));
			 		review.addProperty("dirty", rs.getBoolean("dirty"));
			 		review.addProperty("missing_amenities", rs.getBoolean("missingAmenities"));
			 		review.addProperty("hidden_fees", rs.getBoolean("hiddenFees"));
			 		review.addProperty("poor_service", rs.getBoolean("poorService"));
			 		//add this obj to the array
			 		jArray.add(review);
			 	}
			 	
		        Gson gson = new Gson();
		        String jsonData = gson.toJson(jArray);
		        response.setContentType("application/json");
		        response.getWriter().write(jsonData);
	                  
	        } catch (SQLException e) {
	        	e.printStackTrace();
        	} catch (ClassNotFoundException e) {
        		e.printStackTrace();
        	}
        }
        else if (indexType.equals("user")){
        	int userId = Integer.parseInt(request.getHeader("Authorization").split(" ", 2)[1]);
        	try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
    	            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM reviews WHERE userID = ?;")) { 	
            		pstmt.setInt(1, userId);
            		ResultSet rs = pstmt.executeQuery();
            		
    			 	JsonArray jArray = new JsonArray();
    			 	while(rs.next()) {
    			 		JsonObject review = new JsonObject();
    			 		review = new JsonObject();
    			 		review.addProperty("hotelID", rs.getString("hotelID"));
    			 		review.addProperty("star_rating", rs.getInt("rating"));
    			 		review.addProperty("review", rs.getString("comment"));
    			 		review.addProperty("date", rs.getDate("reviewDate").toString());
    			 		review.addProperty("cleanliness", rs.getBoolean("clean"));
    			 		review.addProperty("pet_friendly", rs.getBoolean("pet-friendly"));
    			 		review.addProperty("eco_friendly", rs.getBoolean("ecofriendly"));
    			 		review.addProperty("excellent_service", rs.getBoolean("excellent-service"));
    			 		review.addProperty("dirty", rs.getBoolean("dirty"));
    			 		review.addProperty("missing_amenities", rs.getBoolean("missing-amenities"));
    			 		review.addProperty("hidden_fees", rs.getBoolean("hidden-fees"));
    			 		review.addProperty("poor_service", rs.getBoolean("poor-service"));
    			 		//add this obj to the array
    			 		jArray.add(review);
    			 	}

    		        Gson gson = new Gson();
    		        String jsonData = gson.toJson(jArray);
    		        response.setContentType("application/json");
    		        response.getWriter().write(jsonData);
        	            
    	        }catch (SQLException e) {e.printStackTrace();}
        	
        }
		
        
        
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}