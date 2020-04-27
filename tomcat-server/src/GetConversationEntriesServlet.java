import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/*
* Servlet gets and returns the entries of a single conversation for the current user and the secondary user/participant
* */

@WebServlet(name = "GetConversationEntriesServlet", urlPatterns = {"/GetConversationEntries"})
public class GetConversationEntriesServlet extends HttpServlet {

    private ConversationService conversationService = new ConversationService();
    private JSONHandler jsonHandler = new JSONHandler();

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, UUID> authorIds = jsonHandler.createAuthorIdListFromJSON(request, response);
        UUID authorId = authorIds.get("authorId");
        UUID secondaryAuthorId = authorIds.get("secondaryAuthorId");

        Conversation conversation = conversationService.getConversation(authorId, secondaryAuthorId);

        List<ConversationEntry> conversationEntries = conversationService.getConversationEntries(conversation.getConversationId(),
                authorId, secondaryAuthorId);

        jsonHandler.writeJSONOutputConversationEntries(response, conversationEntries);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
