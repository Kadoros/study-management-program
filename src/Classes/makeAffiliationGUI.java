package Classes;

import sql.sql;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

public class makeAffiliationGUI extends JFrame {
    private static int userKey;
    private static JLabel userInfoLabel;
    private JPanel centerPanel;

    private JLabel affiliationIdLabel;
    private JTextField affiliationIdField;
    private JLabel affiliationNameLabel;
    private JTextField affiliationNameField;
    private JLabel affiliationOwnerUserKeyLabel;
    private JTextField affiliationOwnerUserKeyField;
    private JLabel affiliationExpirationDateLabel;
    private JDateChooser affiliationExpirationDateChooser;
    private JButton setButton;
    private JButton cancelButton;

    private String affiliationIdValue;
    private String affiliationNameValue;
    private String affiliationOwnerUserKeyValue;
    private String affiliationExpirationDateValue;

    private JLabel affiliationCodeLabel;
    private JTextField affiliationCodeField;

    public makeAffiliationGUI(int userKey2) {
        this.userKey = userKey2;

        setTitle("Set Affiliation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(440, 300);
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
        getContentPane().add(mainPanel);

        affiliationIdLabel = new JLabel("Affiliation ID:");
        affiliationIdLabel.setBounds(10, 10, 100, 20);
        centerPanel.add(affiliationIdLabel);

        affiliationIdField = new JTextField(affiliationIdValue);
        affiliationIdField.setBounds(220, 10, 200, 20);
        centerPanel.add(affiliationIdField);
        affiliationIdField.setEditable(false);

        affiliationNameLabel = new JLabel("Affiliation Name:");
        affiliationNameLabel.setBounds(10, 40, 100, 20);
        centerPanel.add(affiliationNameLabel);

        affiliationNameField = new JTextField(affiliationNameValue);
        affiliationNameField.setBounds(220, 40, 200, 20);
        centerPanel.add(affiliationNameField);

        affiliationOwnerUserKeyLabel = new JLabel("Affiliation Owner User Key:");
        affiliationOwnerUserKeyLabel.setBounds(10, 70, 200, 20);
        centerPanel.add(affiliationOwnerUserKeyLabel);

        affiliationOwnerUserKeyField = new JTextField(affiliationOwnerUserKeyValue);
        affiliationOwnerUserKeyField.setBounds(220, 70, 200, 20);
        centerPanel.add(affiliationOwnerUserKeyField);
        affiliationOwnerUserKeyField.setEditable(false);

        affiliationExpirationDateLabel = new JLabel("Affiliation Expiration Date:");
        affiliationExpirationDateLabel.setBounds(10, 100, 200, 20);
        centerPanel.add(affiliationExpirationDateLabel);

        affiliationCodeLabel = new JLabel("Affiliation Code:");
        affiliationCodeLabel.setBounds(10, 130, 200, 20);
        centerPanel.add(affiliationCodeLabel);

        affiliationCodeField = new JTextField();
        affiliationCodeField.setBounds(220, 130, 200, 20);
        centerPanel.add(affiliationCodeField);

        affiliationExpirationDateChooser = new JDateChooser();
        affiliationExpirationDateChooser.setDateFormatString("dd/MM/yyyy");
        affiliationExpirationDateChooser.setBounds(220, 100, 200, 20);
        affiliationExpirationDateChooser.getDateEditor().getUiComponent()
                .addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent evt) {
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
        JCalendar calx = affiliationExpirationDateChooser.getJCalendar();
        calx.setCalendar(getPrevDate());
        centerPanel.add(affiliationExpirationDateChooser);
        if (affiliationExpirationDateValue != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                if (affiliationExpirationDateValue != null) {
                    Date affiliationExpirationDate = dateFormat.parse(affiliationExpirationDateValue);
                    affiliationExpirationDateChooser.setDate(affiliationExpirationDate);
                }
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date defaultDate = dateFormat.parse("01/01/2023");
                affiliationExpirationDateChooser.setDate(defaultDate);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        setButton = new JButton("Set Affiliation");
        setButton.setBounds(120, 160, 120, 20);
        centerPanel.add(setButton);
        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSetButtonAction();
            }
        });

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(260, 160, 80, 20);
        centerPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        AffiliationDefault();
        setVisible(true);
    }

    private void getInfo() {
        sql sql = new sql();
        sql.getUserInfo(userKey);
        String username = sql.getUsername();
        String classValue = sql.getClassValue();
        String userRoleL = sql.getUserRoleL();
        userInfoLabel.setText("Username: " + username + " | Class: " + classValue + " | Role: " + userRoleL);
    }

    private void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    private Calendar getPrevDate() {
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd");
        Date myDate = new Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.YEAR, -10);
        return cal;
    }

    private void setAffiliation() {
        String affiliationName = affiliationNameField.getText();
        String affiliationCode = affiliationCodeField.getText(); // Get the affiliation code value
        String affiliationExpirationDate = "";
        Date date = affiliationExpirationDateChooser.getDate();
        String affiliationId = affiliationIdField.getText();
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            affiliationExpirationDate = dateFormat.format(date);
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            String insertQuery = "INSERT INTO affiliation (affiliationName, affiliationOwnerUserKey, affiliationExpirationDate, affiliationCode,affiliationId) VALUES (?, ?, ?, ?,?)";
            PreparedStatement pstmt = con.prepareStatement(insertQuery);

            pstmt.setString(1, affiliationName);
            pstmt.setInt(2, userKey);
            pstmt.setString(3, affiliationExpirationDate);
            pstmt.setString(4, affiliationCode); // Set the affiliation code value
            pstmt.setString(5, affiliationId); // Set the affiliation code value

            pstmt.executeUpdate();

            pstmt.close();
            con.close();

            JOptionPane.showMessageDialog(null, "Affiliation set successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            affiliationNameField.setText("");
            affiliationCodeField.setText(""); // Clear the affiliation code field

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to set affiliation: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            String updateQuery = "UPDATE user_info SET userRole = 'AO'WHERE userKey = ?";
            PreparedStatement pstmt = con.prepareStatement(updateQuery);

            pstmt.setInt(1, userKey);

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

    protected void handleSetButtonAction() {
        setAffiliation();
        setVisible(false);
    }

    private void AffiliationDefault() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            // Retrieve the highest number in affiliationId column
            String selectQuery = "SELECT MAX(affiliationId) FROM affiliation";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            int highestValue = 0;
            if (rs.next()) {
                highestValue = rs.getInt(1);
            }

            rs.close();
            stmt.close();
            con.close();

            // Set affiliationIdField as highest number + 1
            affiliationIdField.setText(String.valueOf(highestValue + 1));

            // Set affiliationOwnerUserKeyField as userKey
            sql sql = new sql();
            sql.getUserInfo(userKey);
            affiliationOwnerUserKeyField.setText(sql.getUsername());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            makeAffiliationGUI setAffiliation = new makeAffiliationGUI(1);
            setAffiliation.setVisible(true);
        });
    }
}
