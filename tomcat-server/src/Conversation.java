import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Conversation implements Serializable {
    private UUID userId;
    private UUID conversationId;
    private UUID secondaryUserId;
    private String secondaryUserName;
    private Date createDate;
    private Date lastUpdated;

    Conversation(UUID userId, UUID secondaryUserId, UUID conversationId, Date createDate, Date lastUpdated) {
        this.userId = userId;
        this.secondaryUserId = secondaryUserId;
        this.conversationId = conversationId;
        this.createDate = createDate;
        this.lastUpdated = lastUpdated;
    }

    Conversation() {
        this.userId = null;
        this.secondaryUserId = null;
        this.conversationId = null;
        this.createDate = null;
        this.lastUpdated = null;
    }

    void setUserId(UUID userId) {
        this.userId = userId;
    }

    UUID getConversationId() {
        return conversationId;
    }

    void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    void setSecondaryUserId(UUID secondaryUserId) {
        this.secondaryUserId = secondaryUserId;
    }

    void setSecondaryUserName(String secondaryUserName) {
        this.secondaryUserName = secondaryUserName;
    }

    void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}


