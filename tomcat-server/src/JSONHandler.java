import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JSONHandler {

    ConversationEntry createConversationEntryFromJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = parseIncomingJSON(request, response);

        ConversationEntry conversationEntry = new ConversationEntry();
        conversationEntry.setConversationId(UUID.fromString((String) jsonObject.get("conversationId")));
        conversationEntry.setAuthorId(UUID.fromString((String) jsonObject.get("authorId")));
        conversationEntry.setSecondaryAuthorId(UUID.fromString((String) jsonObject.get("secondaryAuthorId")));
        conversationEntry.setContent((String) jsonObject.get("content"));

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'").parse((String) jsonObject.get("createDate"));
            conversationEntry.setDateCreated(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return conversationEntry;
    }

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

    void writeJSONOutputConversationEntries(HttpServletResponse response, List<ConversationEntry> conversationEntries) throws IOException{
        Collections.sort(conversationEntries);
        response.getWriter().write(buildConversationEntriesJsonArray(conversationEntries).toJSONString());
        response.getWriter().flush();
        response.getWriter().close();
    }
}
