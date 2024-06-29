package Classes;

import sql.sql;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import java.util.Calendar;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class makeClassGUI extends JFrame {
    public static int userKey;
    private static JLabel userInfoLabel;
    public JPanel centerPanel;

    private JLabel classIdLabel;
    private JTextArea classIdArea;
    private JLabel classNameLabel;
    public static JTextField classNameField;
    private JLabel classOwnerUserKeyLabel;
    private JTextArea classOwnerUserKeyArea;
    private JLabel classExpirationDateLabel;
    public static JDateChooser classExpirationDateChooser;
    private JLabel classMaxStudentsLabel;
    public static JTextField classMaxStudentsField;
    private JButton searchButton;
    private JLabel classEnterCodeLabel;
    public static JTextField classEnterCodeField;
    private JButton searchButton2;
    private JButton setButton;
    private JButton cancelButton;

    public static String classIdValue;
    public static String classNameValue;
    public static String classOwnerUserKeyValue;
    public static String classExpirationDateValue;
    public static String classMaxStudentsValue;
    public static String classEnterCodeValue;

    public static String classIdString;
    public static String className;

    public makeClassGUI(int userKey2) {
        this.userKey = userKey2;

        setTitle("Set Class");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(440, 550);
        centreWindow(this);


        centerPanel = new JPanel();
        centerPanel.setLayout(null);

        JPanel infoPanel = new JPanel();

        userInfoLabel = new JLabel();

        getInfo();
        infoPanel.add(userInfoLabel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        getContentPane().add(mainPanel);

        // Add action listeners

        classIdLabel = new JLabel("Class ID:");
        classIdLabel.setBounds(10, 10, 100, 20);
        centerPanel.add(classIdLabel);

        classIdArea = new JTextArea();
        classIdArea.setBounds(220, 10, 200, 20);
        centerPanel.add(classIdArea);

        classIdArea.setEditable(false);

        classNameLabel = new JLabel("Class Name:");
        classNameLabel.setBounds(10, 40, 100, 20);
        centerPanel.add(classNameLabel);

        classNameField = new JTextField(classNameValue);
        classNameField.setBounds(220, 40, 200, 20);

        centerPanel.add(classNameField);

        classOwnerUserKeyLabel = new JLabel("Class Owner User Name:");
        classOwnerUserKeyLabel.setBounds(10, 70, 200, 20);
        centerPanel.add(classOwnerUserKeyLabel);

        classOwnerUserKeyArea = new JTextArea();
        classOwnerUserKeyArea.setBounds(220, 70, 200, 20);
        centerPanel.add(classOwnerUserKeyArea);

        classOwnerUserKeyArea.setEditable(false);

        classExpirationDateLabel = new JLabel("Class Expiration Date:");
        classExpirationDateLabel.setBounds(10, 100, 200, 20);
        centerPanel.add(classExpirationDateLabel);

        classExpirationDateChooser = new JDateChooser();
        classExpirationDateChooser.setDateFormatString("dd/MM/yyyy");
        classExpirationDateChooser.setBounds(220, 100, 200, 20);
        classExpirationDateChooser.getDateEditor().getUiComponent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (evt.getSource() instanceof JTextComponent) {
                    final JTextComponent textComponent = ((JTextComponent) evt.getSource());
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            textComponent.selectAll();
                        }
                    });
                }
            }
        });
        JCalendar calx = classExpirationDateChooser.getJCalendar();
        calx.setCalendar(getPrevDate());
        centerPanel.add(classExpirationDateChooser);
        if (classExpirationDateValue != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                if (classExpirationDateValue != null) {
                    Date classExpirationDate = dateFormat.parse(classExpirationDateValue);
                    classExpirationDateChooser.setDate(classExpirationDate);
                }

            } catch (ParseException e) {
                // Handle the parse exception here
                e.printStackTrace();
            }
        } else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date defaultDate = dateFormat.parse("01/01/2023"); // Set your desired default date here
                classExpirationDateChooser.setDate(defaultDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        classMaxStudentsLabel = new JLabel("Class Max Students:");
        classMaxStudentsLabel.setBounds(10, 130, 200, 20);
        centerPanel.add(classMaxStudentsLabel);

        classMaxStudentsField = new JTextField(classMaxStudentsValue);
        classMaxStudentsField.setBounds(220, 130, 200, 20);

        centerPanel.add(classMaxStudentsField);

        classEnterCodeLabel = new JLabel("Class Enter Code:");
        classEnterCodeLabel.setBounds(10, 160, 200, 20);
        centerPanel.add(classEnterCodeLabel);

        classEnterCodeField = new JTextField(classEnterCodeValue);
        classEnterCodeField.setBounds(220, 160, 200, 20);

        centerPanel.add(classEnterCodeField);

        searchButton2 = new JButton("Search");
        searchButton2.setBounds(330, 160, 80, 20);
        centerPanel.add(searchButton2);

        setButton = new JButton("Set Class");
        setButton.setBounds(120, 440, 100, 20);
        centerPanel.add(setButton);
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSetButtonAction();
            }

        });

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(240, 440, 80, 20);
        centerPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }

        });

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        ClassDefault();

        setVisible(true);

    }

    public static void getInfo() {
        sql sql = new sql();
        sql.getUserInfo(userKey);

        // Call the getUserInfo method and retrieve the values
        String username = sql.getUsername();
        String Class = sql.getClassValue();
        String userRoleL = sql.getUserRoleL();

        userInfoLabel.setText(
                "Username: " + username + " | Class: " + Class + " | Role: " + userRoleL);
    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    private void ClassDefault() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            // Retrieve the highest number in classId column
            String selectQuery = "SELECT MAX(classId) FROM class";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            int highestValue = 0;
            if (rs.next()) {
                highestValue = rs.getInt(1);
            }

            rs.close();
            stmt.close();
            con.close();

            // Set classIdArea as highest number + 1
            classIdArea.setText(String.valueOf(highestValue + 1));

    
            sql sql = new sql();
            sql.getUserInfo(userKey);
            classOwnerUserKeyArea.setText(sql.getUsername());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setClass() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            // Retrieve the highest value in the classId column
            String selectQuery = "SELECT MAX(classId) FROM class";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            int highestValue = 0;
            if (rs.next()) {
                highestValue = rs.getInt(1);
            }
            rs.close();

            // Prepare the INSERT query to add the new class
            String insertQuery = "INSERT INTO class (classId, className, classOwnerUserKey, classExpirationDate, classMaxStu, classEnterCode) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertQuery);

            // Set the values for the prepared statement
            int classId = highestValue + 1;
            classIdString = String.valueOf(highestValue + 1);
            className = classNameField.getText();
            int classOwnerUserKey = userKey;
            Date classExpirationDate = classExpirationDateChooser.getDate();
            String classExpirationDateString = ""; // Initialize the classExpirationDateString variable
            if (classExpirationDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                classExpirationDateString = dateFormat.format(classExpirationDate);
            }
            String classMaxStudents = classMaxStudentsField.getText();
            String classEnterCode = classEnterCodeField.getText();
            String classAffiliationName = null;
            String classAffilationId = null;

            pstmt.setString(1, classIdString); // classId
            pstmt.setString(2, className); // className
            pstmt.setInt(3, classOwnerUserKey); // classOwnerUserKey
            pstmt.setString(4, classExpirationDateString); // classExpirationDate
            pstmt.setString(5, classMaxStudents); // classMaxStudents
            pstmt.setString(6, classEnterCode); // classEnterCode

            // Execute the INSERT query
            pstmt.executeUpdate();

            // Close the prepared statement and database connection
            pstmt.close();
            con.close();

            // Display a success message
            JOptionPane.showMessageDialog(null, "Class set successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear the input fields

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to set class: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            String updateQuery = "UPDATE user_info SET userRole = 'CO', class = ?, classId = ? WHERE userKey = ?";
            PreparedStatement pstmt = con.prepareStatement(updateQuery);
            String className = classNameField.getText();
            pstmt.setString(1, className);
            pstmt.setString(2, classIdString);
            pstmt.setInt(3, userKey);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User information updated successfully.");
            } else {
                System.out.println("No rows were affected.");
            }

            pstmt.close();
            con.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to set class: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private static Calendar getPrevDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date myDate = new Date(System.currentTimeMillis());
        System.out.println("result is " + dateFormat.format(myDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.YEAR, -10);
        System.out.println(dateFormat.format(cal.getTime()));
        return cal;
    }

    protected void handleSetButtonAction() {
        setClass();
        setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            makeClassGUI setClass;
            setClass = new makeClassGUI(1);
            setClass.setVisible(true);

        });
    }
}
