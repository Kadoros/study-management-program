package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class makeAffiliationClassGUI extends makeClassGUI {
    private int classAffiliationId;
    private String classAffiliationName;

    private JLabel classAffiliationIdLabel;
    private JLabel classAffiliationNameLabel;

    private JTextArea classAffiliationIdTextArea;
    private JTextArea classAffiliationNameTextArea;

    private String affiliationCode;

    public makeAffiliationClassGUI(int userKeys, String affiliationCodes) {
        super(userKeys); // Call the constructor of the superclass
        this.affiliationCode = affiliationCodes;

        classAffiliationIdLabel = new JLabel("Class Affiliation ID:");
        classAffiliationIdLabel.setBounds(10, 190, 200, 20);
        centerPanel.add(classAffiliationIdLabel);

        classAffiliationIdTextArea = new JTextArea();
        classAffiliationIdTextArea.setBounds(220, 190, 200, 20);
        centerPanel.add(classAffiliationIdTextArea);

        classAffiliationNameLabel = new JLabel("Class Affiliation Name:");
        classAffiliationNameLabel.setBounds(10, 220, 200, 20);
        centerPanel.add(classAffiliationNameLabel);

        classAffiliationNameTextArea = new JTextArea();
        classAffiliationNameTextArea.setBounds(220, 220, 200, 20);
        centerPanel.add(classAffiliationNameTextArea);
        setAffiliation();

    }

    public void setAffiliation() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            String selectQuery = "SELECT affiliationId, affiliationName FROM affiliation WHERE affiliationCode = ?";
            PreparedStatement pstmt = con.prepareStatement(selectQuery);
            pstmt.setString(1, affiliationCode);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                classAffiliationId = rs.getInt("affiliationId");
                classAffiliationName = rs.getString("affiliationName");
            }

            rs.close();
            pstmt.close();
            con.close();

            // Set the values in the text areas
            classAffiliationIdTextArea.setText(String.valueOf(classAffiliationId));
            classAffiliationNameTextArea.setText(classAffiliationName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setClassExtra() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            // Update query to set classAffiliationName and classAffiliationId6
            String updateQuery = "UPDATE class SET classAffiliationName = ?, classAffiliationId = ? WHERE classOwnerUserKey = ?";
            PreparedStatement pstmt = con.prepareStatement(updateQuery);

            // Set the values for the prepared statement
            pstmt.setString(1, classAffiliationName); // Set the value of classAffiliationName
            pstmt.setInt(2, classAffiliationId); // Set the value of classAffiliationId
            pstmt.setInt(3, userKey); // Set the value of userKey

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Class affiliation updated successfully.");
            } else {
                System.out.println("No rows were affected.");
            }

            pstmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void handleSetButtonAction() {
        super.handleSetButtonAction(); // Call the superclass method
        setClassExtra();
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            makeAffiliationClassGUI affiliationClassGUI = new makeAffiliationClassGUI(1, "qwe");
            affiliationClassGUI.setVisible(true);
        });
    }
}
