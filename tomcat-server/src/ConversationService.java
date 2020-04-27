import java.util.List;
import java.util.UUID;

public class ConversationService {

    private CassandraDataStore cassandraDataStore;
    private ValidationService validationService;

    ConversationService() {
        this.cassandraDataStore = new CassandraDataStore();
    }

    void addConversationEntry(ConversationEntry conversationEntry) {
        cassandraDataStore.addConversationEntry(conversationEntry);
        cassandraDataStore.close();
    }

    List<ConversationEntry> getConversationEntries(UUID conversationId, UUID authorId, UUID secondaryAuthorId) {
        String authorName = cassandraDataStore.getUser(authorId).getUsername();
        String secondaryAuthorName = cassandraDataStore.getUser(secondaryAuthorId).getUsername();
        cassandraDataStore.close();

        return cassandraDataStore.getConversationEntries(conversationId, authorId, secondaryAuthorId, authorName, secondaryAuthorName);
    }
}
