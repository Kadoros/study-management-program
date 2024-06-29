package mainPage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import TaskPage.Tasks;
import question.QuestionnaireApp;
import question.revisionPage;
import sql.sql;

public class StudentRevisionFrame extends JFrame {
    private JPanel infoPanel;
    private JLabel userInfoLabel;
    private int userKey;
    private List<Tasks> taskslist;
    private List<ExamSet> examSetList = new ArrayList<>();
    private JPanel contextPanel;
    private int currentPage;
    private JLabel JLabel;

    public StudentRevisionFrame(int userKeys, List<Tasks> tasksList) {

        this.userKey = userKeys;
        this.taskslist = tasksList;
        // Set up the main frame
        setTitle("Student Revision Frame");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        setSize(1000, 800);

        // Info panel at the south
        infoPanel = new JPanel();
        userInfoLabel = new JLabel();
        infoPanel.add(userInfoLabel);
        add(infoPanel, BorderLayout.SOUTH);

        // Center panel with scrollable content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        contextPanel = new JPanel();
        contextPanel.setLayout(new GridLayout(0, 2)); // Changed the rows to 0 to allow vertical expansion
        JScrollPane scrollPane = new JScrollPane(contextPanel); // Wrap contextPanel inside JScrollPane
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Upper panel with search box and sort button
        JPanel upperPanel = new JPanel(new BorderLayout());
        JTextField searchBox = new JTextField(20);
        JButton sortButton = new JButton("Sort");
        JPanel searchPanel = new JPanel();
        searchPanel.add(searchBox);
        searchPanel.add(sortButton);
        upperPanel.add(searchPanel, BorderLayout.EAST);
        JLabel JLabel = new JLabel("Finished tasks");
        upperPanel.add(JLabel, BorderLayout.WEST); // Added label "Finished tasks"
        centerPanel.add(upperPanel, BorderLayout.NORTH);

        // Scrollable panel

        add(centerPanel, BorderLayout.CENTER);

        // Sidebar panel on the west
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        JButton finishedTaskButton = new JButton("Finished Task");
        JButton externalExamButton = new JButton("External Exam Set");
        TitledBorder sideBorder = BorderFactory.createTitledBorder("sidebar");

        add(sidebarPanel, BorderLayout.WEST);

   
        finishedTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contextPanel.removeAll();
                JLabel.setText("finishedTask");
                for (Tasks task : tasksList) {
                    if ("0".equals(task.getTaskFinish())) {
                        System.out.println("not finished task");
                    }

                    else {

                        JPanel taskPanel = createTaskPanel(task);
                        contextPanel.add(taskPanel);

                    }
                }
                contextPanel.revalidate();
                contextPanel.repaint();
            }
        });

        externalExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    examSetList = getExamSetInfo();
                    contextPanel.removeAll();
                    JLabel.setText("externalExam");
                    for (ExamSet examset : examSetList) {
                        JPanel examsetPanel = createExamSetPanel(examset);
                        contextPanel.add(examsetPanel);
                        contextPanel.revalidate();
                        contextPanel.repaint();
                    }

                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });

        // Set the TitledBorder for the scrollable panel

        // Call the method to get user info
        getInfo();

        // Make the frame visible
        setVisible(true);

        // design

        sidebarPanel.setBackground(Color.decode("#3C587A"));
        sidebarPanel.add(Box.createHorizontalGlue());
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(finishedTaskButton);
        sidebarPanel.add(Box.createVerticalStrut(60));
        sidebarPanel.add(externalExamButton);
        sidebarPanel.add(Box.createHorizontalGlue());
        sidebarPanel.add(Box.createVerticalGlue());

        finishedTaskButton.setBackground(Color.decode("#3C587A"));
        externalExamButton.setBackground(Color.decode("#3C587A"));
        finishedTaskButton.setForeground(Color.white);
        externalExamButton.setForeground(Color.white);
        finishedTaskButton.setBorderPainted(false);
        externalExamButton.setBorderPainted(false);

        upperPanel.setBackground(Color.decode("#4C8CD6"));
        sortButton.setBackground(Color.decode("#3C587A"));
        sortButton.setForeground(Color.white);
        externalExamButton.setForeground(Color.white);
        finishedTaskButton.setBorderPainted(false);
        searchPanel.setBackground(Color.decode("#4C8CD6"));
        JLabel.setForeground(Color.white);

        infoPanel.setBackground(Color.decode("#4C8CD6"));
        userInfoLabel.setForeground(Color.white);

        centerPanel.setBackground(Color.white);

    }

    public void getInfo() {
        
        sql sql = new sql();
        sql.getUserInfo(userKey);

        // Call the getUserInfo method and retrieve the values
        String username = sql.getUsername();
        String Class = sql.getClassValue();
        String userRoleL = sql.getUserRoleL(); // Replace with the actual user role

        userInfoLabel.setText("Username: " + username + " | Class: " + Class + " | Role: " + userRoleL);
    }

    private JPanel createTaskPanel(Tasks task) {
        JPanel taskPanel = new JPanel(new GridBagLayout());
        TitledBorder taskPanelBorder = BorderFactory.createTitledBorder(task.getTaskTitle());
        taskPanel.setBorder(taskPanelBorder);

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
        gbc.gridy = 10;
        taskPanel.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

        // Set the maximum height of the task panel
        int maxTaskHeight = 600; 
        taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxTaskHeight));
        JButton startRevisionButton = new JButton("Start Revision");

        // Add an action listener to the button
        startRevisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revisionPage revisionPage = new revisionPage(userKey, task);
                revisionPage.setVisible(true);
            }
        });

        // Add the "Start Revision" button to the task panel
        gbc.gridy = 7;
        taskPanel.add(startRevisionButton, gbc);
        taskPanel.setBackground(Color.WHITE);

        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        taskPanel.setBorder(blackBorder);
        return taskPanel;
    }

    private JPanel createExamSetPanel(ExamSet exam_set) {
        JPanel examSetPanel = new JPanel(new GridBagLayout());
        TitledBorder examSetPanelBorder = BorderFactory.createTitledBorder("Exam Set " + exam_set.getIdexam_set());
        examSetPanel.setBorder(examSetPanelBorder);

        // Create labels for different exam set details
        JLabel examNameLabel = new JLabel("Exam Name: " + exam_set.getExam_name());
        JLabel examQNumLabel = new JLabel("Exam QNum: " + exam_set.getExam_QNum());
        JLabel examDiffLabel = new JLabel("Exam Difficulty: " + exam_set.getExam_diff());

        // Use JTextArea for exam context
        JTextArea examContextTextArea = new JTextArea(exam_set.getExam_context());
        examContextTextArea.setLineWrap(true); // Enable line wrapping
        examContextTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        examContextTextArea.setEditable(false); // Make it non-editable
        JScrollPane examContextScrollPane = new JScrollPane(examContextTextArea); // Add a scroll pane for longer text

        // Use GridBagConstraints to set constraints for the components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        examSetPanel.add(examNameLabel, gbc);

        gbc.gridy = 1;
        examSetPanel.add(examQNumLabel, gbc);

        gbc.gridy = 2;
        examSetPanel.add(examDiffLabel, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2; // Set grid width to 2 for the JTextArea
        gbc.weighty = 3.0; // Allow the JTextArea to expand vertically, three times higher
        examSetPanel.add(examContextScrollPane, gbc);

        // Add a rigid area below the exam context to give some vertical space
        gbc.gridy = 4;
        examSetPanel.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

        // Set the maximum height of the exam set panel
        int maxExamSetHeight = 600; // Adjust the height as per your requirement
        examSetPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxExamSetHeight));

        // Add "Start Revision" button to the exam set panel
        JButton startRevisionButton = new JButton("Start Revision");

        // Add an action listener to the button
        startRevisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Tasks Task = new Tasks(-30, "examset", "examset", "examset", "examset", exam_set.getIdexam_set(), "60",
                        "1", "examset",
                        "examset", "examset", "", "", "");
                QuestionnaireApp QuestionnaireApp = new QuestionnaireApp(userKey, Task);
                QuestionnaireApp.setVisible(true);
            }
        });

        gbc.gridy = 5;
        examSetPanel.add(startRevisionButton, gbc);
        examSetPanel.setBackground(Color.WHITE);

        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        examSetPanel.setBorder(blackBorder);

        return examSetPanel;
    }

    private List<ExamSet> getExamSetInfo() throws SQLException {
        examSetList.clear();
        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

        String query = "SELECT * FROM exam_set";
        PreparedStatement statement = con.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int idexam_set = resultSet.getInt("idexam_set");
            String exam_name = resultSet.getString("exam_name");
            String exam_QNum = resultSet.getString("exam_QNum");
            String exam_diff = resultSet.getString("exam_diff");
            String exam_context = resultSet.getString("exam_context");

            ExamSet examSet = new ExamSet(idexam_set, exam_name, exam_QNum, exam_diff, exam_context);
            examSetList.add(examSet);
        }

        return examSetList;
    }
}
