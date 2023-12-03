	

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet("/hotel-search")
public class HotelSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String query = "hotels in New York";
		//String query = request.getParameter("query"); // e.x., "hotels in New York"
	    String apiKey = "AIzaSyCPQJJxasTs23i9QyNJ7Zkz543o3gemWPI"; 

	    if (query == null || query.isEmpty()) {
	        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing query parameter");
	        return;
	    }

	    String urlString = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" +
	            URLEncoder.encode(query, "UTF-8") + "&key=" + apiKey;
	    URL url = new URL(urlString);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.connect();

	    int responsecode = conn.getResponseCode();

	    
        if (responsecode != 200) {
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
    }
	

}
