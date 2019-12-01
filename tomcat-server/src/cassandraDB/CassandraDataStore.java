package cassandraDB;

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

    public CassandraDataStore() {
        cluster = Cluster.builder().addContactPoint(localHostAddress).withPort(portNumber).build();
        // TODO: catch com.datastax.driver.core.exceptions.NoHostAvailableException
        session = cluster.connect("messaging_app");
    }

    public List<User> getUsers() {
        return new UserDao(session).getUsers();
    }

    public User addUser(String userName, String password, Date registerDate, String email) {
        return new UserDao(session).addUser(userName, password, registerDate, email);
    }

    public User getUser(String userName) {
        return new UserDao(session).getUser(userName);
    }

    public String addMessage(UUID messageId, UUID userId, Date createDate, String message) {
        return new MessageDao(session).addMessage(messageId, userId, createDate, message);
    }

    public String getMessage(UUID messageId, UUID userId) {
        return new MessageDao(session).getMessage(messageId, userId);
    }

    public void close() {
        session.close();
        cluster.close();
    }
}
