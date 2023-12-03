
import com.google.gson.Gson;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
	// Assuming you've included a method to get an instance of JDBCConnector
    private JDBCConnector jdbcConnector = new JDBCConnector();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        User user = new Gson().fromJson(request.getReader(), User.class);

        String username = user.getUsername();
        String password = user.getPassword(); // Remember to hash the password in the JDBCConnector's method
        String email = user.getEmail();

        Gson gson = new Gson();

        if (username == null || username.isBlank() || password == null || password.isBlank() || email == null || email.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String error = "User info is missing";
            pw.write(gson.toJson(error));
            pw.flush();
        } else {
            try {
                int userID = jdbcConnector.registerUser(username, password, email);
                if (userID == -2) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    String error = "Email is already in use";
                    pw.write(gson.toJson(error));
                    pw.flush();
                } else if (userID == -1) {
                	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    String error = "Username is already in use";
                    pw.write(gson.toJson(error));
                    pw.flush();
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                    pw.write(gson.toJson(userID));
                    pw.flush();
                }
            } catch (Exception e) {
                // Handle exceptions properly here
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                String error = "An error occurred during registration";
                pw.write(gson.toJson(error));
                pw.flush();
            }
        }
    }

    
}