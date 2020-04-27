import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class ConversationContentDao {
    private final String tableName = "conversation_content";
    private Session session;

    ConversationContentDao(Session session) {
        this.session = session;
    }

    void addConversationEntry(ConversationEntry conversationEntry) {
        PreparedStatement addConversationEntry;

        addConversationEntry = session.prepare("INSERT INTO " + tableName + " (conversation_id, author_id, create_date, content) " +
                "VALUES (?, ?, ?, ?)");
        session.execute(addConversationEntry.bind(conversationEntry.getConversationId(),
                conversationEntry.getAuthorId(), conversationEntry.getDateCreated(), conversationEntry.getContent()));
    }

    List<ConversationEntry> getConversationEntries(UUID conversationId, UUID authorId, UUID secondaryAuthorId,
                                                   String authorName, String secondaryAuthorName) {

        PreparedStatement getConversationEntries;
        List<ConversationEntry> conversationEntries = new ArrayList<>();

        // Gets entries for the primary user of the conversation
        getConversationEntries = session.prepare("SELECT * FROM " + tableName + " WHERE conversation_id = " + conversationId
                + " AND author_id = " + authorId);
        ResultSet resultSet = session.execute(getConversationEntries.bind());
        populateConversationEntriesList(resultSet, conversationEntries, authorName);

        // gets entries for the secondary user of the conversation
        getConversationEntries = session.prepare("SELECT * FROM " + tableName + " WHERE conversation_id = " + conversationId
                + " AND author_id = " + secondaryAuthorId);
        resultSet = session.execute(getConversationEntries.bind());
        populateConversationEntriesList(resultSet, conversationEntries, secondaryAuthorName);

        return conversationEntries;
    }

    // Compiles a single list of messages from both users of the conversation to be returned to client
    private void populateConversationEntriesList(ResultSet resultSet, List<ConversationEntry> conversationEntries,
                                                 String contentAuthor) {

        resultSet.forEach(row -> {
            ConversationEntry conversationEntry = new ConversationEntry();

            conversationEntry.setConversationId(row.getUUID("conversation_id"));
            conversationEntry.setAuthorId(row.getUUID("author_id"));
            conversationEntry.setDateCreated(row.getTimestamp("create_date"));
            conversationEntry.setContent(row.getString("content"));
            conversationEntry.setAuthorName(contentAuthor);

            conversationEntries.add(conversationEntry);
        });
    }
}
