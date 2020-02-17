import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/*
* Servlet registers a new user
* */

@WebServlet(name = "RegisterUserServlet", urlPatterns = {"/RegisterUser"})
public class RegisterUserServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");

        BufferedReader bufferedReader = new BufferedReader(request.getReader());
        String incomingJsonString = bufferedReader.readLine();
        JSONParser jsonParser = new JSONParser();
        JSONObject incomingJsonObject = null;

        try {
            incomingJsonObject = (JSONObject) jsonParser.parse(incomingJsonString);
        } catch (ParseException exception) {
            exception.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        RegisterUser newUser = new RegisterUser();
        newUser.setEmail((String) incomingJsonObject.get("email"));
        newUser.setPassword((String) incomingJsonObject.get("password"));
        newUser.setFirstName((String) incomingJsonObject.get("firstName"));
        newUser.setLastName((String) incomingJsonObject.get("lastName"));

        // validateNewUser = Validates that user input does not contain empty strings
        if (validateNewUser(newUser)) {
            User registeredUser = cassandraDataStore.registerUser(newUser);

            if (registeredUser != null) {
                System.out.println("User registered successfully");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT, "Email already registered");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        cassandraDataStore.close();
    }

    private boolean validateNewUser(RegisterUser newUser) throws IOException {
        return !newUser.getFirstName().equalsIgnoreCase("")
                && !newUser.getLastName().equalsIgnoreCase("")
                && !newUser.getEmail().equalsIgnoreCase("")
                && !newUser.getPassword().equalsIgnoreCase("");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
