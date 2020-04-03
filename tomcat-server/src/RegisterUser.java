import java.io.Serializable;

class RegisterUser implements Serializable {
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    RegisterUser() {
        this.firstName = null;
        this.lastName = null;
        this.username = null;
        this.password = null;
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

    String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }
}
