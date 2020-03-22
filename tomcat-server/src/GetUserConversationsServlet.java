import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/*
* Servlet gets and returns the conversations to which a single user belongs
* */

@WebServlet(name = "GetUserConversationsServlet", urlPatterns = {"/GetUserConversations"})
public class GetUserConversationsServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        ServletHelper servletHelper = new ServletHelper();
        JSONObject incomingJsonObject = servletHelper.parseIncomingJSON(request, response);

        UUID userId = UUID.fromString((String) incomingJsonObject.get("userId"));

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        List<User> users = cassandraDataStore.getAllUsers();
        User duplicateUser = null;

        // Identifies and removes the current user within the returned list of all registered users
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                duplicateUser = user;
                break;
            }
        }
        users.remove(duplicateUser);

        // Populate JSON array with user objects
        JSONArray jsonArray = new JSONArray();
        users.forEach(user -> {
            JSONObject userJsonObject = new JSONObject();
            userJsonObject.put("userId", user.getUserId().toString());
            userJsonObject.put("firstName", user.getFirstName());
            userJsonObject.put("lastName", user.getLastName());

            jsonArray.add(userJsonObject);
        });

        servletHelper.writeJsonOutput(response, jsonArray.toString());

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
