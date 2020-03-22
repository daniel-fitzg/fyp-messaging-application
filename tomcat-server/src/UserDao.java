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
            user.setEmail(row.getString("email"));
            user.setRegisterDate(row.getTimestamp("register_date"));

            users.add(user);
        });

        return users;
    }

    User registerUser(RegisterUser newUser) {
        List<User> users = getAllUsers();
        String newUserEmail = newUser.getEmail();

        // Checks if user email alreay exists in the users table
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(newUserEmail)) {
                System.out.println("User already exists");
                return null;
            }
        }

        // Writes new user data to DB
        UUID userId = UUID.randomUUID();
        PreparedStatement preparedStatement = session.prepare("INSERT INTO " + tableName +
                " (user_id, first_name, last_name, email, password, register_date) VALUES (?, ?, ?, ?, ?, ?)");

        session.execute(preparedStatement.bind(userId, newUser.getFirstName(), newUser.getLastName(), newUser.getEmail(), newUser.getPassword(), new Date()));

        // Reads new user entry from DB and returns user object
        return getUser(userId);
    }

    User authenticateUser(User existingUser) {
        List<User> users = getAllUsers();
        String existingUserEmail = existingUser.getEmail();

        for (User user : users) {

            // Checks if user email exists in table
            if (user.getEmail().equalsIgnoreCase(existingUserEmail)) {
                // Checks if user password matches entry in table
                if (user.getPassword().equalsIgnoreCase(existingUser.getPassword())) {
                    return user;
                } else {
                    return null;
                }
            }
        }

        return null;
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
        user.setEmail(row.getString("email"));
        user.setRegisterDate(row.getTimestamp("register_date"));

        return user;
    }
}
