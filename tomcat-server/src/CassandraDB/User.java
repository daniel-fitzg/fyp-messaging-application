package CassandraDB;

import java.util.Date;
import java.util.UUID;

public class User {
    private UUID userId;
    private String userName;
    private String password;
    private Date registerDate;
    private String email;

    public User() {}

    public User(UUID userId, String userName, String password, Date registerDate, String email) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.registerDate = registerDate;
        this.email = email;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
