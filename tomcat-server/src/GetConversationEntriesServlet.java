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
import java.util.Collections;
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

//        UUID conversationId = UUID.fromString((String) incomingJsonObject.get("conversationId"));
        UUID conversationId = UUID.fromString("cf88e803-4f31-4719-b726-ae3ac6fa10e3");
        UUID authorId = UUID.fromString((String) incomingJsonObject.get("authorId"));
//        UUID secondaryAuthorId = UUID.fromString((String) incomingJsonObject.get("secondaryAuthorId"));
        UUID secondaryAuthorId = UUID.fromString("7db251f0-a3ef-4787-830a-9bc0b1dbd0de");

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        List<ConversationEntry> conversationEntries = new ArrayList<>();
        if (conversationId != null && authorId != null && secondaryAuthorId != null) {
            conversationEntries = cassandraDataStore.getConversationEntries(conversationId, authorId, secondaryAuthorId);
            Collections.sort(conversationEntries);

//            String authorName = cassandraDataStore.getUser(authorId).getFirstName();
            String authorName = cassandraDataStore.getUser(UUID.fromString("2003c2f5-0d6d-4b05-8e79-ec90518a5675")).getFirstName();
//            String secondaryAuthorName = cassandraDataStore.getUser(secondaryAuthorId).getFirstName();
            String secondaryAuthorName = cassandraDataStore.getUser(UUID.fromString("2ebcb689-d31e-4d85-8dff-351e8f902d6c")).getFirstName();

            JSONArray jsonArray = new JSONArray();
            conversationEntries.forEach(entry -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("authorId", entry.getAuthorId().toString());
                jsonObject.put("conversationId", entry.getConversationId().toString());
                jsonObject.put("dateCreated", entry.getDateCreated().toString());
                jsonObject.put("content", entry.getContent());

                if (entry.getAuthorId().toString().equalsIgnoreCase("51dca0e3-f008-4dd6-baf8-63b60348a119")) {
                    jsonObject.put("authorName", "Alesia");
                } else {
                    jsonObject.put("authorName", "Danny");
                }

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
