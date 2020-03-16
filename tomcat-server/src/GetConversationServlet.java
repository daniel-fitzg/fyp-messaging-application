import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "GetConversationServlet", urlPatterns = {"/GetConversation"})
public class GetConversationServlet extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");

        BufferedReader bufferedReader = new BufferedReader(request.getReader());
        String incomingJsonString = bufferedReader.readLine();
        JSONParser jsonParser = new JSONParser();
        JSONObject incomingJsonObject = null;

        try {
            incomingJsonObject = (JSONObject) jsonParser.parse(incomingJsonString);
        } catch (ParseException exception) {
            exception.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JSON Parse Exception thrown");
        }

        UUID authorId = UUID.fromString((String) incomingJsonObject.get("authorId"));
        UUID secondaryAuthorId = UUID.fromString((String) incomingJsonObject.get("secondaryAuthorId"));

        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        Conversation conversation = cassandraDataStore.getConversation(authorId, secondaryAuthorId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("conversationId", conversation.getConversationId().toString());

        response.getWriter().write(jsonObject.toJSONString());
        response.getWriter().flush();
        response.getWriter().close();

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
