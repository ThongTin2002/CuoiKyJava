package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class AdminDAO {

	//kiểm tra admin tồn tại chưa bằng cách so sánh mật khẩu vs csdl
    public boolean validateAdmin(String adminname, String password) throws SQLException {
        String query = "SELECT password FROM admin WHERE admin_name = ?";
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, adminname);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                return BCrypt.checkpw(password, hashedPassword);
            }
        }
        return false;
    }
    public boolean registerAdmin(String adminname, String password) throws SQLException {
        String checkQuery = "SELECT * FROM admin WHERE admin_name = ?";
        String insertQuery = "INSERT INTO admin (admin_name, password) VALUES (?, ?)";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            checkStatement.setString(1, adminname);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                return false; // adminname already exists
            } else {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                insertStatement.setString(1, adminname);
                insertStatement.setString(2, hashedPassword);
                insertStatement.executeUpdate();
                return true;
            }
        }
    }
    public List<String[]> getAllUsers() {
        List<String[]> users = new ArrayList<>();
        String query = "SELECT user_name FROM user";

        try (Connection connection = DBUtils.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String username = rs.getString("user_name");
                users.add(new String[]{username});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
    public void deleteUser(String username) {
        String query = "DELETE FROM user WHERE user_name = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
