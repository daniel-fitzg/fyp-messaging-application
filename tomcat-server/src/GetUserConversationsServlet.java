import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/*
* Servlet gets and returns the conversations to which a single user belongs
* */

@WebServlet(name = "GetUserConversationsServlet", urlPatterns = {"/GetUserConversations"})
public class GetUserConversationsServlet extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

//        response.setContentType("application/octet-stream");
        // Needed to circumvent Cross Origin Resource-Sharing
        response.setHeader("Access-Control-Allow-Origin", "*");
        //response.setHeader("Access-Control-Allow-Methods", "post");
        BufferedReader bufferedReader = new BufferedReader(request.getReader());
        String incomingJsonString = bufferedReader.readLine();
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(incomingJsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        ObjectInputStream objectInputStream = new ObjectInputStream(request.getInputStream());
        //ObjectOutputStream objectOutputStream = new ObjectOutputStream(response.getOutputStream());

//        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write("Hello World");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serverResponse", Boolean.FALSE);
        response.getWriter().write(jsonObject.toJSONString());
        response.getWriter().flush();
        response.getWriter().close();

        System.out.println();

//        CassandraDataStore cassandraDataStore = new CassandraDataStore();
//        UUID userId = null;

//        try {
//            userId = (UUID) objectInputStream.readObject();
        // userId = UUID.fromString("51dca0e3-f008-4dd6-baf8-63b60348a119");
//        } catch (ClassNotFoundException exception) {
//            exception.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }

//        List<Conversation> userConversations = new ArrayList<>();
//
//        if (userId != null) {
//            userConversations = cassandraDataStore.getUserConversations(userId);
//        }

        // TODO: JSON to be used to send data to JavaScript Client, code below requires testing
//        JSONArray jsonArray = new JSONArray();
//        userConversations.forEach(conversation -> {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("userId", conversation.getUserId());
//            jsonObject.put("conversationId", conversation.getConversationId());
//            jsonObject.put("secondaryUserId", conversation.getSecondaryUserId());
//            jsonObject.put("secondaryUserName", conversation.getSecondaryUserName());
//            jsonObject.put("userId", conversation.getUserId());
//            jsonObject.put("lastUpdated", conversation.getLastUpdated());
//
//            jsonArray.add(jsonObject);
//        });


//        objectOutputStream.writeObject(jsonArray);
//        objectOutputStream.writeObject(userConversations);

//        cassandraDataStore.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }
}
