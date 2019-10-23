import com.datastax.driver.core.Cluster;
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
        StringBuilder stringBuilder = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                .append(keyspaceName).append(" WITH replication = {")
                .append("'class':'").append(replicationStrategy)
                .append("','replication_factor':").append(replicationFactor)
                .append("};");

        String query = stringBuilder.toString();
        session.execute(query);
    }

    private void createTable(Session session, String keyspace, String tableName) {
        // TODO: Use keyspace command not working
        StringBuilder stringBuilder = new StringBuilder("USE " + keyspace + "CREATE TABLE IF NOT EXISTS ")
                .append(tableName).append("(")
                .append("id text, ")
                .append("title text, ")
                .append("age int, ")
                .append("PRIMARY KEY(id));");

        String query = stringBuilder.toString();
        session.execute(query);
    }
}

