import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
* Adds an entry to a conversation for a user
* */

@WebServlet(name = "AddConversationEntryServlet", urlPatterns = {"/AddConversationEntry"})
public class AddConversationEntryServlet extends javax.servlet.http.HttpServlet {

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
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JSON Parse Exception Thrown");
        }

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        ConversationEntry conversationEntry = new ConversationEntry();
        //conversationEntry.setConversationId(UUID.fromString((String) incomingJsonObject.get("conversationId")));
        conversationEntry.setConversationId(UUID.fromString("cf88e803-4f31-4719-b726-ae3ac6fa10e3"));
//        conversationEntry.setAuthorId(UUID.fromString((String) incomingJsonObject.get("authorId")));
        conversationEntry.setAuthorId(UUID.fromString("51dca0e3-f008-4dd6-baf8-63b60348a119"));

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        Date date = null;
        try {
             date = myFormat.parse((String) incomingJsonObject.get("createDate"));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        conversationEntry.setDateCreated(date);
        conversationEntry.setContent((String) incomingJsonObject.get("content"));

        cassandraDataStore.addConversationEntry(conversationEntry);

        List<ConversationEntry> conversationEntries = new ArrayList<>();
        conversationEntries = cassandraDataStore.getConversationEntries(UUID.fromString("cf88e803-4f31-4719-b726-ae3ac6fa10e3"),
                UUID.fromString("51dca0e3-f008-4dd6-baf8-63b60348a119"),
                UUID.fromString("7db251f0-a3ef-4787-830a-9bc0b1dbd0de"));

        Collections.sort(conversationEntries);

        JSONArray jsonArray = new JSONArray();
        conversationEntries.forEach(entry -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authorId", entry.getAuthorId().toString());
            jsonObject.put("conversationId", entry.getConversationId().toString());
            jsonObject.put("dateCreated", entry.getDateCreated().toString());
            jsonObject.put("content", entry.getContent());

            jsonArray.add(jsonObject);
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
