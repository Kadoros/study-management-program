package analysis;

import javax.swing.*;
import java.awt.*;
import sql.sql;

public class PersonalAnalysis extends JFrame {
    private static JLabel userInfoLabel;
    private int userKey;
    private JPanel mainPanel;
    private JPanel upperPanel;
    private JPanel bottomPanel;
    private JPanel centerPanel;

    public PersonalAnalysis(int UserKey) {
        this.userKey = UserKey;
        setTitle("Personal Analysis");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        mainPanel = new JPanel(new BorderLayout());
        upperPanel = new JPanel();
        bottomPanel = new JPanel();
        centerPanel = new JPanel();

        userInfoLabel = new JLabel("ah");
        bottomPanel.add(userInfoLabel);

        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // getInfo(userKey);
    }

    public static void getInfo(int userKey) {
        sql sql = new sql();
        sql.getUserInfo(userKey);

        // Call the getUserInfo method and retrieve the values
        String username = sql.getUsername();
        String Class = sql.getClassValue();
        String userRoleL = sql.getUserRoleL();

        userInfoLabel.setText(
                "Username: " + username + " | Class: " + Class + " | Role: " + userRoleL);
    }

    public static void main(String[] args) {
        PersonalAnalysis personalAnalysis = new PersonalAnalysis(1);
        personalAnalysis.setVisible(true);
    }
}