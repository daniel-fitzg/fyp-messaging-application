public class ValidationService {

    boolean validateAuthenticateUser(User user) {
        // Checks if user has entered empty strings
        if (user.getUsername().equalsIgnoreCase("") || user.getPassword().equalsIgnoreCase("")) {
            return false;
        }

        return true;
    }

    boolean validateRegisterUser(RegisterUser user) {
        // Checks if user has entered empty strings
        return !user.getFirstName().equalsIgnoreCase("")
                && !user.getLastName().equalsIgnoreCase("")
                && !user.getUsername().equalsIgnoreCase("")
                && !user.getPassword().equalsIgnoreCase("");
    }
}
