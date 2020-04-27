import java.util.List;
import java.util.UUID;

public class UserService {

    private CassandraDataStore cassandraDataStore;
    private ValidationService validationService;

    UserService() {
        cassandraDataStore = new CassandraDataStore();
        validationService = new ValidationService();
    }

    User authenticateUser(User user) {
        // New User will be null and will follow failed authentication flow
        if (!validationService.validateAuthenticateUser(user)) {
            return null;
        }

        return cassandraDataStore.authenticateUser(user);
    }

    List<User> getAllUsers() {
        return cassandraDataStore.getAllUsers();
    }

    List<User> removeCurrentUser(List<User> users, UUID userId) {
        User duplicateUser = null;

        // Identifies and removes the current user within the returned list of all registered users
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                duplicateUser = user;
                break;
            }
        }
        users.remove(duplicateUser);

        return users;
    }
}
