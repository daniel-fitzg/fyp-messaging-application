package CassandraDB;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.Date;
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

        resultSet.forEach(row -> {
            User user = new User();
            user.setUserId(row.getUUID("user_id"));
            user.setUserName(row.getString("user_name"));
            user.setPassword(row.getString("password"));
            user.setRegisterDate(row.getTimestamp("register_date"));
            user.setEmail(row.getString("email"));

            users.add(user);
        });

        return users;
    }

    User addUser(String userName, String password, Date registerDate, String email) {
        PreparedStatement preparedStatement = session.prepare("INSERT INTO " + tableName +
                " (user_id, user_name, password, register_date, email) VALUES (?, ?, ?, ?, ?)");

        session.execute(preparedStatement.bind(UUID.randomUUID(), userName, password, registerDate, email));

        return getUser(userName);
    }

    User getUser(String userName) {
        // Allow filtering enables search by user_name, avoid this by including user_name in table primary key
        PreparedStatement preparedStatement = session.prepare("SELECT user_id FROM " + tableName + " WHERE user_name = '" + userName + "' ALLOW FILTERING;");
        ResultSet resultSet = session.execute(preparedStatement.bind());

        // Populate User with DB results
        User user = new User();
        Row row = resultSet.one();
        user.setUserId(row.getUUID("user_id"));
        user.setUserName(row.getString("user_name"));
        user.setPassword(row.getString("password"));
        user.setEmail(row.getString("email"));

        return user;
    }
}
