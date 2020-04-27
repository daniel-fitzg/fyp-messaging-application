import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/*
* Adds an entry to a conversation for a user. Once DB write occurs the servlet will read DB for all messages in the
* conversation and return list to the client.
* */

@WebServlet(name = "AddConversationEntryServlet", urlPatterns = {"/AddConversationEntry"})
public class AddConversationEntryServlet extends javax.servlet.http.HttpServlet {

    private ConversationService conversationService = new ConversationService();
    private JSONHandler jsonHandler = new JSONHandler();

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        ConversationEntry conversationEntry = jsonHandler.createConversationEntryFromJSON(request, response);

        conversationService.addConversationEntry(conversationEntry);

        List<ConversationEntry> conversationEntries = conversationService.getConversationEntries(conversationEntry.getConversationId(),
                conversationEntry.getAuthorId(), conversationEntry.getSecondaryAuthorId());

        jsonHandler.writeJSONOutputConversationEntries(response, conversationEntries);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
