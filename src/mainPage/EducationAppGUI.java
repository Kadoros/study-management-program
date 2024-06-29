package mainPage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import Classes.ClassEnterGUI;
import Classes.ClassInfo;
import Classes.ClassManege;
import Login.Login.CreateLoginForm;
import TaskManager.StuTaskManager;
import TaskManager.TaskManager;
import TaskPage.TaskWindow;
import TaskPage.Tasks;
import TaskPage.setTask;
import adminPanel.AdminPanel;
import analysis.recosys;
import extractor.extractor;
import grade.Grade;
import sql.sql;

public class EducationAppGUI extends JFrame {
    public static int userKey;
    private static JLabel userInfoLabel;
    public JPanel subTaskPanel;
    private JComboBox<String> comboBox;
    static String userRoleL = "";
    List<Tasks> tasksList = new ArrayList<>();

    private JProgressBar gaugeBar1;
    private JProgressBar gaugeBar2;
    private JProgressBar gaugeBar3;
    private JProgressBar gaugeBar4;
    private JProgressBar gaugeBar5;
    private JProgressBar gaugeBar6;

    private int Topic1Grade;
    private int Topic2Grade;
    private int Topic3Grade;
    private int Topic4Grade;
    private int Topic5Grade;
    private int Topic6Grade;

    private JLabel engagementLabel;
    private JButton analyzeButton;
    private JPanel analyzePanel;
    private JButton testButton;

    // sideButton
    static JButton adminButton;
    static JButton logoutButton;
    static JButton refreshButton;
    static JButton TaskManagerButton;
    static JButton setButton;
    static JButton examSetButton;
    static JButton classButton;

    public EducationAppGUI(int userKey) throws SQLException {
        EducationAppGUI.userKey = userKey;

        setTitle("Hub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        // Create custom panel for tasks
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Task");
        taskPanel.setBorder(titledBorder);

        
        subTaskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        taskPanel.add(subTaskPanel);

        

        getTaskInfo(userKey);
        showTask(tasksList);

        // Create scroll pane for the task panel
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, taskPanel.getPreferredSize().height));

        // Create buttons
        setButton = new JButton("Set Task");

