import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MessageDao {
    private final String tableName = "conversation_content";
    private Session session;

    private PreparedStatement addMessage;
    private PreparedStatement getMessage;
    private PreparedStatement getConversationEntries;

    MessageDao(Session session) {
        this.session = session;
    }

    String addMessage(UUID messageId, UUID userId, Date createDate, String message) {
        addMessage = session.prepare("INSERT INTO " + tableName + " (message_id, creator_id, create_date, content) " +
                "VALUES (?, ?, ?, ?)");
        session.execute(addMessage.bind(messageId, userId, createDate, message));

        return getMessage(messageId, userId);
    }

    String getMessage(UUID messageId, UUID userId) {
        getMessage = session.prepare("SELECT content FROM " + tableName + " WHERE message_id = " + messageId + " AND creator_id = "
                + userId + "ALLOW FILTERING");
        ResultSet resultSet = session.execute(getMessage.bind());

        return resultSet.one().getString("content");
    }

    List<ConversationEntry> getConversationEntries(UUID conversationId) {
        List<ConversationEntry> conversationEntries = new ArrayList<>();

        getConversationEntries = session.prepare("SELECT * FROM " + tableName + " WHERE conversation_id = " + conversationId);
        ResultSet resultSet = session.execute(getConversationEntries.bind());

        return conversationEntries;
    }
}
