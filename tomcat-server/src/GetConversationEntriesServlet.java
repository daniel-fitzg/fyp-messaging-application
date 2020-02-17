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
* Servlet gets and returns the entries of a single conversation for the user
* */

@WebServlet(name = "GetConversationEntriesServlet", urlPatterns = {"/GetConversationEntries"})
public class GetConversationEntriesServlet extends HttpServlet {

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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JSON Parse Exception thrown");
        }

        UUID conversationId = UUID.fromString((String) incomingJsonObject.get("conversationId"));
        UUID authorId = UUID.fromString((String) incomingJsonObject.get("authorId"));
        UUID secondaryAuthorId = UUID.fromString((String) incomingJsonObject.get("secondaryAuthorId"));

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        List<ConversationEntry> conversationEntries = new ArrayList<>();
        if (conversationId != null && authorId != null && secondaryAuthorId != null) {
            conversationEntries = cassandraDataStore.getConversationEntries(conversationId, authorId, secondaryAuthorId);

            JSONArray jsonArray = new JSONArray();
            conversationEntries.forEach(entry -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("authorId", entry.getAuthorId());
                jsonObject.put("conversationId", entry.getConversationId());
                jsonObject.put("dateCreated", entry.getDateCreated());
                jsonObject.put("content", entry.getContent());

                jsonArray.add(jsonObject);
            });

            response.getWriter().write(jsonArray.toJSONString());
            response.getWriter().flush();
            response.getWriter().close();
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
