import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ConversationContentDao {
    private final String tableName = "conversation_content";
    private Session session;

    private PreparedStatement addConversationEntry;
    private PreparedStatement getMessage;
    private PreparedStatement getConversationEntries;

    ConversationContentDao(Session session) {
        this.session = session;
    }

    void addConversationEntry(UUID conversationId, UUID authorId, Date createDate, String content) {
        addConversationEntry = session.prepare("INSERT INTO " + tableName + " (conversation_id, author_id, create_date, content) " +
                "VALUES (?, ?, ?, ?)");
        session.execute(addConversationEntry.bind(conversationId, authorId, createDate, content));
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
