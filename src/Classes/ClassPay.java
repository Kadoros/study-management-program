package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassPay extends JFrame {
    private static JTextField textField;
    private static JLabel label;

    private static JButton enterButton;
    private static int userKey;

    private static int affiliationId;
    private static String affiliationName;
    private static int affiliationOwnerUserKey;
    private static String affiliationCode;
    private static String affiliationExpirationDate;

    public ClassPay(int userKey2) {
        this.userKey = userKey2;
        setTitle("PAY");
        setLocationRelativeTo(null); // Set the frame to open in the center of the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600); // Set the frame size

        // Create the left box
        JPanel leftBox = createBoxPanel("Personal", "10€/3month",
                "Only one class\nCan use public exam set\nCan't make private exam set");

        JButton payButton = new JButton("Pay");
        payButton.setBounds(0, 410, 250, 40);
        leftBox.add(payButton);
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String textFieldvString = textField.getText();
                if (textFieldvString.equals("pay")) {
                    makeClassGUI makeClassGUI = new makeClassGUI(userKey2);
                    makeClassGUI.setVisible(true);

                }

            }
        });
        // Create the middle box
        JPanel middleBox = createBoxPanel("School", "500€/3month",
                "Up to 100 classes\nCan use public exam set\nCan make private exam set");
        JButton pay2Button = new JButton("Pay");
        pay2Button.setBounds(0, 410, 250, 40);
        middleBox.add(pay2Button);
        pay2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String textFieldvString = textField.getText();
                if (textFieldvString.equals("pay")) {
                    makeAffiliationGUI makeAffiliationGUI = new makeAffiliationGUI(userKey2);
                    makeAffiliationGUI.setVisible(true);

                }
            }
        });

        // Create the right box
        JPanel rightBox = createRightBoxPanel();

        textField = new JTextField();
        textField.setBounds(10, 40, 80, 20);
        rightBox.add(textField);

        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String textFieldvString = textField.getText();
                searchAffiliation(textFieldvString);
            }
        });

        // Set the layout of the frame to null, so we can manually position the boxes
        setLayout(null);

        // Set the bounds for each box
        leftBox.setBounds(50, 50, 250, 450);
        middleBox.setBounds(350, 50, 250, 450);
        rightBox.setBounds(650, 50, 100, 450);

        // Add the boxes to the frame
        add(leftBox);

        add(middleBox);
        add(rightBox);

        // Set the background colors for the boxes
        leftBox.setBackground(new Color(204, 255, 204));
        leftBox.setBackground(new Color(204, 255, 204));// Light green
        middleBox.setBackground(new Color(204, 229, 255)); // Light blue

        // Display the frame
        setVisible(true);
    }

    private static JPanel createBoxPanel(String subject, String cost, String details) {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Create the components for the box
        JLabel subjectLabel = new JLabel("Subject: " + subject);
        JLabel costLabel = new JLabel("Cost: " + cost);
        JTextArea detailsArea = new JTextArea("Details:\n" + details);

        // Set the font size and style
        Font labelFont = subjectLabel.getFont();
        Font boldFont = new Font(labelFont.getName(), Font.BOLD, 16);
        subjectLabel.setFont(boldFont);
        costLabel.setFont(boldFont);
        detailsArea.setFont(boldFont);

        // Set the bounds for each component
        subjectLabel.setBounds(10, 10, 200, 30);
        costLabel.setBounds(10, 50, 200, 30);
        detailsArea.setBounds(10, 90, 225, 100);

        

        // Add the components to the panel
        panel.add(subjectLabel);
        panel.add(costLabel);
        panel.add(detailsArea);

        return panel;
    }

  
    private static JPanel createRightBoxPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Create the components for the right box
        label = new JLabel("School Code");

        enterButton = new JButton("Enter");

        // Set the bounds for each component
        label.setBounds(10, 10, 80, 20);

        enterButton.setBounds(10, 70, 80, 30);

        // Add the components to the panel
        panel.add(label);

        panel.add(enterButton);

        return panel;
    }

    private void searchAffiliation(String AffilationEnterCodes) {
        int flag = 0;
        System.out.println(AffilationEnterCodes);
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            String query = "SELECT * FROM affiliation WHERE affiliationCode = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, AffilationEnterCodes);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                

                affiliationId = resultSet.getInt("affiliationId");
                affiliationName = resultSet.getString("affiliationName");
                affiliationOwnerUserKey = resultSet.getInt("affiliationOwnerUserKey");
                affiliationCode = resultSet.getString("affiliationCode");
                affiliationExpirationDate = resultSet.getString("affiliationExpirationDate");

               
                flag = 1;
                System.out.println(AffilationEnterCodes);
                makeAffiliationClassGUI makeAffiliationClassGUI = new makeAffiliationClassGUI(userKey,
                        AffilationEnterCodes);
                makeAffiliationClassGUI.setVisible(true);
                // Perform actions with the classObject
            } else {
                // Class does not exist
                label.setText("Invalid class enter code");
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
