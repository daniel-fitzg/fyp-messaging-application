import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class DTO {
    private Cluster cluster;
    private Session session;

    DTO() {
        cluster = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).build();
        session = cluster.connect();
    }

    public static void main (String[] args) {
        new DTO().executeQuerySet();
    }

    private void executeQuerySet() {
        createKeyspace(session, "test_keyspace", "SimpleStrategy", 1);
        createTable(session, "test_keyspace", "test_table");
        session.close();
        cluster.close();
    }

    private void createKeyspace(Session session, String keyspaceName, String replicationStrategy, int replicationFactor) {
        PreparedStatement preparedStatement = session.prepare("CREATE KEYSPACE IF NOT EXISTS " + keyspaceName +
                " WITH replication = {'class': '" + replicationStrategy + "','replication_factor': " + replicationFactor + "};");

        session.execute(preparedStatement.bind());
    }

    private void createTable(Session session, String keyspace, String tableName) {
        PreparedStatement preparedStatement = session.prepare("USE " + keyspace + ";");
        session.execute(preparedStatement.bind());

        preparedStatement = session.prepare("CREATE TABLE IF NOT EXISTS " + tableName +
                " (id text, title text, age int, PRIMARY KEY(id));");

        session.execute(preparedStatement.bind());
    }
}

