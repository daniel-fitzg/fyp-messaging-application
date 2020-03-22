import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class ConversationEntry implements Serializable, Comparable<ConversationEntry> {
    private UUID conversationId;
    private UUID authorId;
    private Date dateCreated;
    private String content;
    private String authorName;

    ConversationEntry() {
        this.conversationId = null;
        this.authorId = null;
        this.dateCreated = null;
        this.content = null;
        this.authorName = null;
    }

    UUID getConversationId() {
        return conversationId;
    }

    void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    UUID getAuthorId() {
        return authorId;
    }

    void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    Date getDateCreated() {
        return dateCreated;
    }

    void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    String getAuthorName() {
        return authorName;
    }

    void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    // Comparable interface override, sorts conversation entries by date
    @Override
    public int compareTo(ConversationEntry conversationEntry) {
        return getDateCreated().compareTo(conversationEntry.getDateCreated());
    }
}
