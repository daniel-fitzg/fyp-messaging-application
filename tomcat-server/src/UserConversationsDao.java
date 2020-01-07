import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserConversationsDao {
    private final String tableName = "user_conversations";
    private Session session;

    private PreparedStatement getUserConversations;

    UserConversationsDao(Session session) {
        this.session = session;
    }

    List<Conversation> getUserConversations(UUID userId) {

        List<Conversation> userConversations = new ArrayList<>();

        getUserConversations = session.prepare("SELECT * FROM " + tableName + " WHERE user_id = " + userId);
        ResultSet resultSet = session.execute(getUserConversations.bind());

        resultSet.forEach( row -> {
           Conversation conversation = new Conversation();
           conversation.setUserId(row.getUUID("user_id"));
           conversation.setConversationId(row.getUUID("conversation_id"));
           conversation.setSecondaryUserId(row.getUUID("secondary_user_id"));
           conversation.setSecondaryUserName(row.getString("secondary_user_name"));
           conversation.setCreateDate(row.getTimestamp("create_date"));
           conversation.setLastUpdated(row.getTimestamp("last_updated"));

           userConversations.add(conversation);
        });

        return userConversations;
    }
}
