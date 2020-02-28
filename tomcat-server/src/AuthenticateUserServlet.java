import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.BufferedReader;
import java.io.IOException;

/*
* Servlet processes user sign-in requests by authenticating users credentials
* */

@WebServlet(name = "AuthenticateUserServlet", urlPatterns = {"/AuthenticateUser"})
public class AuthenticateUserServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");

        BufferedReader bufferedReader = new BufferedReader(request.getReader());
        String incomingJsonString = bufferedReader.readLine();
        JSONParser jsonParser = new JSONParser();
        JSONObject incomingJsonObject = null;

        try {
            incomingJsonObject = (JSONObject) jsonParser.parse(incomingJsonString);
        } catch (ParseException exception) {
            exception.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JSON Parse Exception Thrown");
        }

        User user = new User();
        user.setEmail((String) incomingJsonObject.get("email"));
        user.setPassword((String) incomingJsonObject.get("password"));

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        JSONObject outgoingJsonObject = new JSONObject();
        if (validateExistingUser(user)) {
            User authenticatedUser = cassandraDataStore.authenticateUser(user);

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

        response.getWriter().write(outgoingJsonObject.toJSONString());
        response.getWriter().flush();
        response.getWriter().close();

        cassandraDataStore.close();
    }

    // Checks if user has entered empty strings for credentials
    private boolean validateExistingUser(User existingUser) throws IOException {
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
