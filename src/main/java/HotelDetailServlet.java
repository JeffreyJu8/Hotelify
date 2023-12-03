import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



@WebServlet("/hotel-detail-search")
public class HotelDetailServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String place_id = "ChIJw_JUgvhYwokRIgBAxZdF4eA";
		//String place_id = request.getParameter("placeID");
		
		/*if (place_id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing place_id parameter");
            return;
        }*/
		
		String apiKey = "AIzaSyCPQJJxasTs23i9QyNJ7Zkz543o3gemWPI";
        String detailsURL = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" +
                           place_id + "&fields=name,rating,formatted_address,review&key=" + apiKey;
		
        
        try {
			URL url = new URL(detailsURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
	        conn.connect();
	        
	        int responsecode = conn.getResponseCode();
	        
	        if(responsecode != 200) { //error detected
	        	throw new RuntimeException("HttpResponseCode: " + responsecode);
	        } else {
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            StringBuilder jsonResponse = new StringBuilder();
	            String json;
	            while ((json = reader.readLine()) != null) {
	                jsonResponse.append(json);
	            }
	            reader.close();

	            JsonObject jsonObject = JsonParser.parseString(jsonResponse.toString()).getAsJsonObject();

	            JsonArray results = jsonObject.getAsJsonArray("results");

	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            response.getWriter().write(jsonObject.toString());
	        }
	       
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
