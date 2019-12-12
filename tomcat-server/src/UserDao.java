import com.datastax.driver.core.*;

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
            user.setFirstName(row.getString("first_name"));
            user.setLastName(row.getString("last_name"));
            user.setPassword(row.getString("password"));
            user.setEmail(row.getString("email"));
            //user.setRegisterDate((Date) row.getDate("register_date"));

            users.add(user);
        });

        return users;
    }

    User registerUser(RegisterUser user) {
        UUID userId = UUID.randomUUID();

        PreparedStatement preparedStatement = session.prepare("INSERT INTO " + tableName +
                " (user_id, first_name, last_name, email, password, register_date) VALUES (?, ?, ?, ?, ?, ?)");

        session.execute(preparedStatement.bind(userId, user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), new Date()));

        return getUser(userId);
    }

    User getUser(UUID userId) {
        // Allow filtering enables search by user_name, avoid this by including user_name in table primary key
        PreparedStatement preparedStatement = session.prepare("SELECT * FROM " + tableName + " WHERE user_id = " + userId);
        ResultSet resultSet = session.execute(preparedStatement.bind());

        // Populate User with DB results
        User user = new User();
        Row row = resultSet.one();
        user.setUserId(row.getUUID("user_id"));
        user.setFirstName(row.getString("first_name"));
        user.setLastName(row.getString("last_name"));
        user.setPassword(row.getString("password"));
        user.setEmail(row.getString("email"));
        //user.setRegisterDate(row.getDate("register_date"));

        return user;
    }
}
