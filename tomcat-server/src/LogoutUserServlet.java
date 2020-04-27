import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "LogoutUserServlet", urlPatterns = {"/LogoutUser"})
public class LogoutUserServlet extends HttpServlet {

    private UserService userService = new UserService();
    private JSONHandler jsonHandler = new JSONHandler();

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        UUID userId = jsonHandler.getUserIdFromJSON(request, response);

        userService.logoutUser(userId);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
