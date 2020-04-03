import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* Servlet registers a new user
* */

@WebServlet(name = "RegisterUserServlet", urlPatterns = {"/RegisterUser"})
public class RegisterUserServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        ServletHelper servletHelper = new ServletHelper();
        JSONObject incomingJsonObject = servletHelper.parseIncomingJSON(request, response);

        // Cassandra DB instance
        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        RegisterUser newUser = new RegisterUser();
        newUser.setUsername((String) incomingJsonObject.get("email"));
        newUser.setPassword((String) incomingJsonObject.get("password"));
        newUser.setFirstName((String) incomingJsonObject.get("firstName"));
        newUser.setLastName((String) incomingJsonObject.get("lastName"));

        JSONObject outgoingJsonObject = new JSONObject();

        // validateNewUser = Validates that user input does not contain empty strings
        if (validateNewUser(newUser)) {
            User registeredUser = cassandraDataStore.registerUser(newUser);

            if (registeredUser != null) {
                System.out.println("User registered successfully");
                outgoingJsonObject.put("registeredUserId", registeredUser.getUserId().toString());
            } else {
                //response.setStatus(HttpServletResponse.SC_CONFLICT, "Email already registered");
                outgoingJsonObject.put("registeredUserId", false);
            }
        } else {
            //response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            outgoingJsonObject.put("registeredUserId", false);
        }

        // Sends response to server
        // New user ID if registration successful, boolean: false is registration fails
        servletHelper.writeJsonOutput(response, outgoingJsonObject.toString());

        cassandraDataStore.close();
    }

    private boolean validateNewUser(RegisterUser newUser) throws IOException {
        return !newUser.getFirstName().equalsIgnoreCase("")
                && !newUser.getLastName().equalsIgnoreCase("")
                && !newUser.getUsername().equalsIgnoreCase("")
                && !newUser.getPassword().equalsIgnoreCase("");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
