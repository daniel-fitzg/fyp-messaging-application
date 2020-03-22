import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ServletHelper {

    JSONObject parseIncomingJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        return incomingJsonObject;
    }

    JSONArray buildConversationEntriesJsonArray(List<ConversationEntry> conversationEntries) {
        JSONArray jsonArray = new JSONArray();

        conversationEntries.forEach(entry -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authorId", entry.getAuthorId().toString());
            jsonObject.put("conversationId", entry.getConversationId().toString());
            jsonObject.put("dateCreated", entry.getDateCreated().toString());
            jsonObject.put("content", entry.getContent());
            jsonObject.put("authorName", entry.getAuthorName());

            jsonArray.add(jsonObject);
        });

        return jsonArray;
    }

    void writeJsonOutput(HttpServletResponse response, String jsonString) throws IOException {
        response.getWriter().write(jsonString);
        response.getWriter().flush();
        response.getWriter().close();
    }

    Conversation getConversation(CassandraDataStore cassandraDataStore, UUID userId, UUID secondaryUserId) {
        return cassandraDataStore.getConversation(userId, secondaryUserId);
    }
}
