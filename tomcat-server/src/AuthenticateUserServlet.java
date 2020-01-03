import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
* Servlet processes user sign-in requests by authenticating users credentials
* */

@WebServlet(name = "AuthenticateUserServlet", urlPatterns = {"/AuthenticateUser"})
public class AuthenticateUserServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        ObjectInputStream objectInputStream = new ObjectInputStream(request.getInputStream());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(response.getOutputStream());

        CassandraDataStore cassandraDataStore = new CassandraDataStore();
        User user;

        try {
            user = (User) objectInputStream.readObject();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            //objectOutputStream.writeObject("false");
            user = null;
        }

        if (user != null && validateExistingUser(user)) {
            User authenticatedUser = cassandraDataStore.authenticateUser(user);
//            if (authenticatedUser != null) {
                objectOutputStream.writeObject(authenticatedUser);
//            } else {
//                objectOutputStream.writeObject("false");
//            }
        } else {
            objectOutputStream.writeObject(null);
        }

        cassandraDataStore.close();
    }

    private boolean validateExistingUser(User existingUser) throws IOException {
        if (existingUser.getEmail().equalsIgnoreCase("") || existingUser.getPassword().equalsIgnoreCase("")) {
            return false;
        }

        return true;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
