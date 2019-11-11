import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

import java.util.Date;
import java.util.UUID;

public class MessageDao {
    private final String tableName = "messages";
    private Session session;

    private PreparedStatement addMessage;

    MessageDao(Session session) {
        this.session = session;
    }

    void addMessage(UUID messageId, UUID userId, Date createDate, String message) {
        addMessage = session.prepare("INSERT INTO " + tableName + " (message_id, creator_id, create_date, content) " +
                "VALUES (?, ?, ?, ?)");

        session.execute(addMessage.bind(messageId, userId, createDate, message));
    }
}
