import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    Session session;

    public UserDao(Session session) {
        this.session = session;
    }

    public List<String> getUsers() {
        return new ArrayList<String>();
    }
}
