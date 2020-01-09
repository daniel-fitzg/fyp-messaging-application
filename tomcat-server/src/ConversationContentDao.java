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
    private PreparedStatement getConversationEntries;

    ConversationContentDao(Session session) {
        this.session = session;
    }

    void addConversationEntry(UUID conversationId, UUID authorId, Date createDate, String content) {
        addConversationEntry = session.prepare("INSERT INTO " + tableName + " (conversation_id, author_id, create_date, content) " +
                "VALUES (?, ?, ?, ?)");
        session.execute(addConversationEntry.bind(conversationId, authorId, createDate, content));
    }

    List<ConversationEntry> getConversationEntries(UUID conversationId, UUID authorId, UUID secondaryAuthorId) {
        List<ConversationEntry> conversationEntries = new ArrayList<>();

        getConversationEntries = session.prepare("SELECT * FROM " + tableName + " WHERE conversation_id = " + conversationId
        + " AND author_id = " + authorId);
        ResultSet resultSet = session.execute(getConversationEntries.bind());
        // conversationEntries = populateConversationEntriesList(resultSet, conversationEntries);   ???
        populateConversationEntriesList(resultSet, conversationEntries);

        getConversationEntries = session.prepare("SELECT * FROM " + tableName + " WHERE conversation_id = " + conversationId
                + " AND author_id = " + secondaryAuthorId);
        resultSet = session.execute(getConversationEntries.bind());
        populateConversationEntriesList(resultSet, conversationEntries);

        return conversationEntries;
    }

    private void populateConversationEntriesList(ResultSet resultSet, List<ConversationEntry> conversationEntries) {
        resultSet.forEach(row -> {
            ConversationEntry conversationEntry = new ConversationEntry();

            conversationEntry.setConversationId(row.getUUID("conversation_id"));
            conversationEntry.setAuthorId(row.getUUID("author_id"));
            conversationEntry.setDateCreated(row.getTimestamp("create_date"));
            conversationEntry.setContent(row.getString("content"));

            conversationEntries.add(conversationEntry);
        });

    }
}
