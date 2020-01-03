import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Conversation implements Serializable {
    private UUID userId;
    private UUID conversationId;
    private UUID secondaryUserId;
    private Date createDate;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public UUID getSecondaryUserId() {
        return secondaryUserId;
    }

    public void setSecondaryUserId(UUID secondaryUserId) {
        this.secondaryUserId = secondaryUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}


