import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Date;
import java.util.UUID;

public class UserConversationsDao {
    private final String tableName = "user_conversations";
    private Session session;

    UserConversationsDao(Session session) {
        this.session = session;
    }

    Conversation getConversation(UUID authorId, UUID secondaryAuthorId) {
        // Checks for a conversation based on user ID and secondary user ID
        PreparedStatement getConversation = session.prepare("SELECT * FROM " + tableName + " WHERE user_id = " + authorId +
                " AND secondary_user_id = " + secondaryAuthorId);
        ResultSet resultSet = session.execute(getConversation.bind());

        // If conversation exists in table build conversation object and return result
        Row row = resultSet.one();
        if (row != null) {
            return buildConversation(row, authorId, secondaryAuthorId);
        }

        // Second check for the same conversation but with the user and secondary user IDs switched
        getConversation = session.prepare("SELECT * FROM " + tableName + " WHERE user_id = " + secondaryAuthorId +
                " AND secondary_user_id = " + authorId);
        resultSet = session.execute(getConversation.bind());

        // If conversation exists in table build conversation object and return result
        row = resultSet.one();
        if (row != null) {
            return buildConversation(row, authorId, secondaryAuthorId);
        }

        // If both reads do not find a conversation entry a new conversation is created
        return createNewConversation(authorId, secondaryAuthorId);
    }

    private Conversation buildConversation(Row row, UUID authorId, UUID secondaryAuthorId) {
        Conversation conversation = new Conversation();

        conversation.setConversationId(row.getUUID("conversation_id"));
        conversation.setUserId(authorId);
        conversation.setSecondaryUserId(secondaryAuthorId);
        conversation.setCreateDate(row.getTimestamp("create_date"));
        conversation.setLastUpdated(row.getTimestamp("last_updated"));

        return conversation;
    }

    private Conversation createNewConversation(UUID authorId, UUID secondaryAuthorId) {
        UUID conversationId = UUID.randomUUID();
        Date create_date = new Date();
        Date last_updated = create_date;

        PreparedStatement insertConversation = session.prepare("INSERT INTO " + tableName +
                        " (user_id, secondary_user_id, conversation_id, create_date, last_updated) VALUES (?, ?, ?, ?, ?)");
        ResultSet resultSet = session.execute(insertConversation.bind(authorId, secondaryAuthorId, conversationId, new Date(), new Date()));

        return new Conversation(authorId, secondaryAuthorId, conversationId, create_date, last_updated);
    }
}
