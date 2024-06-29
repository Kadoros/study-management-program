package Classes;

import javax.swing.*;

import mainPage.EducationAppGUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassEnterGUI extends JFrame {

    private JLabel welcomeLabel;
    private JTextField classCodeTextField;
    private JButton enterButton;
    private JLabel teacherLabel;
    private int userKey;
    private String ClassEnterCode;

    private int classID;
    private String className;
    private int classOwnerUserKey;
    private String classMaxStu;
    private String classExpirationDate;

    private EducationAppGUI educationAppGUI;

    public ClassEnterGUI(int userKey2) {
        this.userKey = userKey2;
        initializeComponents();

        setTitle("Class GUI");
        setSize(330, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null); // Set null layout to manually position the components

        // Welcome Label
        welcomeLabel.setBounds(10, 20, 300, 20);

        // Class Code Text Field
        classCodeTextField.setBounds(10, 60, 300, 20);

        // Enter Button
        enterButton.setBounds(10, 100, 300, 20);
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClassEnterCode = classCodeTextField.getText();
                System.out.println("classCodeTextField" + ClassEnterCode);
                searchClass(ClassEnterCode);

            }

        });

        // Teacher Label
        teacherLabel.setBounds(10, 140, 300, 20);
        teacherLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Font font = teacherLabel.getFont();
                teacherLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Font font = teacherLabel.getFont();
                teacherLabel.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
            }
        });
        Font font = teacherLabel.getFont();
        teacherLabel.setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));

        teacherLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Open a new frame
                ClassPay ClassPay = new ClassPay(userKey2);
                ClassPay.setTitle("PAY");
                ClassPay.setLocationRelativeTo(null); // Set the frame to open in the center of the screen
                ClassPay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ClassPay.setSize(800, 600); // Set the frame size

                // Close the current frame
                dispose();
            }
        });

        // Add components to the frame
        add(welcomeLabel);
        add(classCodeTextField);
        add(enterButton);
        add(teacherLabel);

    }

    private void initializeComponents() {
        welcomeLabel = new JLabel("Welcome! Enter your class code:");
        classCodeTextField = new JTextField();
        enterButton = new JButton("Enter");
        teacherLabel = new JLabel("Are you a teacher? Form your class!");
    }

    private void searchClass(String classEnterCodes) {
        int flag = 0;
        System.out.println(classEnterCodes);
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            String query = "SELECT * FROM class WHERE classEnterCode = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, classEnterCodes);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                

                classID = resultSet.getInt("classId");
                className = resultSet.getString("className");
                classOwnerUserKey = resultSet.getInt("classOwnerUserKey");
                classMaxStu = resultSet.getString("classMaxStu");
                classExpirationDate = resultSet.getString("classExpirationDate");

        
                flag = 1;

                // Perform actions with the classObject
            } else {
                // Class does not exist
                welcomeLabel.setText("Invalid class enter code");
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (flag == 1) {
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

                String query = "SELECT * FROM class WHERE classEnterCode = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, classEnterCodes);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Class exists, perform necessary actions
                    classID = resultSet.getInt("classID");
                    className = resultSet.getString("className");

                    // Update the user_info table with the class details
                    String updateUserQuery = "UPDATE user_info SET classId = ?, class = ? WHERE userKey = ?";
                    PreparedStatement updateUserStatement = con.prepareStatement(updateUserQuery);
                    updateUserStatement.setInt(1, classID);
                    updateUserStatement.setString(2, className);
                    updateUserStatement.setInt(3, userKey);
                    updateUserStatement.executeUpdate();

                    // Display a success message or perform any additional actions
                    String successMessage = "Successfully entered the class: " + className + " please restart program";
                    JOptionPane.showMessageDialog(null, successMessage);
                    dispose();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
