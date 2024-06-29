package TaskPage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import sql.sql;

public class setTask extends JFrame {
    public static int userKey;
    private static JLabel userInfoLabel;
    private JPanel centerPanel;

    private JLabel idTaskSQLLabel;
    private JTextArea idTaskSQLArea;
    private JLabel taskTitleLabel;
    private static JTextField taskTitleField;
    private JLabel taskDetailLabel;
    private static JTextArea taskDetailArea;
    private JLabel taskSetByLabel;
    private JTextArea taskSetByArea;
    private JLabel taskDueDayLabel;
    private static JTextField taskDueDayField;
    private static JDateChooser taskDueDayDateChooser;
    private JLabel taskInIdExamLabel;
    private static JTextField taskInIdExamField;
    private JButton searchButton;
    private JLabel taskTimeLabel;
    private static JTextField taskTimeField;
    private JLabel taskModeLabel;
    private static JCheckBox checkBox1;
    private static JCheckBox checkBox2;
    private JLabel taskForLabel;
    private static JTextArea taskForArea;
    private JButton searchButton2;
    private JButton setButton;
    private JButton cancelButton;

    public static int idTaskSQLvalue;
    public static String taskTitlevalue;
    public static String taskDetailvalue;
    public static String taskSetByvalue;
    public static String taskDueDayvalue;
    public static int taskInIdExamvalue;
    public static String taskTimevalue;
    public static String taskModevalue;
    public static ArrayList<String> taskForvalue;
    public static ArrayList<String> taskForShow;
    private String taskForShowString;

    public setTask(int userKey, ArrayList taskForSho, ArrayList taskForvalu, String title, String day, String time,
            String mode, String detail, int taskInIdExamvalu) {
        this.userKey = userKey;
        this.taskForShow = taskForSho;
        this.taskForvalue = taskForvalu;

        this.taskTitlevalue = title;
        this.taskDetailvalue = detail;

        this.taskDueDayvalue = day;
        this.taskInIdExamvalue = taskInIdExamvalu;
        this.taskTimevalue = time;
        this.taskModevalue = mode;

        setTitle("setTask");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(440, 550);
        centreWindow(this);
        if (taskForShow != null) {
            taskForShowString = String.join(", ", taskForShow);

        } else {
            taskForShowString = null;
        }
        // Create table model

        // Create scroll pane and add table to it
        centerPanel = new JPanel();
        centerPanel.setLayout(null);

        // Create buttons

        // Create upper panel

        JPanel infoPanel = new JPanel();

        userInfoLabel = new JLabel();

        getInfo();
        infoPanel.add(userInfoLabel, BorderLayout.WEST);

        // Add buttons to the buttons panel

        // Add components to the info panel

        // make detailPanel

        // Add components to the upper panel

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        getContentPane().add(mainPanel);

        // Add action listeners

        idTaskSQLLabel = new JLabel("idTaskSQL :");
        idTaskSQLLabel.setBounds(10, 10, 100, 20);
        centerPanel.add(idTaskSQLLabel);

        idTaskSQLArea = new JTextArea();
        idTaskSQLArea.setBounds(120, 10, 200, 20);
        centerPanel.add(idTaskSQLArea);

        idTaskSQLArea.setEditable(false);

        taskTitleLabel = new JLabel("taskTitle :");
        taskTitleLabel.setBounds(10, 40, 100, 20);
        centerPanel.add(taskTitleLabel);

        taskTitleField = new JTextField(title);
        taskTitleField.setBounds(120, 40, 200, 20);

        centerPanel.add(taskTitleField);

        taskSetByLabel = new JLabel("taskSetBy : ");
        taskSetByLabel.setBounds(10, 70, 100, 20);
        centerPanel.add(taskSetByLabel);

        taskSetByArea = new JTextArea();
        taskSetByArea.setBounds(120, 70, 200, 20);
        centerPanel.add(taskSetByArea);

        taskSetByArea.setEditable(false);

        taskDueDayLabel = new JLabel("taskDueDay : ");
        taskDueDayLabel.setBounds(10, 100, 100, 20);
        centerPanel.add(taskDueDayLabel);

        taskDueDayDateChooser = new JDateChooser();
        taskDueDayDateChooser.setDateFormatString("dd/MM/yyyy");
        taskDueDayDateChooser.setBounds(120, 100, 200, 20);
        taskDueDayDateChooser.getDateEditor().getUiComponent().addFocusListener(new FocusAdapter() {
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
        JCalendar calx = taskDueDayDateChooser.getJCalendar();
        calx.setCalendar(getPrevDate());
        centerPanel.add(taskDueDayDateChooser);
        if (taskDueDayvalue != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                if (taskDueDayvalue != null) {
                    Date taskDueDayDate = dateFormat.parse(taskDueDayvalue);
                    taskDueDayDateChooser.setDate(taskDueDayDate);
                }

            } catch (ParseException e) {
                // Handle the parse exception here
                e.printStackTrace();
            }
        } else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date defaultDate = dateFormat.parse("01/01/2023"); // Set your desired default date here
                taskDueDayDateChooser.setDate(defaultDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        taskInIdExamLabel = new JLabel("taskInIdExam : ");
        taskInIdExamLabel.setBounds(10, 130, 100, 20);
        centerPanel.add(taskInIdExamLabel);

        taskInIdExamField = new JTextField(taskInIdExamvalue);
        taskInIdExamField.setBounds(120, 130, 200, 20);
        taskInIdExamField.setText(String.valueOf(taskInIdExamvalue));

        centerPanel.add(taskInIdExamField);

        searchButton = new JButton("Search");
        searchButton.setBounds(330, 130, 80, 20);
        centerPanel.add(searchButton);

        taskTimeLabel = new JLabel("taskTime : ");
        taskTimeLabel.setBounds(10, 160, 100, 20);
        centerPanel.add(taskTimeLabel);

        taskTimeField = new JTextField(taskTimevalue);
        taskTimeField.setBounds(120, 160, 200, 20);

        centerPanel.add(taskTimeField);

        taskModeLabel = new JLabel("taskMode : ");
        taskModeLabel.setBounds(10, 190, 100, 20);
        centerPanel.add(taskModeLabel);

        checkBox1 = new JCheckBox("Chat GPT");
        checkBox1.setBounds(120, 190, 100, 30);
        centerPanel.add(checkBox1);

        checkBox2 = new JCheckBox("Calculator");
        checkBox2.setBounds(220, 190, 100, 30);
        centerPanel.add(checkBox2);

        if (mode.equals("0")) {
            checkBox1.setSelected(false);
            checkBox2.setSelected(false);
        } else if (mode.equals("1")) {
            checkBox1.setSelected(true);
            checkBox2.setSelected(false);
        } else if (mode.equals("2")) {
            checkBox1.setSelected(false);
            checkBox2.setSelected(true);
        } else {
            checkBox1.setSelected(true);
            checkBox2.setSelected(true);
        }

        taskForLabel = new JLabel("taskFor : ");
        taskForLabel.setBounds(10, 220, 100, 20);
        centerPanel.add(taskForLabel);

        taskForArea = new JTextArea(taskForShowString);
        taskForArea.setBounds(120, 220, 200, 20);
        centerPanel.add(taskForArea);
        taskForArea.setEditable(false);

        searchButton2 = new JButton("Search");
        searchButton2.setBounds(330, 220, 80, 20);
        centerPanel.add(searchButton2);

        taskDetailLabel = new JLabel("taskDetail : ");
        taskDetailLabel.setBounds(10, 250, 100, 20);
        centerPanel.add(taskDetailLabel);

        taskDetailArea = new JTextArea(taskDetailvalue);
        taskDetailArea.setBounds(120, 250, 300, 150);

        JScrollPane scrollPane = new JScrollPane(taskDetailArea);
        scrollPane.setBounds(120, 250, 300, 150);

        centerPanel.add(scrollPane);

        setButton = new JButton("set task");
        setButton.setBounds(120, 440, 80, 20);
        centerPanel.add(setButton);
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTaskma();
                setVisible(false);
            }

        });

