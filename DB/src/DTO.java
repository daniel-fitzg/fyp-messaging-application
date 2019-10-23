import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
//added imports to classpath
public class DTO {


    public static void main (String[] args) {
        Cluster cluster;
        Session session;

        cluster = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).build();
        session = cluster.connect();
        createKeyspace("test_keyspace", "SimpleStrategy", 1, session);
        session.close();
        cluster.close();
    }


    public static void createKeyspace(String keyspaceName, String replicationStrategy, int replicationFactor, Session session) {
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                .append(keyspaceName).append(" WITH replication = {")
                .append("'class':'").append(replicationStrategy)
                .append("','replication_factor':").append(replicationFactor)
                .append("};");

        String query = sb.toString();
        session.execute(query);
    }
}

