import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
* Servlet processes user sign-in requests by authenticating users credentials
* */

@WebServlet(name = "AuthenticateUserServlet", urlPatterns = {"/AuthenticateUser"})
public class AuthenticateUserServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        ServletHelper servletHelper = new ServletHelper();
        JSONObject incomingJsonObject = servletHelper.parseIncomingJSON(request, response);

        // New user object to be passed to data store
        User user = new User();
        user.setEmail((String) incomingJsonObject.get("email"));
        user.setPassword((String) incomingJsonObject.get("password"));

        // Cassandra DB instance
        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        JSONObject outgoingJsonObject = new JSONObject();
        // Check if data from client is empty strings
        if (validateExistingUser(user)) {
            User authenticatedUser = cassandraDataStore.authenticateUser(user);

            // If user is authenticated, assign user details to json object to be returned to client
            if (authenticatedUser != null) {
                outgoingJsonObject.put("userId", authenticatedUser.getUserId().toString());
                outgoingJsonObject.put("firstName", authenticatedUser.getFirstName());
                outgoingJsonObject.put("lastName", authenticatedUser.getLastName());
                outgoingJsonObject.put("email", authenticatedUser.getEmail());
                outgoingJsonObject.put("registerDate", authenticatedUser.getRegisterDate().toString());
            } else {
                // TODO reintegrate when HTTP error codes work
                //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Log in failed");
                outgoingJsonObject.put("userId", null);
            }
        } else {
            // TODO reintegrate when HTTP error codes work
            //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad input entered");
            outgoingJsonObject.put("userId", null);
        }

        servletHelper.writeJsonOutput(response, outgoingJsonObject.toJSONString());

        cassandraDataStore.close();
    }

    // Checks if user has entered empty strings for credentials
    private boolean validateExistingUser(User existingUser) {
        if (existingUser.getEmail().equalsIgnoreCase("") || existingUser.getPassword().equalsIgnoreCase("")) {
            return false;
        }

        return true;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
