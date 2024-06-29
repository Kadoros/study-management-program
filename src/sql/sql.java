package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class sql {
    public static String username;
    public static String email;
    public static String Class;
    public static String userRole;
    public static String userRoleL;
    public static int classId;

    public void getUserInfo(int userKey) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA",
                    "kado", "1130Coolhan@");
            String query = "SELECT * FROM user_info WHERE userKey = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userKey);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve values from the result set
                username = resultSet.getString("name");
                email = resultSet.getString("Email");
                Class = resultSet.getString("class");
                userRole = resultSet.getString("userRole");
                classId = resultSet.getInt("classID");

                userRoleL = getUserRoleString(userRole);

            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getUserRoleString(String userRole) {
        if (userRole.equals("A")) {
            return "Admin";
        } else if (userRole.equals("T")) {
            return "Teacher";
        } else if (userRole.equals("S")) {
            return "Student";
        } else if (userRole.equals("CO")) {
            return "Class Owner";
        } else if (userRole.equals("AO")) {
            return "Affiliation Owner";
        } else {
            return "";
        }
    }

    public String getUsername() {
        System.out.println(username);
        return username;
    }

    public String getClassValue() {
        return Class;
    }

    public String getUserRoleL() {
        return userRoleL;
    }

    public String getUserEmail() {
        return email;
    }

    public int getClssID() {
        return classId;
    }

}
