package TaskManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import Classes.ClassEnterGUI;
import Classes.ClassInfo;
import Classes.ClassManege;
import Login.Login.CreateLoginForm;
import TaskPage.Tasks;
import sql.sql;

public class TaskManager extends JFrame {
    public static int userKey;
    private static JLabel userInfoLabel;
    public JPanel subTaskPanel;
    private JComboBox<String> comboBox;
    static String userRoleL = "";
    List<Tasks> tasksList = new ArrayList<>();

    private JTable studentTable;
    private DefaultTableModel tableModel;

    public TaskManager(int userKey) throws SQLException {
        TaskManager.userKey = userKey;
        tasksList = getTaskInfo(userKey);

        setTitle("TaskManeger");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);

        // Create custom panel for tasks
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Task");
        taskPanel.setBorder(titledBorder);

        subTaskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        taskPanel.add(subTaskPanel);

        // Create scroll pane for the task panel
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, taskPanel.getPreferredSize().height));

        // Create buttons

        // Create sidebar panel

        // Create upper panel
        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel();
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));

        String[] options = { "Account", "Class", "Logout" };
        comboBox = new JComboBox<>(options);
        comboBox.addActionListener(e -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            if (selectedOption.equals("Account")) {
                // Perform action for account option
            } else if (selectedOption.equals("Class")) {
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
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Set the weight for horizontal expansion
        gbc.weighty = 1.0; // Set the weight for the top panel
        gbc.fill = GridBagConstraints.BOTH;
        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        TitledBorder topBorder = BorderFactory.createTitledBorder("Tasks");
        topPanel.setBorder(topBorder);

        topPanel.add(scrollPane);
        mainPanel.add(topPanel, gbc);
        gbc.gridy = 1;
        gbc.weighty = 1.0;

        // Create bottom panel
        JPanel bottomsPanel = new JPanel(new BorderLayout());
        mainPanel.add(bottomsPanel, gbc);

        getInfo();

        TitledBorder bottomsBorder = BorderFactory.createTitledBorder("Students");
        bottomsPanel.setBorder(bottomsBorder);

        // Create container panel for the upper panel and main panel
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(upperPanel, BorderLayout.NORTH);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        // Create content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(containerPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(contentPane);

        // Add container panel
        getContentPane().add(containerPanel);

        // Set the window location in the middle of the monitor
        setLocationRelativeTo(null);

        JTable taskTable = createTaskTable(tasksList);

        // Create scroll pane for the task table
        JScrollPane tableScrollPane = new JScrollPane(taskTable);
        topPanel.add(tableScrollPane, BorderLayout.CENTER);

        String[] columnNames = { "Student ID", "Student Name", "Email" };
        tableModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollsPane = new JScrollPane(studentTable);
        bottomsPanel.add(scrollsPane, BorderLayout.CENTER);

        JPanel buttomBPanel = new JPanel();
        bottomsPanel.add(buttomBPanel, BorderLayout.SOUTH);
        JButton buttomaddButton = new JButton("check Student");
        buttomaddButton.addActionListener((e) -> {
            try {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Object value = studentTable.getValueAt(selectedRow, 0); // Assuming the second column index is 1

                    if (value != null) {
                        int secondColumnValue = Integer.valueOf(value.toString()); // Convert the value to a string if
                                                                                   // needed

                        StuTaskManager stuTaskManager;
                        try {
                            stuTaskManager = new StuTaskManager(userKey, secondColumnValue);
                            stuTaskManager.setVisible(true);

                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        // Handle the case where the value is null
                    }
                } else {
                    // Handle the case where no row is selected
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        buttomBPanel.add(buttomaddButton);

        JPanel topBPanel = new JPanel();
        topPanel.add(topBPanel, BorderLayout.SOUTH);
        JButton topaddButton = new JButton("check Task");
        topaddButton.addActionListener((e) -> {
            try {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Tasks selectedTask = tasksList.get(selectedRow);

                    if (selectedTask != null) {
                        TaskTaskManager TaskTaskManager;
                        try {
                            TaskTaskManager = new TaskTaskManager(userKey, selectedTask);
                            TaskTaskManager.setVisible(true);

                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        // Handle the case where the selectedTask is null
                    }
                } else {
                    // Handle the case where no row is selected
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        topBPanel.add(topaddButton);

        loadStudentTable();

        upperPanel.setBackground(Color.decode("#4C8CD6"));
        infoPanel.setBackground(Color.decode("#4C8CD6"));
        userInfoLabel.setForeground(Color.white);
        buttonsPanel.setBackground(Color.decode("#4C8CD6"));

        mainPanel.setBackground(Color.decode("#BBD8FA"));
        topPanel.setBackground(Color.decode("#BBD8FA"));
        topBPanel.setBackground(Color.decode("#4C8CD6"));

        bottomsPanel.setBackground(Color.decode("#BBD8FA"));
        buttomBPanel.setBackground(Color.decode("#4C8CD6"));

        tableScrollPane.setBackground(Color.decode("#BBD8FA"));

        comboBox.setBackground(Color.decode("#4C8CD6"));
        comboBox.setForeground(Color.white);

        topaddButton.setBackground(Color.decode("#4C8CD6"));
        topaddButton.setForeground(Color.white);
        topaddButton.setBorderPainted(false);

        buttomaddButton.setBackground(Color.decode("#4C8CD6"));
        buttomaddButton.setForeground(Color.white);
        buttomaddButton.setBorderPainted(false);

    }

    private void loadStudentTable() {
        tableModel.setRowCount(0); // Clear existing table rows

        try {
            // Fetch student details using className
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado", "1130Coolhan@");
            String query = "SELECT userKey, name, Email FROM user_info WHERE class = ?";
            PreparedStatement statement = con.prepareStatement(query);
            sql sql = new sql();
            sql.getUserInfo(userKey);
            String Class = sql.getClassValue();
            statement.setString(1, Class);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String userKey = resultSet.getString("userKey");
                String name = resultSet.getString("name");
                String email = resultSet.getString("Email");

                Object[] rowData = { userKey, name, email };

                tableModel.addRow(rowData);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }
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

    // Get task information from the database
    private List<Tasks> getTaskInfo(int userKey2) throws SQLException {
        tasksList.clear();
        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado", "1130Coolhan@");
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

    private JTable createTaskTable(List<Tasks> tasksList) {
        String[] columnNames = {
                "Task Title", "Task ID", "Set By", "Due Day", "In Exam ID", "Task Time", "Task Mode", "Task Grade",
                "Task_for"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Tasks task : tasksList) {
            Object[] rowData = {
                    task.getTaskTitle(), task.getIdTaskSQL(), task.getTaskSetBy(), task.getTaskDueDay(),
                    task.getTaskInIdExam(), task.getTaskTime(), task.getTaskMode(), task.getTaskGrade(),
                    task.getTaskFor()
            };
            tableModel.addRow(rowData);
        }

        JTable table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 400)); // Adjust the size as needed

        return table;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManager adminPanel;
            try {
                adminPanel = new TaskManager(3);
                adminPanel.setVisible(true);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        });
    }
}
