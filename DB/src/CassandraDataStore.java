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

    public void addUser(String userName, String password, String email) {
        new UserDao(session).addUser(userName, password, email);
    }

    public UUID getUserId(String userName) {
        return new UserDao(session).getUserId(userName);
    }

    public void addMessage(UUID messageId, UUID userId, Date createDate, String message) {
        new MessageDao(session).addMessage(messageId, userId, createDate, message);
    }

    public void close() {
        session.close();
        cluster.close();
    }
}
