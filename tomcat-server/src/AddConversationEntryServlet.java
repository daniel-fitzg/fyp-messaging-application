import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "AddConversationEntryServlet", urlPatterns = {"/AddConversationEntry"})
public class AddConversationEntryServlet extends javax.servlet.http.HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        ObjectInputStream objectInputStream = new ObjectInputStream(request.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(response.getOutputStream());

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        try {
            ConversationEntry newConversationEntry = (ConversationEntry) objectInputStream.readObject();
            cassandraDataStore.addConversationEntry(newConversationEntry.getConversationId(), newConversationEntry.getAuthorId(),
                    newConversationEntry.getDateCreated(), newConversationEntry.getContent());
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        objectOutputStream.writeObject("true");

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
