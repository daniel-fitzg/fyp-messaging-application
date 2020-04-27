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
}
