import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class MessageDao {
    private final String tableName = "messages";
    private Session session;

    private PreparedStatement addMessage;

    MessageDao(Session session) {
        this.session = session;
    }

    void addMessage(String message) {
        addMessage = session.prepare("INSERT INTO " + tableName + " (message) " +
                "VALUES ('" + message + "')");

        session.execute(addMessage.bind());
    }
}
