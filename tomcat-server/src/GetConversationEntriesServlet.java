import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "GetConversationEntriesServlet", urlPatterns = {"/GetConversationEntries"})
public class GetConversationEntriesServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        ObjectInputStream objectInputStream = new ObjectInputStream(request.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(response.getOutputStream());

        CassandraDataStore cassandraDataStore = new CassandraDataStore();
        UUID conversationId = null;
        UUID authorId = null;
        UUID secondaryAuthorId = null;

        try {
            conversationId = (UUID) objectInputStream.readObject();
            authorId = (UUID) objectInputStream.readObject();
            secondaryAuthorId = (UUID) objectInputStream.readObject();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        List<ConversationEntry> conversationEntries = new ArrayList<>();
        if (conversationId != null && authorId != null && secondaryAuthorId != null) {
            conversationEntries = cassandraDataStore.getConversationEntries(conversationId, authorId, secondaryAuthorId);
        }

        objectOutputStream.writeObject(conversationEntries);

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
