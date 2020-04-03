import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class User implements Serializable {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Date registerDate;
    private boolean onlineStatus;

    User() {
        this.userId = null;
        this.firstName = null;
        this.lastName = null;
        this.username = null;
        this.password = null;
        this.registerDate = null;
        this.onlineStatus = false;
    }

    UUID getUserId() {
        return userId;
    }

    void setUserId(UUID userId) {
        this.userId = userId;
    }

    String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    Date getRegisterDate() {
        return registerDate;
    }

    void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
