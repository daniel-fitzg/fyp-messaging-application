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

    public List<User> getUsers() {
        return new UserDao(session).getUsers();
    }

    User registerUser(RegisterUser newUser) {
        return new UserDao(session).registerUser(newUser);
    }

    User authenticateUser(User user) {
        return new UserDao(session).authenticateUser(user);
    }

    List<Conversation> getUserConversations(UUID userId) {
        return new UserConversationsDao(session).getUserConversations(userId);
    }

    public User getUser(UUID userId) {
        return new UserDao(session).getUser(userId);
    }

    void addConversationEntry(UUID conversationId, UUID authorId, Date createDate, String content) {
        new ConversationContentDao(session).addConversationEntry(conversationId, authorId, createDate, content);
    }

    List<ConversationEntry> getConversationEntries(UUID conversationId) {
        return new ConversationContentDao(session).getConversationEntries(conversationId);
    }

    public String getMessage(UUID messageId, UUID userId) {
        return new ConversationContentDao(session).getMessage(messageId, userId);
    }

    void close() {
        session.close();
        cluster.close();
    }
}
