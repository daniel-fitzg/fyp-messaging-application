import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class MessageDao {
    private final String tableName = "messages";
    Session session;

    public MessageDao(Session session) {
        this.session = session;
    }

    public void addMessage(String message) {
        PreparedStatement preparedStatement = session.prepare("INSERT INTO " + tableName + " (message) " +
                "VALUES ('" + message + "')");

        session.execute(preparedStatement.bind());
    }
}
