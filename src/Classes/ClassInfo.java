package Classes;

import sql.sql;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ClassInfo extends JFrame {
    private Connection con;
    private static int userKeys;
    private static int ClassID;
    private JLabel classIdLabel;
    private JLabel classNameLabel;
    private JLabel classOwnerUserNameLabel;
    private JLabel classExpirationDateLabel;
    private JLabel classMaxStuLabel;
    private JLabel classEnterCodeLabel;

    private static JLabel userInfoLabel;

    public ClassInfo(int userKey2) {
        this.userKeys = userKey2;
        initializeDatabaseConnection();
        createAndShowGUI();
        setLocationRelativeTo(null);
    }

    private void initializeDatabaseConnection() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle connection error
        }
    }

    private void createAndShowGUI() {
        // Create JFrame and set layout
        setTitle("class info");
        setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel();
        userInfoLabel = new JLabel();
        getInfo();
        infoPanel.add(userInfoLabel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        // Create JPanel for class details
        JPanel classDetailsPanel = new JPanel(new GridLayout(6, 2));

        classIdLabel = new JLabel();
        classNameLabel = new JLabel();
        classOwnerUserNameLabel = new JLabel();
        classExpirationDateLabel = new JLabel();
        classMaxStuLabel = new JLabel();
        classEnterCodeLabel = new JLabel();

        classDetailsPanel.add(new JLabel("Class ID:"));
        classDetailsPanel.add(classIdLabel);
        classDetailsPanel.add(new JLabel("Class Name:"));
        classDetailsPanel.add(classNameLabel);
        classDetailsPanel.add(new JLabel("Class Owner:"));
        classDetailsPanel.add(classOwnerUserNameLabel);
        classDetailsPanel.add(new JLabel("Expiration Date:"));
        classDetailsPanel.add(classExpirationDateLabel);
        classDetailsPanel.add(new JLabel("Max Students:"));
        classDetailsPanel.add(classMaxStuLabel);
        classDetailsPanel.add(new JLabel("Enter Code:"));
        classDetailsPanel.add(classEnterCodeLabel);

        // Add panels to the frame
        mainPanel.add(classDetailsPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        // Load class details
        loadClassDetails();
    }

    private void loadClassDetails() {
        try {
            // Fetch class details using userKeys
            String query = "SELECT classId, className, classOwnerUserKey, classExpirationDate, classMaxStu, classEnterCode "
                    +
                    "FROM class " +
                    "WHERE classId = ?";
            PreparedStatement statement = con.prepareStatement(query);

            statement.setInt(1, ClassID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                classIdLabel.setText(String.valueOf(resultSet.getInt("classId")));
                classNameLabel.setText(resultSet.getString("className"));

                int classOwnerUserKey = resultSet.getInt("classOwnerUserKey");
                String classOwnerUserName = getClassOwnerUserName(classOwnerUserKey);
                classOwnerUserNameLabel.setText(classOwnerUserName);

                classExpirationDateLabel.setText(resultSet.getString("classExpirationDate"));
                classMaxStuLabel.setText(resultSet.getString("classMaxStu"));
                classEnterCodeLabel.setText(resultSet.getString("classEnterCode"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }

    private String getClassOwnerUserName(int classOwnerUserKey) {
        String classOwnerUserName = "";
        try {
            // Fetch class owner name using classOwnerUserKey
            String query = "SELECT name FROM user_info WHERE userKey = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, classOwnerUserKey);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                classOwnerUserName = resultSet.getString("name");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
        return classOwnerUserName;
    }

    public static void getInfo() {
        sql sql = new sql();
        sql.getUserInfo(userKeys);

        // Call the getUserInfo method and retrieve the values
        String username = sql.getUsername();
        String Class = sql.getClassValue();
        String userRoleL = sql.getUserRoleL();
        ClassID = sql.getClssID();

        userInfoLabel.setText(
                "Username: " + username + " | Class: " + Class + " | Role: " + userRoleL);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClassInfo(1);
            }
        });
    }
}
