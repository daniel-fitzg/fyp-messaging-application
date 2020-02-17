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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* Servlet gets and returns the conversations to which a single user belongs
* */

@WebServlet(name = "GetUserConversationsServlet", urlPatterns = {"/GetUserConversations"})
public class GetUserConversationsServlet extends HttpServlet {

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

        List<Conversation> userConversations = new ArrayList<>();
        if (userId != null) {
            userConversations = cassandraDataStore.getUserConversations(userId);
        }

        JSONArray jsonArray = new JSONArray();
        userConversations.forEach(conversation -> {
            JSONObject userConversationJsonObject = new JSONObject();
            userConversationJsonObject.put("conversationId", conversation.getConversationId());
            userConversationJsonObject.put("secondaryUserId", conversation.getSecondaryUserId());
            userConversationJsonObject.put("secondaryUserName", conversation.getSecondaryUserName());
            userConversationJsonObject.put("userId", conversation.getUserId());
            userConversationJsonObject.put("lastUpdated", conversation.getLastUpdated());
            userConversationJsonObject.put("createDate", conversation.getCreateDate());

            jsonArray.add(userConversationJsonObject);
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
