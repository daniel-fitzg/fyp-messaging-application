import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    Map<String, UUID> createAuthorIdListFromJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, UUID> authorIds = new HashMap<>();

        JSONObject jsonObject = parseIncomingJSON(request, response);
        authorIds.put("authorId", UUID.fromString((String) jsonObject.get("authorId")));
        authorIds.put("secondaryAuthorId", UUID.fromString((String) jsonObject.get("secondaryAuthorId")));

        return authorIds;
    }

    UUID getUserIdFromJSON(HttpServletRequest request, HttpServletResponse response) throws IOException{
        JSONObject jsonObject = parseIncomingJSON(request, response);
         return UUID.fromString((String) jsonObject.get("userId"));
    }

    User createAuthenticateUserFromJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = parseIncomingJSON(request, response);

        User user = new User();
        user.setUsername((String) jsonObject.get("email"));
        user.setPassword((String) jsonObject.get("password"));

        return user;
    }

    RegisterUser createRegisterUserFromJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = parseIncomingJSON(request, response);

        RegisterUser user = new RegisterUser();
        user.setUsername((String) jsonObject.get("email"));
        user.setPassword((String) jsonObject.get("password"));
        user.setFirstName((String) jsonObject.get("firstName"));
        user.setLastName((String) jsonObject.get("lastName"));

        return user;
    }

    private JSONObject parseIncomingJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    private JSONArray buildConversationEntriesJsonArray(List<ConversationEntry> conversationEntries) {
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

    private JSONArray buildUserConversationsJsonArray(List<User> users) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        // Populate JSON array with user objects
        JSONArray jsonArray = new JSONArray();
        users.forEach(user -> {
            JSONObject userJsonObject = new JSONObject();
            userJsonObject.put("userId", user.getUserId().toString());
            userJsonObject.put("firstName", user.getFirstName());
            userJsonObject.put("lastName", user.getLastName());
            userJsonObject.put("registerDate", simpleDateFormat.format(user.getRegisterDate()));
            userJsonObject.put("onlineStatus", user.isOnlineStatus());

            jsonArray.add(userJsonObject);
        });

        return jsonArray;
    }

    void writeJSONOutputConversationEntries(HttpServletResponse response, List<ConversationEntry> conversationEntries) throws IOException{
        Collections.sort(conversationEntries);
        writeJSONOutput(response, buildConversationEntriesJsonArray(conversationEntries).toJSONString());
    }

    void writeJSONOutputAuthenticateUser(HttpServletResponse response, User user) throws IOException {
        JSONObject jsonObject = new JSONObject();


        if (user != null) {
            jsonObject.put("userId", user.getUserId().toString());
            jsonObject.put("firstName", user.getFirstName());
            jsonObject.put("lastName", user.getLastName());
            jsonObject.put("email", user.getUsername());
            jsonObject.put("registerDate", user.getRegisterDate().toString());

            writeJSONOutput(response, jsonObject.toJSONString());
        } else {
            jsonObject.put("userId", null);
        }

        writeJSONOutput(response, jsonObject.toJSONString());
    }

    void writeJSONOutputRegisterUser(HttpServletResponse response, User registeredUser) throws IOException {
        JSONObject jsonObject = new JSONObject();

        if (registeredUser != null) {
            System.out.println("User registered successfully");
            jsonObject.put("registeredUserId", registeredUser.getUserId().toString());
        } else {
            jsonObject.put("registeredUserId", false);
        }

        writeJSONOutput(response, jsonObject.toJSONString());
    }

    void writeJSONOutputConversationId(HttpServletResponse response, UUID conversationId) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("conversationId", conversationId.toString());
        writeJSONOutput(response, jsonObject.toJSONString());
    }

    void writeJSONOutputUserConversations(HttpServletResponse response, List<User> users) throws IOException {
        writeJSONOutput(response, buildUserConversationsJsonArray(users).toJSONString());
    }

    private void writeJSONOutput(HttpServletResponse response, String jsonString) throws IOException {
        response.getWriter().write(jsonString);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
