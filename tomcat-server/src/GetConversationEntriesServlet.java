import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/*
* Servlet gets and returns the entries of a single conversation for the current user and the secondary user/participant
* */

@WebServlet(name = "GetConversationEntriesServlet", urlPatterns = {"/GetConversationEntries"})
public class GetConversationEntriesServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        ServletHelper servletHelper = new ServletHelper();
        JSONObject incomingJsonObject = servletHelper.parseIncomingJSON(request, response);

        // User IDs of both participants of the conversation
        UUID authorId = UUID.fromString((String) incomingJsonObject.get("authorId"));
        UUID secondaryAuthorId = UUID.fromString((String) incomingJsonObject.get("secondaryAuthorId"));

        // Cassandra DB instance
        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        // Retrieves conversation data
        Conversation conversation = servletHelper.getConversation(cassandraDataStore, authorId,
                secondaryAuthorId);

        List<ConversationEntry> conversationEntries = new ArrayList<>();
        String authorName = cassandraDataStore.getUser(authorId).getFirstName();
        String secondaryAuthorName = cassandraDataStore.getUser(secondaryAuthorId).getFirstName();

        // Retrieves all entries of a conversation  from both participants
        conversationEntries = cassandraDataStore.getConversationEntries(conversation.getConversationId(), authorId, secondaryAuthorId,
                authorName, secondaryAuthorName);

        Collections.sort(conversationEntries);

        // Build JSON array of messages and then send to client
        JSONArray jsonArray = servletHelper.buildConversationEntriesJsonArray(conversationEntries);
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
