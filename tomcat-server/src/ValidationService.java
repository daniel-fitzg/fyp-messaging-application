public class ValidationService {

    boolean validateAuthenticateUser(User user) {
        // Checks if user has entered empty strings
        if (user.getUsername().equalsIgnoreCase("") || user.getPassword().equalsIgnoreCase("")) {
            return false;
        }

        return true;
    }
}
