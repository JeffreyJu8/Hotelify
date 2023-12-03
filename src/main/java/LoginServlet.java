

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import java.security.SecureRandom;
import java.math.BigInteger;


import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private JDBCConnector jdbcConnector = new JDBCConnector();
    private SecureRandom random = new SecureRandom();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        User user = new Gson().fromJson(request.getReader(), User.class);

        String username = user.getUsername();
        String password = user.getPassword(); // Assume password is hashed

        Gson gson = new Gson();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String error = "Username or password is missing";
            pw.write(gson.toJson(error));
            pw.flush();
        } else {
            try {
                int isValidUser = jdbcConnector.authenticateUser(username, password);
                if (isValidUser > 0) {
                    // Generate a secure random session token
                    String sessionToken = new BigInteger(130, random).toString(32);

                    // Construct a JSON response with user ID and session token
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("userId", isValidUser);
                    jsonResponse.addProperty("sessionToken", sessionToken);

                    response.setStatus(HttpServletResponse.SC_OK);
                    pw.write(gson.toJson(jsonResponse));
                    pw.flush();
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    String error = "Invalid username or password";
                    pw.write(gson.toJson(error));
                    pw.flush();
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                String error = "An error occurred during login";
                pw.write(gson.toJson(error));
                pw.flush();
            }
        }
    }
}
