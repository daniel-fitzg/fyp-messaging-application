public class UserService {

    private CassandraDataStore cassandraDataStore;

    UserService() {
        cassandraDataStore = new CassandraDataStore();
    }

    
}
