

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImagePostServlet
 */
@WebServlet("/submit-review")
@MultipartConfig
public class SubmitReview extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 Megabytes
	
	//private JDBCConnector jdbcConnector = new JDBCConnector(); Larry's 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubmitReview() {
        super();
        
    }


	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String hotelId = request.getParameter("hotelId"); // we should automatically set this parameter when the button to review a hotel is clicked
		int userId = Integer.parseInt(request.getHeader("Authorization").split(" ", 2)[1]);
	    //Text and radio button
	    String comment = request.getParameter("comment");
	    int star_rating = Integer.parseInt(request.getParameter("rating"));
	    long currentTimeMillis = System.currentTimeMillis();
	    Date currentDate = new java.sql.Date(currentTimeMillis);
	    String tag_clean = request.getParameter("clean");
	    String tag_excellent_service = request.getParameter("excellent-service");
	    String tag_pet_friendly = request.getParameter("pet-friendly");
	    String tag_ecofriendly = request.getParameter("ecofriendly");
	    String tag_dirty = request.getParameter("dirty");
	    String tag_hidden_fees = request.getParameter("hidden-fees");
	    String tag_poorservice = request.getParameter("poor-service");
	    String tag_missing_amenities = request.getParameter("missing-amenities");
	    
	    boolean clean = "ON".equalsIgnoreCase(tag_clean);
	    boolean excellentService = "ON".equalsIgnoreCase(tag_excellent_service);
	    boolean petFriendly = "ON".equalsIgnoreCase(tag_pet_friendly);
	    boolean ecoFriendly = "ON".equalsIgnoreCase(tag_ecofriendly);
	    boolean dirty = "ON".equalsIgnoreCase(tag_dirty);
	    boolean hiddenFees = "ON".equalsIgnoreCase(tag_hidden_fees);
	    boolean poorService = "ON".equalsIgnoreCase(tag_poorservice);
	    boolean missingAmenities = "ON".equalsIgnoreCase(tag_missing_amenities);

	    
      

		//JDBC info - TODO: change to a real ones
        String url = "jdbc:mysql://localhost:3306/mydb";
        String dbUser = "yourUsername";
        String dbPassword = "yourPassword";

        // TODO: specify the Values
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO reviews (userID, hotelID, rating, comment, reviewDate, clean,"
             		+ " petFriendly, ecoFriendly, excellentService, dirty, missingAmenities, hiddenFees, poorService) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            
        	pstmt.setInt(1, userId);
        	pstmt.setString(2, hotelId);
        	pstmt.setInt(3, star_rating);
            pstmt.setString(4, comment);;
            pstmt.setDate(5, currentDate);
            pstmt.setBoolean(6, clean);
            pstmt.setBoolean(7, petFriendly);
            pstmt.setBoolean(8, ecoFriendly);
            pstmt.setBoolean(9, excellentService);
            pstmt.setBoolean(10, dirty);
            pstmt.setBoolean(11, missingAmenities);
            pstmt.setBoolean(12, hiddenFees);
            pstmt.setBoolean(13, poorService);
            
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        // Create a json obj
//        JsonObject reviewForm = new JsonObject();
//        reviewForm.addProperty("textField", textField);
//        reviewForm.addProperty("radioButton", radioButtonValue);
//        reviewForm.addProperty("fileName", fileName);
//
//        Gson gson = new Gson();
//        String jsonData = gson.toJson(reviewForm);
//
//        //TODO: define the path
//        try (FileWriter fileWriter = new FileWriter("path/to/yourData.json")) {
//            fileWriter.write(jsonData);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//		// TODO: link this JSON file with User and Hotel class.
//		
//        //response
//        response.setContentType("text/html");
//        PrintWriter out = response.getWriter();
//        out.println("<html><body>");
//        out.println("<h2>Received Data</h2>");
//        out.println("<p>Text Field: " + textField + "</p>");
//        out.println("<p>Radio Button: " + radioButtonValue + "</p>");
//        out.println("<p>File: " + fileName + "</p>");
//        out.println("</body></html>");


	}
	
	private String generateUniqueFileName() {
	    // Using current time
	    return String.valueOf(System.currentTimeMillis());
	}
	
	private String getFileExtension(String contentType) {
	    switch (contentType) {
	        case "image/jpeg":
	            return ".jpg";
	        case "image/png":
	            return ".png";
	        case "image/gif":
	            return ".gif";
	        default:
	            return "";
	    }
	}

}
