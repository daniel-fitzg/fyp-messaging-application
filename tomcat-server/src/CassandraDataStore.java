import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CassandraDataStore {
    private Cluster cluster;
    private Session session;
    private String localHostAddress = "127.0.0.1";
    private int portNumber = 9042;

    CassandraDataStore() {
        cluster = Cluster.builder().addContactPoint(localHostAddress).withPort(portNumber).build();
        session = cluster.connect("messaging_app");
    }

    public List<User> getAllUsers() {
        return new UserDao(session).getAllUsers();
    }

    User registerUser(RegisterUser newUser) {
        return new UserDao(session).registerUser(newUser);
    }

    User authenticateUser(User user) {
        return new UserDao(session).authenticateUser(user);
    }

    List<Conversation> getUserConversations(UUID userId, boolean isNewUser) {
        return new UserConversationsDao(session).getUserConversations(userId, isNewUser);
    }

    public User getUser(UUID userId) {
        return new UserDao(session).getUser(userId);
    }

    Conversation getConversation(UUID authorId, UUID secondaryAuthorId) {
        return new UserConversationsDao(session).getConversation(authorId, secondaryAuthorId);
    }

    void addConversationEntry(ConversationEntry conversationEntry) {
        new ConversationContentDao(session).addConversationEntry(conversationEntry);
    }

    List<ConversationEntry> getConversationEntries(UUID conversationId, UUID authorId, UUID secondaryAuthorId,
                                                   String authorName, String secondaryAuthorName) {

        return new ConversationContentDao(session).getConversationEntries(conversationId, authorId, secondaryAuthorId,
                authorName, secondaryAuthorName);
    }

    void close() {
        session.close();
        cluster.close();
    }
}
