import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
* Servlet registers a new user
* */

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

//            // TODO: JSON to be used to send data to JavaScript Client, code needs testing
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("serverResponse", Boolean.FALSE);
//            objectOutputStream.writeObject(jsonObject.toJSONString());

            objectOutputStream.writeObject("false");
            newUser = null;
        }

        if (newUser != null && validateNewUser(newUser)) {
            User registeredUser = cassandraDataStore.registerUser(newUser);

            if (registeredUser != null) {
                // TODO: JSON to be used to send data to JavaScript Client, below code working OK
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("serverResponse", Boolean.TRUE);
//                objectOutputStream.writeObject(jsonObject.toJSONString());

                objectOutputStream.writeObject("true");
            } else {
//                // TODO: JSON to be used to send data to JavaScript Client, code needs testing
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("serverResponse", Boolean.FALSE);
//                objectOutputStream.writeObject(jsonObject.toJSONString());

                objectOutputStream.writeObject("false");
            }
        } else {
            // TODO: JSON to be used to send data to JavaScript Client, code needs testing
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("serverResponse", Boolean.FALSE);
//            objectOutputStream.writeObject(jsonObject.toJSONString());

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
