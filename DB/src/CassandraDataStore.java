import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.List;

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

    public void addMessage(String message) {
        new MessageDao(session).addMessage(message);
    }
}