        examSetButton = new JButton("Exam Set");
        examSetButton.addActionListener(e -> {
            extractor extractor;
            try {
                extractor = new extractor(userKey);
                extractor.setLocationRelativeTo(null);
                extractor.setVisible(true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // Create sidebar panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        TitledBorder sideBorder = BorderFactory.createTitledBorder("");
        sidePanel.setBorder(sideBorder);
        // Create sidebar buttons

        classButton = new JButton("Class");
        classButton.addActionListener(e -> {
            openClassWiondow(userKey);
        });

        logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(e -> {
            try {
                CreateLoginForm form = new CreateLoginForm();
                form.setSize(900, 600);
                centreWindow(form);
                form.setVisible(true);
            } catch (Exception E) {
                JOptionPane.showMessageDialog(null, E.getMessage());
            }
            this.dispose();
        });

        refreshButton = new JButton("refresh");

        TaskManagerButton = new JButton("taskManager");
        TaskManagerButton.addActionListener(e -> {
            try {
                TaskManager TaskManager = new TaskManager(userKey);
                TaskManager.setVisible(true);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });

        // Add sidebar buttons to the side panel

        // Create upper panel
        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel();
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        String[] options = { "Class", "Logout" };
        comboBox = new JComboBox<>(options);
        comboBox.addActionListener(e -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            if (selectedOption.equals("Class")) {
                openClassWiondow(userKey);
            } else if (selectedOption.equals("Logout")) {
                try {
                    CreateLoginForm form = new CreateLoginForm();
                    form.setSize(900, 600);
                    centreWindow(form);
                    form.setVisible(true);
                } catch (Exception E) {
                    JOptionPane.showMessageDialog(null, E.getMessage());
                }
                this.dispose();
            }
        });
        comboBox.setSize(10, 10);

        // Add buttons to the buttons panel

        setButton.addActionListener(e -> {
            setTask setTask = new setTask(userKey, null, null, null, null, null, "0", null, 0);
            setTask.setLocationRelativeTo(null);
            setTask.setVisible(true);
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        userInfoLabel = new JLabel();
        bottomPanel.add(userInfoLabel, BorderLayout.EAST);
        // Remove the following line from infoPanel
        infoPanel.add(userInfoLabel, BorderLayout.EAST);
        Dimension preferredSize = new Dimension(infoPanel.getPreferredSize().width, 30);
        infoPanel.setPreferredSize(preferredSize);

        // Change the layout of bottomPanel to BorderLayout
        bottomPanel.setLayout(new BorderLayout());
        // Place the infoPanel at the bottom
        bottomPanel.add(infoPanel, BorderLayout.SOUTH);

        upperPanel.add(comboBox, BorderLayout.EAST);
        // Add components to the upper panel
        upperPanel.add(buttonsPanel, BorderLayout.WEST);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 2.0; // Set the weight for horizontal expansion
        gbc.weighty = 1.0; // Set the weight for the top panel
        gbc.fill = GridBagConstraints.BOTH;
        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        gbc.gridy = 1;
        gbc.weighty = 1.0;

        // Create bottom panel
        JPanel bottomsPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(bottomsPanel, BorderLayout.SOUTH);
        // Create bottom left panel
        JPanel bottomLeftPanel = new JPanel();

        bottomLeftPanel.setPreferredSize(new Dimension(400, 400));
        // bottomLeftPanel.setLayout(BoxLayout);
        JPanel north = new JPanel();
        JPanel south = new JPanel();
        // bottomLeftPanel.add(north, BorderLayout.NORTH);
        // bottomLeftPanel.add(south, BorderLayout.SOUTH);
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Create label with bold text
        JLabel unlockLabel = new JLabel("Unlock your potential now!");
        Font boldFont = unlockLabel.getFont().deriveFont(Font.BOLD, 33f); // Adjust font size as needed
        unlockLabel.setFont(boldFont);

        // Create button
        JButton startReviseButton = new JButton("Start Revise");
        startReviseButton.addActionListener(e -> {
            StudentRevisionFrame studentRevisionFrame = new StudentRevisionFrame(userKey, tasksList);
            studentRevisionFrame.setVisible(true);
        });
        startReviseButton.setPreferredSize(new Dimension(150, 50));

        // Add label and button to the bottom left panel
        // north.add(unlockLabel);
        north.add(Box.createVerticalStrut(15)); // Add vertical spacing between components
        // north.add(startReviseButton);

        recosys recosys = new recosys(getGradeByUserKey(userKey), getAllExamSets());
        List<Integer> ExamIDList = recosys.recommendExamSet(1);
        int ExamID = ExamIDList.get(0);
        String userKeyDString = String.valueOf(userKey);
        // bottomLeftPanel.add(createTaskPanel(new Tasks(0, "recommendation",
        // "recommendation", "recommendation",
        // "1/1/1900", userKey, "60", "1", ExamIDString, "0", "0", "0", "", "0")));
        JPanel innerPanel = createTaskPanel(new Tasks(0, "recommendation",
                "recommendation", "recommendation",
                "1/1/1900", ExamID, "60", "1", userKeyDString, "0", "0", "0", "", "0"));

        JPanel innnnnerJPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        innnnnerJPanel.add(innerPanel);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        bottomLeftPanel.add(unlockLabel);
        bottomLeftPanel.add(startReviseButton);

        gbc.gridy = 1;
        bottomLeftPanel.add(innerPanel);

        JPanel bottomRightPanel = new JPanel();

        TitledBorder bottomLeftBorder = BorderFactory.createTitledBorder("Revision");
        bottomLeftPanel.setBorder(bottomLeftBorder);

        TitledBorder bottomRightBorder = BorderFactory.createTitledBorder("Engagement");
        bottomRightPanel.setBorder(bottomRightBorder);
        engagementLabel = new JLabel("Check your Engagement");
        engagementLabel.setFont(engagementLabel.getFont().deriveFont(Font.BOLD, 20f));

        // Create a button
        analyzeButton = new JButton("Task Manager");
        analyzeButton.addActionListener(e -> {
            try {
                StuTaskManager StuTaskManager = new StuTaskManager(userKey, userKey);
                StuTaskManager.setVisible(true);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        testButton = new JButton("Test");
        testButton.addActionListener(e -> {
            try {
                openTaskWindow(EntranceTestTask());
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });

        // Create a panel for the label and button
        analyzePanel = new JPanel(new BorderLayout());
        analyzePanel.add(engagementLabel, BorderLayout.NORTH);

        // Add the panel to the bottomRightPanel
        bottomRightPanel.add(analyzePanel, BorderLayout.CENTER);

        JPanel gaugesPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // Create percent gauge panels
        JPanel gaugePanel1 = new JPanel(new BorderLayout());
        JPanel gaugePanel2 = new JPanel(new BorderLayout());
        JPanel gaugePanel3 = new JPanel(new BorderLayout());
        JPanel gaugePanel4 = new JPanel(new BorderLayout());
        JPanel gaugePanel5 = new JPanel(new BorderLayout());
        JPanel gaugePanel6 = new JPanel(new BorderLayout());

        // Create percent gauge labels
        JLabel gaugeLabel1 = new JLabel("Topic 1");
        JLabel gaugeLabel2 = new JLabel("Topic 2");
        JLabel gaugeLabel3 = new JLabel("Topic 3");
        JLabel gaugeLabel4 = new JLabel("Topic 4");
        JLabel gaugeLabel5 = new JLabel("Topic 5");
        JLabel gaugeLabel6 = new JLabel("Topic 6");

        // Create percent gauge progress bars
        gaugeBar1 = new JProgressBar(JProgressBar.VERTICAL);
        gaugeBar2 = new JProgressBar(JProgressBar.VERTICAL);
        gaugeBar3 = new JProgressBar(JProgressBar.VERTICAL);
        gaugeBar4 = new JProgressBar(JProgressBar.VERTICAL);
        gaugeBar5 = new JProgressBar(JProgressBar.VERTICAL);
        gaugeBar6 = new JProgressBar(JProgressBar.VERTICAL);

        // Set initial values for the progress bars (0-100)
        gaugeBar1.setMinimum(0);
        gaugeBar1.setMaximum(70);

        gaugeBar2.setMinimum(0);
        gaugeBar2.setMaximum(70);

        gaugeBar3.setMinimum(0);
        gaugeBar3.setMaximum(70);

        gaugeBar4.setMinimum(0);
        gaugeBar4.setMaximum(70);

        gaugeBar5.setMinimum(0);
        gaugeBar5.setMaximum(70);

        gaugeBar6.setMinimum(0);
        gaugeBar6.setMaximum(70);

        gaugeBar1.setValue(0);
        gaugeBar2.setValue(0);
        gaugeBar3.setValue(0);
        gaugeBar4.setValue(0);
        gaugeBar5.setValue(0);
        gaugeBar6.setValue(0);

        gaugeBar1.setFont(gaugeBar1.getFont().deriveFont(Font.BOLD));
        gaugeBar2.setFont(gaugeBar2.getFont().deriveFont(Font.BOLD));
        gaugeBar3.setFont(gaugeBar3.getFont().deriveFont(Font.BOLD));
        gaugeBar4.setFont(gaugeBar4.getFont().deriveFont(Font.BOLD));
        gaugeBar5.setFont(gaugeBar5.getFont().deriveFont(Font.BOLD));
        gaugeBar6.setFont(gaugeBar6.getFont().deriveFont(Font.BOLD));
        // customizeProgressBarUI(gaugeBar1);
        // customizeProgressBarUI(gaugeBar2);
        // customizeProgressBarUI(gaugeBar3);
        // customizeProgressBarUI(gaugeBar4);
        // customizeProgressBarUI(gaugeBar5);
        // customizeProgressBarUI(gaugeBar6);

        gaugeBar1.setStringPainted(true);
        gaugeBar1.setString(gaugeBar1.getValue() / 10 + "");

        gaugeBar2.setStringPainted(true);
        gaugeBar2.setString(gaugeBar2.getValue() / 10 + "");

        gaugeBar3.setStringPainted(true);
        gaugeBar3.setString(gaugeBar3.getValue() / 10 + "");

        gaugeBar4.setStringPainted(true);
        gaugeBar4.setString(gaugeBar4.getValue() / 10 + "");

        gaugeBar5.setStringPainted(true);
        gaugeBar5.setString(gaugeBar5.getValue() / 10 + "");

        gaugeBar6.setStringPainted(true);
        gaugeBar6.setString(gaugeBar6.getValue() / 10 + "");

        // Add components to the gauge panels
        gaugePanel1.add(gaugeLabel1, BorderLayout.NORTH);
        gaugePanel1.add(gaugeBar1, BorderLayout.CENTER);
        gaugePanel2.add(gaugeLabel2, BorderLayout.NORTH);
        gaugePanel2.add(gaugeBar2, BorderLayout.CENTER);
        gaugePanel3.add(gaugeLabel3, BorderLayout.NORTH);
        gaugePanel3.add(gaugeBar3, BorderLayout.CENTER);
        gaugePanel4.add(gaugeLabel4, BorderLayout.NORTH);
        gaugePanel4.add(gaugeBar4, BorderLayout.CENTER);
        gaugePanel5.add(gaugeLabel5, BorderLayout.NORTH);
        gaugePanel5.add(gaugeBar5, BorderLayout.CENTER);
        gaugePanel6.add(gaugeLabel6, BorderLayout.NORTH);
        gaugePanel6.add(gaugeBar6, BorderLayout.CENTER);

        gaugesPanel.add(gaugePanel1);
        gaugesPanel.add(gaugePanel2);
        gaugesPanel.add(gaugePanel3);
        gaugesPanel.add(gaugePanel4);
        gaugesPanel.add(gaugePanel5);
        gaugesPanel.add(gaugePanel6);

        bottomRightPanel.add(gaugesPanel, BorderLayout.SOUTH);

        
        bottomsPanel.add(bottomLeftPanel, BorderLayout.WEST);
        bottomsPanel.add(bottomRightPanel, BorderLayout.EAST);
        // Add the top, bottom left, and bottom right panels to the main panel

        adminButton = new JButton("Admin Panel");

        adminButton.setVisible(true);
        if (userRoleL.equals("Student")) {
            adminButton.setVisible(false);
        }
        adminButton.addActionListener(e -> {
            AdminPanel adminPanel = new AdminPanel(userKey);
            adminPanel.setLocationRelativeTo(null);
            adminPanel.setVisible(true);
        });

        // Create container panel for the upper panel and main panel
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);
        containerPanel.add(upperPanel, BorderLayout.NORTH);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        // Create content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(containerPanel, BorderLayout.CENTER);
        contentPane.add(sidePanel, BorderLayout.WEST);
        setContentPane(contentPane);

        // Add container panel
        getContentPane().add(containerPanel);

        // Set the window location in the middle of the monitor
        setLocationRelativeTo(null);
        getGradeInfo(userKey);
        setGauge(userKey);

        Timer timer = new Timer(3000, e -> {
            try {
                getTaskInfo(userKey);
                showTask(tasksList);
                getGradeInfo(userKey);
                setGauge(userKey);
                getInfo();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        timer.start();

        refreshButton.addActionListener(e -> {
            try {
                getTaskInfo(userKey);
                showTask(tasksList);
                getGradeInfo(userKey);
                setGauge(userKey);
                getInfo();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        getInfo();

        // design

        // bottomRightPanel
        sidePanel.setBackground(Color.decode("#3C587A"));
        sidePanel.add(Box.createHorizontalGlue());
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(adminButton);
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(classButton);
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(logoutButton);
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(refreshButton);
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(TaskManagerButton);
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(examSetButton);
        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(setButton);
        sidePanel.add(Box.createHorizontalGlue());
        sidePanel.add(Box.createVerticalGlue());
        // sidebar

        // upper
        upperPanel.setBackground(Color.decode("#4C8CD6"));
        comboBox.setBackground(Color.decode("#4C8CD6"));
        comboBox.setForeground(Color.white);
        // upper

        infoPanel.setBackground(Color.decode("#4C8CD6"));
        userInfoLabel.setForeground(Color.white);

        // task
        taskPanel.setBackground(Color.decode("#BBD8FA"));
        subTaskPanel.setBackground(Color.decode("#BBD8FA"));
        // task

        // bottomLeftPanel
        bottomLeftPanel.setBackground(Color.decode("#CEE9FE"));
        startReviseButton.setBackground(Color.white);

        // bottomLeftPanel

        // bottomRightPanel
        // bottomRightPanel.setBackground(Color.decode("#CEEDFE"));
        testButton.setBackground(Color.white);
        analyzeButton.setBackground(Color.white);
        // bottomRightPanel

        // main
        mainPanel.setBackground(Color.decode("#BBD8FA"));
        // bmain

        // button
        adminButton.setBackground(Color.decode("#3C587A"));
        refreshButton.setBackground(Color.decode("#3C587A"));
        examSetButton.setBackground(Color.decode("#3C587A"));
        TaskManagerButton.setBackground(Color.decode("#3C587A"));
        setButton.setBackground(Color.decode("#3C587A"));
        logoutButton.setBackground(Color.decode("#3C587A"));
        classButton.setBackground(Color.decode("#3C587A"));
        adminButton.setForeground(Color.white);
        refreshButton.setForeground(Color.white);
        examSetButton.setForeground(Color.white);
        TaskManagerButton.setForeground(Color.white);
        setButton.setForeground(Color.white);
        logoutButton.setForeground(Color.white);
        classButton.setForeground(Color.white);
        adminButton.setBorderPainted(false);
        refreshButton.setBorderPainted(false);
        examSetButton.setBorderPainted(false);
        TaskManagerButton.setBorderPainted(false);
        setButton.setBorderPainted(false);
        logoutButton.setBorderPainted(false);
        classButton.setBorderPainted(false);

        // adminButton, refreshButton, startRevisionButton testButton analyzeButton,
        // TaskManagerButton logoutButton classButton setButton examSetButton
    }

    public ExamSet[] getAllExamSets() {
        ExamSet[] examSets = null; // Initialize the array

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            String query = "SELECT * FROM exam_set ";
            PreparedStatement preparedStatement = con.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<ExamSet> examSetList = new ArrayList<>(); // Use a List to temporarily store objects

            while (resultSet.next()) {
                int idexam_set = resultSet.getInt("idexam_set");
                String exam_name = resultSet.getString("exam_name");
                String exam_QNum = resultSet.getString("exam_QNum");
                String exam_diff = resultSet.getString("exam_diff");
                String exam_context = resultSet.getString("exam_context");

                ExamSet examSet = new ExamSet(idexam_set, exam_name, exam_QNum, exam_diff, exam_context);
                examSetList.add(examSet);
            }

            resultSet.close();
            preparedStatement.close();
            con.close();

            // Convert the List to an array
            examSets = examSetList.toArray(new ExamSet[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return examSets;
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

        if (userRoleL.equals("Admin")) {
            adminButton.setVisible(true);
            logoutButton.setVisible(true);
            refreshButton.setVisible(true);
            TaskManagerButton.setVisible(true);
            setButton.setVisible(true);
            examSetButton.setVisible(true);
            classButton.setVisible(true);
        } else if (userRoleL.equals("Teacher")) {
            adminButton.setVisible(false);
            logoutButton.setVisible(true);
            refreshButton.setVisible(true);
            TaskManagerButton.setVisible(true);
            setButton.setVisible(true);
            examSetButton.setVisible(true);
            classButton.setVisible(true);

        } else if (userRoleL.equals("Student")) {
            adminButton.setVisible(false);
            logoutButton.setVisible(true);
            refreshButton.setVisible(true);
            TaskManagerButton.setVisible(false);
            setButton.setVisible(false);
            examSetButton.setVisible(false);
            classButton.setVisible(true);

        } else if (userRoleL.equals("Class Owner")) {
            adminButton.setVisible(false);
            logoutButton.setVisible(true);
            refreshButton.setVisible(true);
            TaskManagerButton.setVisible(true);
            setButton.setVisible(true);
            examSetButton.setVisible(true);
            classButton.setVisible(true);

        } else if (userRoleL.equals("Affiliation Owner")) {
            adminButton.setVisible(true);
            logoutButton.setVisible(true);
            refreshButton.setVisible(true);
            TaskManagerButton.setVisible(true);
            setButton.setVisible(true);
            examSetButton.setVisible(true);
            classButton.setVisible(true);

        } else {
            adminButton.setVisible(false);
            logoutButton.setVisible(true);
            refreshButton.setVisible(true);
            TaskManagerButton.setVisible(false);
            setButton.setVisible(false);
            examSetButton.setVisible(false);
            classButton.setVisible(true);
        }
    }

    public Grade getGradeByUserKey(int userKey) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

        String query = "SELECT * FROM grade WHERE userKey = ?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(1, userKey);
        ResultSet resultSet = preparedStatement.executeQuery();

        Grade grade = null;
        if (resultSet.next()) {
            int topic1Grade = resultSet.getInt("topic1");
            int topic2Grade = resultSet.getInt("topic2");
            int topic3Grade = resultSet.getInt("topic3");
            int topic4Grade = resultSet.getInt("topic4");
            int topic5Grade = resultSet.getInt("topic5");
            int topic6Grade = resultSet.getInt("topic6");

            grade = new Grade(userKey, topic1Grade, topic2Grade, topic3Grade,
                    topic4Grade, topic5Grade, topic6Grade);
        }

        resultSet.close();
        preparedStatement.close();
        con.close();

        return grade;
    }

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    // Get task information from the database
    private List<Tasks> getTaskInfo(int userKey2) throws SQLException {
        tasksList.clear();
        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
        String query = "SELECT * FROM task WHERE Task_for = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, userKey2);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int idTaskSQL = resultSet.getInt("idTaskSQL");
            String taskTitle = resultSet.getString("Task_title");
            String taskDetail = resultSet.getString("Task_detail");
            String taskSetBy = resultSet.getString("Task_setBy");
            String taskDueDay = resultSet.getString("Task_dueDay");
            int taskInIdExam = resultSet.getInt("Task_in_idExam");
            String taskTime = resultSet.getString("Task_time");
            String taskMode = resultSet.getString("Task_mode");
            String taskFor = resultSet.getString("Task_for");
            String taksPercentage = resultSet.getString("Task_percent");
            String taksFinish = resultSet.getString("Task_finish");
            String taskGrade = resultSet.getString("Task_grade");
            String taskGradecode = resultSet.getString("Task_gradecode");
            String taskFinishTime = resultSet.getString("Task_finishTime");

            Tasks task = new Tasks(idTaskSQL, taskTitle, taskDetail, taskSetBy, taskDueDay, taskInIdExam, taskTime,
                    taskMode, taskFor, taksPercentage,
                    taksFinish, taskGrade, taskGradecode, taskFinishTime);
            tasksList.add(task);
        }

        return tasksList;

    }

    // Set task information in the UI
    private void showTask(List<Tasks> tasksList) {
        // Clear the subTaskPanel before adding new components
        subTaskPanel.removeAll();

        // Add the tasks to the subTaskPanel
        for (Tasks task : tasksList) {
            if ("1".equals(task.getTaskFinish())) {
                System.out.println("finished task");
            }

            else {

                JPanel taskPanel = createTaskPanel(task);
                subTaskPanel.add(taskPanel);
            }
        }

        // Refresh the subTaskPanel to reflect the changes
        subTaskPanel.revalidate();
        subTaskPanel.repaint();
    }

    private JPanel createTaskPanel(Tasks task) {
        JPanel taskPanel = new JPanel(new GridBagLayout());

        // Create labels for different task details
        JLabel taskTitleLabel = new JLabel("Task Title: " + task.getTaskTitle());
        JLabel taskIdLabel = new JLabel("Task ID: " + task.getIdTaskSQL());

        JTextArea taskDetailTextArea = new JTextArea(task.getTaskDetail()); // Use JTextArea for task details
        taskDetailTextArea.setLineWrap(true); // Enable line wrapping
        taskDetailTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        taskDetailTextArea.setEditable(false); // Make it non-editable
        JScrollPane taskDetailScrollPane = new JScrollPane(taskDetailTextArea); // Add a scroll pane for longer text

        JLabel taskSetByLabel = new JLabel("Set By: " + task.getTaskSetBy());
        JLabel taskDueDayLabel = new JLabel("Due Day: " + task.getTaskDueDay());
        JLabel taskInIdExamLabel = new JLabel("In Exam ID: " + task.getTaskInIdExam());
        JLabel taskTimeLabel = new JLabel("Task Time: " + task.getTaskTime());
        JLabel taskModeLabel = new JLabel("Task Mode: " + task.getTaskMode());
        JLabel taskGradeLabel = new JLabel("Task Grade: " + task.getTaskGrade());

        // Use GridBagConstraints to set constraints for the components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        taskPanel.add(taskTitleLabel, gbc);

        gbc.gridy = 1;
        taskPanel.add(taskIdLabel, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 2; // Set grid width to 2 for the JTextArea
        gbc.weighty = 3.0; // Allow the JTextArea to expand vertically, three times higher
        taskPanel.add(taskDetailScrollPane, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1; // Reset grid width to 1 for other labels
        gbc.weighty = 0.0; // Reset vertical weight to 0 for other labels
        taskPanel.add(taskSetByLabel, gbc);

        gbc.gridy = 2;
        taskPanel.add(taskDueDayLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        taskPanel.add(taskInIdExamLabel, gbc);

        gbc.gridy = 1;
        taskPanel.add(taskTimeLabel, gbc);

        gbc.gridy = 2;
        taskPanel.add(taskModeLabel, gbc);

        gbc.gridy = 3;
        taskPanel.add(taskGradeLabel, gbc);

        // Create a progress bar for the task percentage
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(Integer.parseInt(task.getTaskPercentage()));
        progressBar.setStringPainted(true);
        progressBar.setString(task.getTaskPercentage() + "%");
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 6;
        gbc.weighty = 1.0; // Allow the progress bar to expand vertically

        taskPanel.add(progressBar, gbc);

        // Add a rigid area below the progress bar to give some vertical space
        gbc.gridy = 8;
        taskPanel.add(Box.createRigidArea(new Dimension(0, 7)), gbc);

        // Set the maximum height of the task panel
        int maxTaskHeight = 30; // Adjust the height as per your requirement
        taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxTaskHeight));
        JButton startRevisionButton = new JButton("Start Task");

        // Add an action listener to the button
        startRevisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTaskWindow(task);
            }
        });

        // Add the "Start Revision" button to the task panel
        gbc.gridy = 7;
        taskPanel.add(startRevisionButton, gbc);

        taskPanel.setPreferredSize(new Dimension(300, 250));
        taskPanel.setBackground(Color.WHITE);

        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        taskPanel.setBorder(blackBorder);
        return taskPanel;
    }

    private void openTaskWindow(Tasks task) {
        // Create a new task window
        TaskWindow taskWindow = new TaskWindow(task, userKey);
        taskWindow.setVisible(true);
    }

    // Refresh the subTaskPanel to reflect the changes
    private void setGauge(int userKey2) throws SQLException {
        List<Grade> gradeList = getGradeInfo(userKey2);

        // Update the gaugeBar values based on the grades
        for (Grade grade : gradeList) {

            if (grade.getUserKey() == userKey2) {
                Topic1Grade = grade.getTopic1Grade();
                Topic2Grade = grade.getTopic2Grade();
                Topic3Grade = grade.getTopic3Grade();
                Topic4Grade = grade.getTopic4Grade();
                Topic5Grade = grade.getTopic5Grade();
                Topic6Grade = grade.getTopic6Grade();
                gaugeBar1.setValue(Topic1Grade);
                gaugeBar2.setValue(Topic2Grade);
                gaugeBar3.setValue(Topic3Grade);
                gaugeBar4.setValue(Topic4Grade);
                gaugeBar5.setValue(Topic5Grade);
                gaugeBar6.setValue(Topic6Grade);
            }

        }

        // Update the gaugeBar strings to reflect the current values
        gaugeBar1.setString(gaugeBar1.getValue() + "");
        gaugeBar2.setString(gaugeBar2.getValue() + "");
        gaugeBar3.setString(gaugeBar3.getValue() + "");
        gaugeBar4.setString(gaugeBar4.getValue() + "");
        gaugeBar5.setString(gaugeBar5.getValue() + "");
        gaugeBar6.setString(gaugeBar6.getValue() + "");

        if (Topic1Grade == 0 && Topic2Grade == 0 && Topic3Grade == 0 && Topic4Grade == 0 && Topic5Grade == 0
                && Topic6Grade == 0) {
            engagementLabel.setText("Let's check your GRADE");
            analyzePanel.add(testButton);
        } else {
            analyzePanel.add(analyzeButton, BorderLayout.CENTER);
        }
    }

    private List<Grade> getGradeInfo(int userKey2) throws SQLException {
        List<Grade> gradeList = new ArrayList<>();

        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
        String query = "SELECT * FROM grade WHERE userKey = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, userKey2);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int userKeys = resultSet.getInt("userKey");
            int topic1Grade = resultSet.getInt("topic1");
            int topic2Grade = resultSet.getInt("topic2");
            int topic3Grade = resultSet.getInt("topic3");
            int topic4Grade = resultSet.getInt("topic4");
            int topic5Grade = resultSet.getInt("topic5");
            int topic6Grade = resultSet.getInt("topic6");

            Grade grade = new Grade(userKeys, topic1Grade, topic2Grade, topic3Grade, topic4Grade, topic5Grade,
                    topic6Grade);
            gradeList.add(grade);
        }

        return gradeList;
    }

    public static void openClassWiondow(int userKey2) {
        sql sql = new sql();

        if (sql.getClassValue().equals("non")) {
            ClassEnterGUI ClassEnterGUI = new ClassEnterGUI(userKey2);
            ClassEnterGUI.setVisible(true);
        } else {
            if (sql.getUserRoleL().equals("Student")) {
                ClassInfo ClassInfo = new ClassInfo(userKey2);
                ClassInfo.setVisible(true);

            } else {
                ClassManege ClassManege = new ClassManege(userKey2);
                ClassManege.setVisible(true);

            }
        }
    }

    private Tasks EntranceTestTask() throws SQLException {

        Tasks task = new Tasks(1, "entrance Test", "entrance Test", "1", "1/1/1999", 1, "12",
                "1 ", "1", "0",
                "0", "5", "0", "0");

        return task;

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EducationAppGUI adminPanel;
            try {
                adminPanel = new EducationAppGUI(1);
                adminPanel.setVisible(true);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        });
    }
}
