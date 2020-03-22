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
        // Allows resource sharing across different origins
        response.setHeader("Access-Control-Allow-Origin", "*");

        ServletHelper servletHelper = new ServletHelper();
        JSONObject incomingJsonObject = servletHelper.parseIncomingJSON(request, response);

        // Cassandra DB instance
        CassandraDataStore cassandraDataStore = new CassandraDataStore();

        // Retrieves conversation data
        Conversation conversation = servletHelper.getConversation(cassandraDataStore, UUID.fromString((String) incomingJsonObject.get("authorId")),
                UUID.fromString((String) incomingJsonObject.get("secondaryAuthorId")));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("conversationId", conversation.getConversationId().toString());

        servletHelper.writeJsonOutput(response, jsonObject.toString());

        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
