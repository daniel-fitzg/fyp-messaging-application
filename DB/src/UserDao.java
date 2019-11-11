import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO For each table add comment block specifying columns


class UserDao {
    final String tableName = "users";
    private Session session;

    public UserDao(Session session) {
        this.session = session;
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        PreparedStatement preparedStatement = session.prepare("SELECT * FROM " + tableName);
        ResultSet resultSet = session.execute(preparedStatement.bind());

        User user = new User();
        resultSet.forEach(row -> {
            user.setUserId(row.getUUID("user_id"));
            user.setUserName(row.getString("user_name"));
            user.setPassword(row.getString("password"));
            user.setRegisterDate(row.getTimestamp("register_date"));
            user.setEmail(row.getString("email"));

            users.add(user);
        });

        return users;
    }

    void addUser(String userName, String password, String email) {
        PreparedStatement preparedStatement = session.prepare("INSERT INTO " + tableName +
                " (user_id, user_name, password, register_date, email) VALUES (?, ?, ?, ?, ?)");

        ResultSet resultSet = session.execute(preparedStatement.bind(UUID.randomUUID(), userName, password, null, email));

    }
}
