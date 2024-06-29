package Classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import sql.sql;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ClassManege extends JFrame {
    private Connection con;
    private static int userKeys;

    private JLabel classIdLabel;
    private JLabel classNameLabel;
    private JLabel classOwnerUserNameLabel;
    private JLabel classExpirationDateLabel;
    private JLabel classMaxStuLabel;
    private JLabel classEnterCodeLabel;

    private JTable studentTable;
    private DefaultTableModel tableModel;

    private static JLabel userInfoLabel;

    public ClassManege(int userKey2) {
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
        setTitle("Student Management System");
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

        // Create JPanel for student table
        JPanel studentTablePanel = new JPanel(new BorderLayout());

        String[] columnNames = { "Student ID", "Student Name", "Email" };
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        studentTablePanel.add(scrollPane, BorderLayout.CENTER);

        // Create JPanel for buttons
        JPanel buttonPanel = new JPanel();

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStu();
            }
        });

        JButton kickButton = new JButton("kick Student");
        kickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kickStu();
            }
        });

        buttonPanel.add(editButton);
        buttonPanel.add(kickButton);

        // Add panels to the frame
        mainPanel.add(classDetailsPanel, BorderLayout.NORTH);
        mainPanel.add(studentTablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

        // Load class details and student table
        loadClassDetails();
        loadStudentTable();
    }

    private void loadClassDetails() {
        try {
            // Fetch class details using userKeys
            String query = "SELECT classId, className, classOwnerUserKey, classExpirationDate, classMaxStu, classEnterCode "
                    +
                    "FROM class " +
                    "WHERE classOwnerUserKey = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userKeys);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                classIdLabel.setText(resultSet.getString("classId"));
                classNameLabel.setText(resultSet.getString("className"));

                String classOwnerUserKey = resultSet.getString("classOwnerUserKey");
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

    private String getClassOwnerUserName(String classOwnerUserKey) {
        String classOwnerUserName = "";
        try {
            // Fetch class owner name using classOwnerUserKey
            String query = "SELECT name FROM user_info WHERE userKey = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, classOwnerUserKey);
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

    private void loadStudentTable() {
        tableModel.setRowCount(0); // Clear existing table rows

        try {
            // Fetch student details using className
            String query = "SELECT userKey, name, Email FROM user_info WHERE class = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, classNameLabel.getText());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String userKey = resultSet.getString("userKey");
                String name = resultSet.getString("name");
                String email = resultSet.getString("Email");

                Object[] rowData = { userKey, name, email };
                tableModel.addRow(rowData);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }

    public static void getInfo() {
        sql sql = new sql();
        sql.getUserInfo(userKeys);

        // Call the getUserInfo method and retrieve the values
        String username = sql.getUsername();
        String Class = sql.getClassValue();
        String userRoleL = sql.getUserRoleL();

        userInfoLabel.setText(
                "Username: " + username + " | Class: " + Class + " | Role: " + userRoleL);
    }

    private void kickStu() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            // No row is selected
            JOptionPane.showMessageDialog(null, "Please select a student to kick.", "No Student Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userKey = (String) tableModel.getValueAt(selectedRow, 0);

        try {
            String updateQuery = "UPDATE user_info SET class = 'non' WHERE userKey = ?";
            PreparedStatement pstmt = con.prepareStatement(updateQuery);
            pstmt.setString(1, userKey);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Student has been kicked.", "Student Kicked",
                        JOptionPane.INFORMATION_MESSAGE);
                loadStudentTable(); // Reload the student table after the update
            } else {
                JOptionPane.showMessageDialog(null, "Failed to kick student.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
    }

    private void editStu() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            // No row is selected
            JOptionPane.showMessageDialog(null, "Please select a student to edit.", "No Student Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userKey = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String email = (String) tableModel.getValueAt(selectedRow, 2);

        // Show a dialog to allow editing of student details
        JTextField nameField = new JTextField(name);
        JTextField emailField = new JTextField(email);

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Edit Student Details", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String updatedName = nameField.getText();
            String updatedEmail = emailField.getText();

            try {
                String updateQuery = "UPDATE user_info SET name = ?, Email = ? WHERE userKey = ?";
                PreparedStatement pstmt = con.prepareStatement(updateQuery);
                pstmt.setString(1, updatedName);
                pstmt.setString(2, updatedEmail);
                pstmt.setString(3, userKey);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Student details have been updated.", "Student Details Updated",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadStudentTable(); // Reload the student table after the update
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update student details.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database error
            }
        }

    }

    public void checkTasks() {
        System.err.println("s");
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClassManege(1);
            }
        });
    }
}
