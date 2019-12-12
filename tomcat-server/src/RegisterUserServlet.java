import com.sun.codemodel.internal.JForEach;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@WebServlet(name = "RegisterUserServlet", urlPatterns = {"/RegisterUser"})
public class RegisterUserServlet extends HttpServlet {

    private CassandraDataStore cassandraDataStore;

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/octet-stream");
        ObjectInputStream objectInputStream = new ObjectInputStream(request.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(response.getOutputStream());

        this.cassandraDataStore = new CassandraDataStore();

        RegisterUser newUser;
        try {
            newUser = (RegisterUser) objectInputStream.readObject();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectOutputStream.writeObject("false");
            newUser = null;
        }

        if (newUser != null) {
            try {
                authenticateNewUser(newUser);
                cassandraDataStore.registerUser(newUser);
                objectOutputStream.writeObject("true");
            } catch (IOException exception) {
                exception.printStackTrace();
                objectOutputStream.writeObject("false");
            }
        } else {
            objectOutputStream.writeObject("false");
        }

        cassandraDataStore.close();
    }

    private void authenticateNewUser(RegisterUser newUser) throws IOException {
        if (newUser.getFirstName().equalsIgnoreCase("")
        || newUser.getLastName().equalsIgnoreCase("")
        || newUser.getEmail().equalsIgnoreCase("")
        || newUser.getPassword().equalsIgnoreCase("")) {
            throw new IOException();
        }

        List<User> users = cassandraDataStore.getUsers();
        String newUserEmail = newUser.getEmail();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equalsIgnoreCase(newUserEmail)) {
                throw new IOException();
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
