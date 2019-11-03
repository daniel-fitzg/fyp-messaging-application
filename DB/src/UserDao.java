import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;

class UserDao {
    final String tableName = "users";
    private Session session;

    public UserDao(Session session) {
        this.session = session;
    }

    public List<String> getUsers() {
        List<String> users = new ArrayList<>();

        PreparedStatement preparedStatement = session.prepare("SELECT user_id FROM " + tableName);
        ResultSet resultSet = session.execute(preparedStatement.bind());

        resultSet.forEach(row -> {
            users.add(row.getString("user_id"));
        });

        return users;
    }
}
