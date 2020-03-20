import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
* Adds an entry to a conversation for a user. Once DB write occurs the servlet will read DB for all messages in the
* conversation and return list to the client.
* */

@WebServlet(name = "AddConversationEntryServlet", urlPatterns = {"/AddConversationEntry"})
public class AddConversationEntryServlet extends javax.servlet.http.HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        ServletHelper servletHelper = new ServletHelper();
        JSONObject incomingJsonObject = servletHelper.parseIncomingJSON(request, response);

        // User ID of the user on the other end of the conversation
        UUID secondaryAuthorId = UUID.fromString((String) incomingJsonObject.get("secondaryAuthorId"));

        // Cassandra DB instance
        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        // New conversation entry to be written to DB
        ConversationEntry conversationEntry = new ConversationEntry();
        conversationEntry.setConversationId(UUID.fromString((String) incomingJsonObject.get("conversationId")));
        conversationEntry.setAuthorId(UUID.fromString((String) incomingJsonObject.get("authorId")));
        conversationEntry.setContent((String) incomingJsonObject.get("content"));

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        Date date = null;
        try {
            date = myFormat.parse((String) incomingJsonObject.get("createDate"));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        conversationEntry.setDateCreated(date);
        // DB write
        cassandraDataStore.addConversationEntry(conversationEntry);

        // Names of the authors of the messages to be added to the array to be returned to the client
        // Each message item in array will have an author name associated with it
        String authorName = cassandraDataStore.getUser(conversationEntry.getAuthorId()).getFirstName();
        String secondaryAuthorName = cassandraDataStore.getUser(secondaryAuthorId).getFirstName();

        // Read DB for list of messages from the conversation to be sent to client
        List<ConversationEntry> conversationEntries = cassandraDataStore.getConversationEntries(conversationEntry.getConversationId(),
                conversationEntry.getAuthorId(), secondaryAuthorId, authorName, secondaryAuthorName);

        Collections.sort(conversationEntries);

        // Build JSON array of messages and then send to client
        JSONArray jsonArray = servletHelper.buildConversationEntriesJsonArray(conversationEntries);
        servletHelper.writeJsonOutput(response, jsonArray.toJSONString());

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
