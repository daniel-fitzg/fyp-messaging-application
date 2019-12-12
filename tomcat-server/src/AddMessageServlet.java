import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.UUID;

@WebServlet(name = "AddMessageServlet", urlPatterns = {"/AddMessage"})
public class AddMessageServlet extends javax.servlet.http.HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/octet-stream");
        ObjectInputStream objectInputStream = new ObjectInputStream(request.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(response.getOutputStream());

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        try {
            String incomingMessage = (String) objectInputStream.readObject();
            String returnedMessage = cassandraDataStore.addMessage(UUID.randomUUID(), UUID.randomUUID(), new Date(), incomingMessage);
            objectOutputStream.writeObject(returnedMessage);
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
