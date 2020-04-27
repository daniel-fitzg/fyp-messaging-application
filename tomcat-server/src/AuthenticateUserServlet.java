import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/*
* Servlet processes user sign-in requests by authenticating users credentials
* */

@WebServlet(name = "AuthenticateUserServlet", urlPatterns = {"/AuthenticateUser"})
public class AuthenticateUserServlet extends HttpServlet {

    private UserService userService = new UserService();
    private JSONHandler jsonHandler = new JSONHandler();

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        User user = jsonHandler.createAuthenticateUserFromJSON(request, response);
        user = userService.authenticateUser(user);

        jsonHandler.writeJSONOutputUser(response, user);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
