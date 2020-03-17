import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/*
* Servlet gets and returns the conversations to which a single user belongs
* */

@WebServlet(name = "GetUserConversationsServlet", urlPatterns = {"/GetUserConversations"})
public class GetUserConversationsServlet extends HttpServlet {

    // TODO Large scale refactor needed of this servlet

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Needed to circumvent Cross Origin Resource-Sharing
        response.setHeader("Access-Control-Allow-Origin", "*");

        BufferedReader bufferedReader = new BufferedReader(request.getReader());
        String incomingJsonString = bufferedReader.readLine();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(incomingJsonString);
        } catch (ParseException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        UUID userId = UUID.fromString((String) jsonObject.get("userId"));

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        // If userId belongs to a newly registered user, all users are returned to allow the new user to start a conversation.
        // In reality, this method would be replaced by the users contact book sync to generate available users to interact with
       // boolean isNewUser = cassandraDataStore.isNewUser(userId);

        List<User> users = cassandraDataStore.getAllUsers();
        User duplicateUser = null;
        // Removes the current user from the user list to be sent back to the client
//        users.forEach(user -> {
//           if (user.getUserId().equals(userId)) {
//               duplicateUser.set(user);
//           }
//        });

        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                duplicateUser = user;
                break;
            }
        }

        users.remove(duplicateUser);

        JSONArray jsonArray = new JSONArray();
        users.forEach(user -> {
            JSONObject userJsonObject = new JSONObject();
            userJsonObject.put("userId", user.getUserId().toString());
            userJsonObject.put("firstName", user.getFirstName());
            userJsonObject.put("lastName", user.getLastName());

            jsonArray.add(userJsonObject);
        });

        response.getWriter().write(jsonArray.toJSONString());
        response.getWriter().flush();
        response.getWriter().close();

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
