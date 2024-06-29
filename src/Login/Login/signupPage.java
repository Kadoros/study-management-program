package Login.Login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sec.SHA256;

public class signupPage extends JFrame implements ActionListener {
    JButton playButton, signupButton, backButton;
    JPanel newPanel;
    JLabel userLabel, passLabel, imageLabel, signalLabel, agpassLabel, nickLabel;
    final JTextField textField1, textField2, textField3, textField4;
    public int i = 0;

    public enum Actions {
        PLAY,
        SIGNUP,
        BACK
    }

    public signupPage() throws IOException {
        CreateLoginForm.IDar = null;
        CreateLoginForm.Passwordar = null;
        CreateLoginForm.nicknamear = null;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        String path = System.getProperty("user.dir") + "\\src\\resuorces\\5050.png";
        File file = new File(path);
        BufferedImage image = ImageIO.read(file);

        imageLabel = new JLabel(new ImageIcon(image));

        userLabel = new JLabel();
        userLabel.setText("Email");
        agpassLabel = new JLabel("Confirm Password");
        nickLabel = new JLabel("Name");

        nickLabel.setBounds(520, 300, 300, 50);
        nickLabel.setFont(new Font("Arial", Font.BOLD, 20));

        textField1 = new JTextField(15);
        textField4 = new JTextField(45);
        textField4.setBounds(520, 350, 300, 50);

        passLabel = new JLabel();
        passLabel.setText("Password");

        textField2 = new JPasswordField(15);
        textField3 = new JPasswordField(15);

        playButton = new JButton("SIGN UP");
        playButton.setBackground(Color.lightGray);
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.addActionListener(this);

        newPanel = new JPanel();
        newPanel.setLayout(null);
        newPanel.add(userLabel);

        userLabel.setBounds(520, 0, 300, 50);
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newPanel.add(textField1);

        textField1.setBounds(520, 50, 300, 50);
        newPanel.add(passLabel);

        passLabel.setBounds(520, 100, 300, 50);
        passLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newPanel.add(textField2);

        textField2.setBounds(520, 150, 300, 50);
        newPanel.add(playButton);

        playButton.setBounds(575, 470, 180, 60);
        newPanel.add(imageLabel);

        imageLabel.setBounds(0, 0, 450, 600);

        signalLabel = new JLabel();
        signalLabel.setBounds(520, 400, 300, 50);
        signalLabel.setText("Enter your Email and password");
        signalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newPanel.add(signalLabel);

        signupButton = new JButton("SIGN UP");
        signupButton.setBounds(780, 20, 90, 30);
        signupButton.setBackground(Color.lightGray);
        signupButton.setFont(new Font("Arial", Font.BOLD, 13));

        backButton = new JButton("X");
        backButton.setBounds(500, 470, 60, 60);
        backButton.setBackground(Color.lightGray);
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setActionCommand(Actions.BACK.name());
        backButton.addActionListener(this);
        newPanel.add(backButton);

        signupButton.setActionCommand(Actions.SIGNUP.name());
        signupButton.addActionListener(this);

        agpassLabel.setBounds(520, 200, 300, 50);
        agpassLabel.setFont(new Font("Arial", Font.BOLD, 20));
        textField3.setBounds(520, 250, 300, 50);

        newPanel.add(agpassLabel);
        newPanel.add(textField3);

        this.add(newPanel);
        this.setResizable(false);
        this.setSize(dimension);
        this.setLocationRelativeTo(null);
        newPanel.add(nickLabel);
        newPanel.add(textField4);

        add(newPanel, BorderLayout.CENTER);

        playButton.setActionCommand(Actions.PLAY.name());
        setTitle("LOGIN");

        textField1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    signup();
                }
            }
        });

        textField2.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    signup();
                }
            }
        });

    }

    public static String userValue;
    public static String passValue;
    public static String agpassValue;
    public static String nickValue;
    CreateLoginForm newSignupPage = new CreateLoginForm();

    public void signup() {
        userValue = textField1.getText();
        passValue = textField2.getText();
        agpassValue = textField3.getText();
        nickValue = textField4.getText();
        if (!userValue.equals("")) {

            if (!passValue.equals("")) {

                if (!agpassValue.equals("")) {
                    if (!nickValue.equals("")) {
                        if (passValue.equals(agpassValue)) {
                            if (isValidEmail(userValue)) { // Verify email format
                                this.setVisible(false);
                                addUser();
                                newSignupPage.setVisible(true);
                                newSignupPage.setLocationRelativeTo(null);
                                newSignupPage.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                                newSignupPage.setTitle("LOGIN");
                                newSignupPage.setSize(900, 600);
                                newSignupPage.setLocationRelativeTo(null);
                                newSignupPage.setResizable(false);
                                this.setVisible(false);
                            } else {
                                signalLabel.setText("Invalid email format");
                            }
                        } else {
                            signalLabel.setText("Check confirm password and password");
                        }
                    } else {
                        signalLabel.setText("Name is empty");
                    }
                } else {
                    signalLabel.setText("Confirm password is empty");
                }
            } else {
                signalLabel.setText("Password is empty");
            }
        } else {
            signalLabel.setText("Email is empty");
        }
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand() == Actions.PLAY.name()) {
            signup();
            System.out.println("signupPage.actionPerformed(play)");
        }
        if (evt.getActionCommand() == Actions.BACK.name()) {
            this.setVisible(false);
            newSignupPage.setVisible(true);
            newSignupPage.setLocationRelativeTo(null);
            newSignupPage.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            newSignupPage.setTitle("LOGIN");
            newSignupPage.setSize(900, 600);
            newSignupPage.setLocationRelativeTo(null);
            newSignupPage.setResizable(false);
            this.setVisible(false);
            System.out.println("signupPage.actionPerformed(back)");
        }
    }

    private void addUser() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado", "1130Coolhan@");

            String getMaxKeyQuery = "SELECT MAX(userKey) FROM user_info";
            java.sql.Statement getMaxKeyStatement = con.createStatement();
            ResultSet maxKeyResult = getMaxKeyStatement.executeQuery(getMaxKeyQuery);
            int highestKey = 0;
            if (maxKeyResult.next()) {
                highestKey = maxKeyResult.getInt(1);
            }
            getMaxKeyStatement.close();

            String query = "INSERT INTO user_info (userKey, Email, pass, name, userRole, class) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, highestKey + 1);
            statement.setString(2, userValue);
            String passenc = SHA256.encrSHA(passValue);
            statement.setString(3, passenc);
            statement.setString(4, nickValue);
            statement.setString(5, "S");
            statement.setString(6, "non");
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }

            statement.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado", "1130Coolhan@");

            String getMaxKeyQuery = "SELECT MAX(userKey) FROM user_info";
            java.sql.Statement getMaxKeyStatement = con.createStatement();
            ResultSet maxKeyResult = getMaxKeyStatement.executeQuery(getMaxKeyQuery);
            int highestKey = 0;
            if (maxKeyResult.next()) {
                highestKey = maxKeyResult.getInt(1);
            }
            getMaxKeyStatement.close();

            String query = "INSERT INTO grade (userKey, topic1, topic2, topic3, topic4, topic5, topic6) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, highestKey);
            statement.setInt(2, 0);
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            statement.setInt(5, 0);
            statement.setInt(6, 0);
            statement.setInt(7, 0);
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }

            statement.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
