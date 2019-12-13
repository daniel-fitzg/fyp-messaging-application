import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@WebServlet(name = "RegisterUserServlet", urlPatterns = {"/RegisterUser"})
public class RegisterUserServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        ObjectInputStream objectInputStream = new ObjectInputStream(request.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(response.getOutputStream());

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        RegisterUser newUser;
        try {
            newUser = (RegisterUser) objectInputStream.readObject();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectOutputStream.writeObject("false");
            newUser = null;
        }

        if (newUser != null && validateNewUser(newUser)) {
            User registeredUser = cassandraDataStore.registerUser(newUser);

            if (registeredUser != null) {
                objectOutputStream.writeObject("true");
            } else {
                objectOutputStream.writeObject("false");
            }
        } else {
            objectOutputStream.writeObject("false");
        }

        cassandraDataStore.close();
    }

    private boolean validateNewUser(RegisterUser newUser) throws IOException {
        return !newUser.getFirstName().equalsIgnoreCase("")
                && !newUser.getLastName().equalsIgnoreCase("")
                && !newUser.getEmail().equalsIgnoreCase("")
                && !newUser.getPassword().equalsIgnoreCase("");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
