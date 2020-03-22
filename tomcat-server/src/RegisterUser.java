import java.io.Serializable;

class RegisterUser implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    RegisterUser() {
        this.firstName = null;
        this.lastName = null;
        this.email = null;
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

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }
}
