import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class ConversationEntry implements Serializable, Comparable<ConversationEntry> {
    private UUID conversationId;
    private UUID authorId;
    private Date dateCreated;
    private String content;
    private String authorName;

    ConversationEntry(UUID conversationId, UUID authorId, Date dateCreated, String content, String authorName) {
        this.conversationId = conversationId;
        this.authorId = authorId;
        this.dateCreated = dateCreated;
        this.content = content;
        this.authorName = authorName;
    }

    ConversationEntry() {
        this.conversationId = null;
        this.authorId = null;
        this.dateCreated = null;
        this.content = null;
        this.authorName = null;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public int compareTo(ConversationEntry conversationEntry) {
        return getDateCreated().compareTo(conversationEntry.getDateCreated());
    }
}
