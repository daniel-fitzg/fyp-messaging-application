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

    public String getSecondaryUserName() {
        return secondaryUserName;
    }

    public void setSecondaryUserName(String secondaryUserName) {
        this.secondaryUserName = secondaryUserName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}


