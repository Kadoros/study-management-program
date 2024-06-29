package Login.Login;

import sec.SHA256;

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
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import mainPage.EducationAppGUI;

public class CreateLoginForm extends JFrame implements ActionListener {

    public int flagReadLogin = 0;
    public static ArrayList<String> IDar = new ArrayList<String>();
    public static ArrayList<String> Passwordar = new ArrayList<String>();
    public static ArrayList<String> nicknamear = new ArrayList<String>();
    JButton loginButton, signupButton;
    JPanel newPanel;
    JLabel userLabel, passLabel, imageLabel, signalLabel;
    final JTextField textField1, textField2;
    public int i = 0;
    public static String nick;

    enum Actions {
        LOGIN,
        SIGNUP
    }

    // calling constructor
    public CreateLoginForm() throws IOException {

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        String path = System.getProperty("user.dir") + "\\src\\resuorces\\5050.png";
        File file = new File(path);
        BufferedImage image = ImageIO.read(file);

        imageLabel = new JLabel(new ImageIcon(image));

        // create label for username

        userLabel = new JLabel();
        userLabel.setText("Email"); // set label value for textField1

        // create text field to get username from the user
        textField1 = new JTextField(15); // set length of the text

        // create label for password
        passLabel = new JLabel();
        passLabel.setText("Passcode"); // set label value for textField2

        // create text field to get password from the user
        textField2 = new JPasswordField(15); // set length for the password

        // create submit button
        loginButton = new JButton("LOGIN"); // set label to button
        loginButton.setBackground(Color.lightGray);
        loginButton.setFont(new Font("Arial", Font.BOLD, 10));
        loginButton.addActionListener(this);
        // create panel to put form elements
        newPanel = new JPanel();
        newPanel.setLayout(null);
        newPanel.add(userLabel); // set username label to panel

        userLabel.setBounds(520, 80, 300, 50);
        userLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newPanel.add(textField1); // set text field to panel

        textField1.setBounds(520, 130, 300, 50);
        newPanel.add(passLabel); // set password label to panel

        passLabel.setBounds(520, 180, 300, 50);
        passLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newPanel.add(textField2); // set text field to panel

        textField2.setBounds(520, 230, 300, 50);
        newPanel.add(loginButton); // set button to panel

        loginButton.setBounds(575, 450, 180, 60);
        newPanel.add(imageLabel); // set image to panel

        imageLabel.setBounds(0, 0, 450, 600);

        signalLabel = new JLabel();
        signalLabel.setBounds(520, 320, 400, 50);
        signalLabel.setText(" enter your Email and Passcode");
        signalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newPanel.add(signalLabel);

        signupButton = new JButton("SIGN UP"); // set label to button
        signupButton.setBounds(780, 20, 90, 30);
        signupButton.setBackground(Color.lightGray);
        signupButton.setFont(new Font("Arial", Font.BOLD, 13));

        newPanel.add(signupButton); // set button to panel;
        signupButton.setActionCommand(Actions.SIGNUP.name());
        signupButton.addActionListener(this);

        this.add(newPanel);
        this.setResizable(false);
        this.setSize(dimension);
        this.setLocationRelativeTo(null);

        // set border to panel
        add(newPanel, BorderLayout.CENTER);

        // perform action on button click
        loginButton.setActionCommand(Actions.LOGIN.name()); // add action listener to button
        setTitle("LOGIN"); // set title to the login form
        textField1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    LoginPass();
                }
            }
        });
        textField2.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    LoginPass();
                }
            }
        });

    }

    // setter of i
    public String userValue; // get user entered username from the textField1
    public String passValue;

    public void ShowSignup() throws IOException {
        signupPage newSignupPage = new signupPage();

        newSignupPage.setVisible(true);
        newSignupPage.setLocationRelativeTo(null);
        newSignupPage.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        newSignupPage.setTitle("sign up");
        newSignupPage.setSize(900, 600);
        newSignupPage.setLocationRelativeTo(null);
        newSignupPage.setResizable(false);
        this.setVisible(false);

        IDar = null;
        Passwordar = null;
        nicknamear = null;

    }

    // define abstract method actionPerformed() which will be called on button click
    /**
     * @param evt
     */

    public void actionPerformed(ActionEvent evt) // pass action listener as a parameter
    {
        if (evt.getActionCommand() == Actions.SIGNUP.name()) {
            try {
                ShowSignup();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (evt.getActionCommand() == Actions.LOGIN.name()) {
            LoginPass();
        }
    }

    public void LoginPass() {
        userValue = textField1.getText();
        passValue = textField2.getText();
        int flag = 0;
        int userKey = 0; // Variable to store the userKey from the database
        String passenc;

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA",
                    "kado",
                    "1130Coolhan@");
            String query = "SELECT * FROM user_info WHERE Email = ? AND pass = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, userValue);
            passenc = SHA256.encrSHA(passValue);
            statement.setString(2, passenc);
            System.err.println(passenc);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                flag = 1;
                userKey = resultSet.getInt("userKey"); // Get the userKey value from the ResultSet
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (flag == 1) {
            // Successful login
            try {
                EducationAppGUI adminPanel = new EducationAppGUI(userKey); // Pass the userKey to the constructor
                adminPanel.setVisible(true);
                this.setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Invalid credentials
            System.out.println("Please enter valid Email and pass");
            System.out.println(userValue);
            System.out.println(passValue);
            signalLabel.setText("Enter valid Email and pass");
        }
    }
}
//I think it would be good to apply the basic prevention method for prevent SQL in SQL information
