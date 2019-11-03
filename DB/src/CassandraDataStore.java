import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;

public class CassandraDataStore {
    private Cluster cluster;
    private Session session;
    private String localHostAddress = "127.0.0.1";
    private int portNumber = 9042;

    CassandraDataStore() {
        cluster = Cluster.builder().addContactPoint(localHostAddress).withPort(portNumber).build();
        session = cluster.connect();
    }

    public List<String> getUsers() {
        return new UserDao(session).getUsers();
    }
}
