import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class SchemaCreation {
    private Cluster cluster;
    private Session session;

    private SchemaCreation() {
        cluster = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).build();
        session = cluster.connect();
    }

    public static void main (String[] args) {
        new SchemaCreation().executeQuerySet();
    }

    private void executeQuerySet() {
        try {
            createKeyspace(session, "test_keyspace", "SimpleStrategy", 1);
            createTable(session, "test_keyspace", "test_table");
            write("test_table");
            read("test_table");
            session.close();
            cluster.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.close();
            cluster.close();
        }
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
                " (id text, name text, age int, PRIMARY KEY(id));");

        session.execute(preparedStatement.bind());
    }

    private void write(String tableName) {
        PreparedStatement preparedStatement = session.prepare("INSERT INTO " + tableName + " (id, name, age) " +
                "VALUES ('1234', 'Daniel', 32)");

        session.execute(preparedStatement.bind());

        preparedStatement = session.prepare("INSERT INTO " + tableName + " (id, name, age) " +
                "VALUES ('9876', 'Bob', 57)");

        session.execute(preparedStatement.bind());
    }

    private void read(String tableName) {
        PreparedStatement preparedStatement = session.prepare("SELECT * FROM " + tableName);
        ResultSet resultSet = session.execute(preparedStatement.bind());

        resultSet.forEach(result -> {
            System.out.println("Data: " + result);
        });
    }
}

