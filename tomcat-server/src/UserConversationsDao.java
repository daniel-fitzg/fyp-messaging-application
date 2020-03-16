import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserConversationsDao {
    private final String tableName = "user_conversations";
    private Session session;

    private PreparedStatement getUserConversations;

    UserConversationsDao(Session session) {
        this.session = session;
    }

    Conversation getConversation(UUID authorId, UUID secondaryAuthorId) {
        Conversation conversation = null;

        PreparedStatement getConversation = session.prepare("SELECT * FROM " + tableName + " WHERE user_id = " + authorId +
                " AND secondary_user_id = " + secondaryAuthorId);
        ResultSet resultSet = session.execute(getConversation.bind());

        Row row = resultSet.one();
        if (row != null) {
            conversation = new Conversation();
            conversation.setConversationId(row.getUUID("conversation_id"));
            conversation.setUserId(authorId);
            conversation.setSecondaryUserId(secondaryAuthorId);
            conversation.setCreateDate(row.getTimestamp("create_date"));
            conversation.setLastUpdated(row.getTimestamp("last_updated"));
        } else {
            conversation = insertNewConversation(authorId, secondaryAuthorId);
        }

        return conversation;
    }

    private Conversation insertNewConversation(UUID authorId, UUID secondaryAuthorId) {
        UUID conversationId = UUID.randomUUID();
        Date create_date = new Date();
        Date last_updated = create_date;

        PreparedStatement insertConversation = session.prepare("INSERT INTO " + tableName +
                        " (user_id, secondary_user_id, conversation_id, create_date, last_updated) VALUES (?, ?, ?, ?, ?)");
        ResultSet resultSet = session.execute(insertConversation.bind(authorId, secondaryAuthorId, conversationId, new Date(), new Date()));

        return new Conversation(authorId, secondaryAuthorId, conversationId, create_date, last_updated);
    }

    List<Conversation> getUserConversations(UUID userId, boolean isNewUser) {

        List<Conversation> userConversations = new ArrayList<>();

        getUserConversations = session.prepare("SELECT * FROM " + tableName);
        ResultSet resultSet = session.execute(getUserConversations.bind());

        resultSet.forEach(row -> {
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
