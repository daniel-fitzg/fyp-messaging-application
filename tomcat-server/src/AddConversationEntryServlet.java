import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.UUID;

/*
* Adds an entry to a conversation for a user
* */

@WebServlet(name = "AddConversationEntryServlet", urlPatterns = {"/AddConversationEntry"})
public class AddConversationEntryServlet extends javax.servlet.http.HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

//        response.setHeader("Access-Control-Allow-Origin", "*");

        BufferedReader bufferedReader = new BufferedReader(request.getReader());
        String incomingJsonString = bufferedReader.readLine();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) jsonParser.parse(incomingJsonString);
        } catch (ParseException exception) {
            exception.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JSON Parse Exception Thrown");
        }

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        ConversationEntry conversationEntry = new ConversationEntry();
        // TODO Replace random UUIDs with actual UUIDs from client
        conversationEntry.setConversationId(UUID.randomUUID());
        conversationEntry.setAuthorId(UUID.randomUUID());
        conversationEntry.setDateCreated(new Date());
        conversationEntry.setContent((String) jsonObject.get("message"));

        cassandraDataStore.addConversationEntry(conversationEntry);
        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