        cancelButton = new JButton("cancel");
        cancelButton.setBounds(240, 440, 80, 20);
        centerPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }

        });

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        TaskDefault();
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskTitlevalue = taskTitleField.getText();

                Date selectedDate = taskDueDayDateChooser.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                taskDueDayvalue = dateFormat.format(selectedDate);

                taskInIdExamvalue = Integer.parseInt(taskInIdExamField.getText());
                taskTimevalue = taskTimeField.getText();
                taskDetailvalue = taskDetailArea.getText();
                if (!checkBox1.isSelected() && !checkBox2.isSelected()) {
                    taskModevalue = "0"; // Checkbox1 is off, Checkbox2 is off
                } else if (checkBox1.isSelected() && !checkBox2.isSelected()) {
                    taskModevalue = "1"; // Checkbox1 is on, Checkbox2 is off
                } else if (!checkBox1.isSelected() && checkBox2.isSelected()) {
                    taskModevalue = "2"; // Checkbox1 is off, Checkbox2 is on
                } else {
                    taskModevalue = "3"; // Checkbox1 is on, Checkbox2 is on
                }
                System.out.println(taskModevalue);
                examSet_search examSet_search = new examSet_search(userKey, taskForShow, taskForvalue, taskTitlevalue,
                        taskDueDayvalue, taskTimevalue,
                        taskModevalue, taskDetailvalue, taskInIdExamvalue);

                examSet_search.setVisible(true);
                setVisible(false);
            }
        });
        searchButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                taskTitlevalue = taskTitleField.getText();

                Date selectedDate = taskDueDayDateChooser.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                taskDueDayvalue = dateFormat.format(selectedDate);

                taskInIdExamvalue = Integer.valueOf(taskInIdExamField.getText());
                taskTimevalue = taskTimeField.getText();
                taskDetailvalue = taskDetailArea.getText();
                if (!checkBox1.isSelected() && !checkBox2.isSelected()) {
                    taskModevalue = "0"; // Checkbox1 is off, Checkbox2 is off
                } else if (checkBox1.isSelected() && !checkBox2.isSelected()) {
                    taskModevalue = "1"; // Checkbox1 is on, Checkbox2 is off
                } else if (!checkBox1.isSelected() && checkBox2.isSelected()) {
                    taskModevalue = "2"; // Checkbox1 is off, Checkbox2 is on
                } else {
                    taskModevalue = "3"; // Checkbox1 is on, Checkbox2 is on
                }
                System.out.println(taskModevalue);
                stu_search stu_search = new stu_search(userKey, taskForShow, taskForvalue, taskTitlevalue,
                        taskDueDayvalue, taskTimevalue,
                        taskModevalue, taskDetailvalue, taskInIdExamvalue);

                stu_search.setVisible(true);
                setVisible(false);
            }
        });
        if (taskForShow != null) {

            int length = taskForShow.size() + Integer.parseInt(idTaskSQLArea.getText().toString());

            idTaskSQLArea.setText(idTaskSQLArea.getText() + " ~ " + length);
        }

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

    private void TaskDefault() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado", "1130Coolhan@");

            // Retrieve the highest number in idTaskSQL column
            String selectQuery = "SELECT MAX(idTaskSQL) FROM task";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            int highestValue = 0;
            if (rs.next()) {
                highestValue = rs.getInt(1);
            }

            rs.close();
            stmt.close();
            con.close();

            // Set idTaskSQLTextField as highest number + 1
            idTaskSQLArea.setText(String.valueOf(highestValue + 1));

            sql sql = new sql();
            sql.getUserInfo(userKey);
            taskSetByArea.setText(sql.getUsername());

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private static void setTaskma() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado", "1130Coolhan@");

            // Retrieve the highest value in the idTaskSQL column
            String selectQuery = "SELECT MAX(idTaskSQL) FROM task";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(selectQuery);

            int highestValue = 0;
            if (rs.next()) {
                highestValue = rs.getInt(1);
            }
            rs.close();

            // Prepare the INSERT query to add the new task
            String insertQuery = "INSERT INTO task (idTaskSQL, Task_title, Task_detail, Task_setBy, Task_dueday, "
                    + "Task_in_idExam, Task_for, Task_percent, Task_finish, Task_grade, Task_gradeCode, Task_time, Task_mode, Task_finishTime) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertQuery);

            // Set the values for the prepared statement
            int idTaskSQL = highestValue + 1;
            String idTaskSQLSting = String.valueOf(idTaskSQL);
            String taskTitle = taskTitleField.getText();
            String taskDetail = taskDetailArea.getText();
            String taskSetBy = String.valueOf(userKey);
            Date taskDueDayDate = taskDueDayDateChooser.getDate();
            String taskDueDay = ""; // Initialize the taskDueDay variable
            if (taskDueDayDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                taskDueDay = dateFormat.format(taskDueDayDate);
            }
            int taskInIdExam = Integer.valueOf(taskInIdExamField.getText());
            String taskTime = taskTimeField.getText();
            String taskMode = taskModevalue;
            int flag = 0;
            taskForvalue.add(String.valueOf(userKey));
            // Iterate over the taskForShow list
            for (String taskFor : taskForvalue) {
                int idTaskSQLs = highestValue + flag + 1; // Increment idTaskSQL based on the flag

                pstmt.setInt(1, idTaskSQLs); // idTaskSQL
                pstmt.setString(2, taskTitle); // Task_title
                pstmt.setString(3, taskDetail); // Task_detail
                pstmt.setString(4, taskSetBy); // Task_setBy
                pstmt.setString(5, taskDueDay); // Task_dueday
                pstmt.setInt(6, taskInIdExam); // Task_in_idExam
                pstmt.setString(7, taskFor); // Task_for
                pstmt.setString(8, "0"); // Task_percent
                pstmt.setString(9, "0"); // Task_finish
                pstmt.setString(10, "0"); // Task_grade
                pstmt.setString(11, "0"); // Task_gradeCode
                pstmt.setString(12, taskTime); // Task_time
                pstmt.setString(13, taskMode); // Task_mode
                pstmt.setString(14, "0");

                // Execute the INSERT query
                pstmt.executeUpdate();

                // Increment idTaskSQL for the next iteration
                flag++;
            }

            // Close the prepared statement and database connection
            pstmt.close();
            con.close();

            // Display a success message
            JOptionPane.showMessageDialog(null, "Task(s) set successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear the input fields

        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to set task(s): " + ex.getMessage(), "Error",
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

}
