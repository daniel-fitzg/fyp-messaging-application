import javax.servlet.ServletException;
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

        try {
            conversationId = (UUID) objectInputStream.readObject();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        // TODO conversation_content table - conversation_id, author_id, content, date_created
        List<ConversationEntry> conversationEntries = new ArrayList<>();
        if (conversationId != null) {
            conversationEntries = cassandraDataStore.getConversationEntries(conversationId);
        }

        objectOutputStream.writeObject(null);

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
