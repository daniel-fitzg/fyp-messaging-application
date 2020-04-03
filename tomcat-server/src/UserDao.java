import com.datastax.driver.core.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

// TODO For each table add comment block specifying columns

class UserDao {
    final String tableName = "users";
    private Session session;

    UserDao(Session session) {
        this.session = session;
    }

    List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        PreparedStatement preparedStatement = session.prepare("SELECT * FROM " + tableName);
        ResultSet resultSet = session.execute(preparedStatement.bind());

        resultSet.forEach(row -> {
            User user = new User();
            user.setUserId(row.getUUID("user_id"));
            user.setFirstName(row.getString("first_name"));
            user.setLastName(row.getString("last_name"));
            user.setPassword(row.getString("password"));
            user.setUsername(row.getString("username"));
            user.setRegisterDate(row.getTimestamp("register_date"));
            user.setOnlineStatus(row.getBool("online_status"));

            users.add(user);
        });

        return users;
    }

    User registerUser(RegisterUser newUser) {
        List<User> users = getAllUsers();
        String newUserUsername = newUser.getUsername();

        // Checks if username already exists in the users table
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(newUserUsername)) {
                System.out.println("User already exists");
                return null;
            }
        }

        // Writes new user data to DB
        UUID userId = UUID.randomUUID();
        PreparedStatement preparedStatement = session.prepare("INSERT INTO " + tableName +
                " (user_id, first_name, last_name, username, password, register_date, online_status) VALUES (?, ?, ?, ?, ?, ?, ?)");

        session.execute(preparedStatement.bind(userId, newUser.getFirstName(), newUser.getLastName(), newUser.getUsername(),
                newUser.getPassword(), new Date(), true));

        // Reads new user entry from DB and returns user object
        return getUser(userId);
    }

    User authenticateUser(User existingUser) {
        List<User> users = getAllUsers();
        String existingUserUsername = existingUser.getUsername();

        for (User user : users) {

            // Checks if username exists in table
            if (user.getUsername().equalsIgnoreCase(existingUserUsername)) {
                // Checks if user password matches entry in table
                if (user.getPassword().equalsIgnoreCase(existingUser.getPassword())) {
                    return updateUserOnlineStatus(user);
                } else {
                    return null;
                }
            }
        }

        return null;
    }

    private User updateUserOnlineStatus(User user) {
        PreparedStatement changeOnlineStatusTrue = session.prepare("UPDATE " + tableName + " SET online_status = " + true +
                " WHERE user_id = " + user.getUserId());
        session.execute(changeOnlineStatusTrue.bind());

        return getUser(user.getUserId());
    }

    void logoutUser(UUID userId) {
        PreparedStatement changeOnlineStatusFalse = session.prepare("UPDATE " + tableName + " SET online_status = " + false +
                " WHERE user_id = " + userId);
        session.execute(changeOnlineStatusFalse.bind());
    }

    User getUser(UUID userId) {
        PreparedStatement preparedStatement = session.prepare("SELECT * FROM " + tableName + " WHERE user_id = " + userId);
        ResultSet resultSet = session.execute(preparedStatement.bind());

        // Populate User with DB results
        User user = new User();
        Row row = resultSet.one();
        user.setUserId(row.getUUID("user_id"));
        user.setFirstName(row.getString("first_name"));
        user.setLastName(row.getString("last_name"));
        user.setPassword(row.getString("password"));
        user.setUsername(row.getString("username"));
        user.setRegisterDate(row.getTimestamp("register_date"));
        user.setOnlineStatus(row.getBool("online_status"));

        return user;
    }
}
