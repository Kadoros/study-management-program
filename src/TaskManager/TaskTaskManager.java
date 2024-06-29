package TaskManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import Classes.ClassEnterGUI;
import Classes.ClassInfo;
import Classes.ClassManege;
import TaskPage.Tasks;
import question.revisionPage;
import sql.sql;

public class TaskTaskManager extends JFrame {
    public static int userKey;
    private static JLabel userInfoLabel;
    public JPanel subTaskPanel;

    static String userRoleL = "";
    List<Tasks> tasksList = new ArrayList<>();
    private JLabel selectedTaskInfoLabel;

    public TaskTaskManager(int userKey, Tasks Task) throws SQLException {
        TaskTaskManager.userKey = userKey;
        tasksList = getTaskInfo(Task);

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

        // Add components to the upper panel
        upperPanel.add(buttonsPanel, BorderLayout.WEST);

        selectedTaskInfoLabel = new JLabel();
        buttonsPanel.add(selectedTaskInfoLabel);
        setStudentLable(Task);

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

        getInfo();

        // design
        upperPanel.setBackground(Color.decode("#4C8CD6"));
        infoPanel.setBackground(Color.decode("#4C8CD6"));
        userInfoLabel.setForeground(Color.white);
        buttonsPanel.setBackground(Color.decode("#4C8CD6"));
        selectedTaskInfoLabel.setForeground(Color.white);

        mainPanel.setBackground(Color.decode("#BBD8FA"));
        topPanel.setBackground(Color.decode("#BBD8FA"));
        tableScrollPane.setBackground(Color.decode("#BBD8FA"));
    }

    public void setStudentLable(Tasks Task) {
        String TaskTitle = Task.getTaskTitle();
        int ID = Task.getIdTaskSQL();
        String DueDay = Task.getTaskDueDay();
        selectedTaskInfoLabel.setText(

                "Task Title: " + TaskTitle + " | ID: " + ID + " | Due Day: " + DueDay);
        ;
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
    private List<Tasks> getTaskInfo(Tasks Task) throws SQLException {
        tasksList.clear();
        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado", "1130Coolhan@");
        ;
        String query = "SELECT * FROM task WHERE Task_title = ? AND Task_in_idExam = ? AND Task_setBy = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, Task.getTaskTitle());
        statement.setInt(2, Task.getTaskInIdExam());
        statement.setString(3, Task.getTaskSetBy());

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
                "Task ID", "For", "Finish time", "result %",
                "State", "review"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Tasks task : tasksList) {
            sql sql = new sql();
            String TaskFor = task.getTaskFor();
            sql.getUserInfo(Integer.valueOf(TaskFor));

            // Call the getUserInfo method and retrieve the values
            String StudentName = sql.getUsername();

            Object[] rowData = {
                    task.getIdTaskSQL(), StudentName, task.getTaskFinishTime(), task.getTaskPercentage()
            };
            tableModel.addRow(rowData);
        }

        JTable table = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 5) { // Button column
                    return new ButtonRenderer();

                } else if (column == 4) { // Task state column (13th column)
                    return new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value,
                                boolean isSelected, boolean hasFocus, int row, int column) {

                            Tasks task = tasksList.get(row);

                            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                                    column);

                            // Calculate task state based on TaskDueDay, TaskFinish, and TaskFinishTime
                            String taskState = calculateTaskState(task.getTaskDueDay(), task.getTaskFinish(),
                                    task.getTaskFinishTime());
                            setText(taskState); // Set the task state text
                            return this;
                        }
                    };
                }
                return super.getCellRenderer(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 5) { // Button column
                    return new ButtonEditor(new JCheckBox());
                }
                return super.getCellEditor(row, column);
            }
        };
        table.setPreferredScrollableViewportSize(new Dimension(800, 400)); // Adjust the size as needed

        TableColumn gaugeColumn = new TableColumn(3, 100); // Column index and width
        gaugeColumn.setHeaderValue("Result Bar");
        table.addColumn(gaugeColumn);
        gaugeColumn.setCellRenderer(new GaugeBarRenderer());

        return table;
    }

    class GaugeBarRenderer extends JProgressBar implements TableCellRenderer {
        public GaugeBarRenderer() {
            setBorderPainted(false);
            setStringPainted(true);
            setString(""); // Empty string to show only the gauge bar
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            int percentage = Integer.parseInt(value.toString());
            setValue(percentage);
            return this;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Review"); // Set button text
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            setClickCountToStart(1);
            button.addActionListener(e -> {
                // Open a new window based on the button click
                Tasks task = tasksList.get(currentRow);

                if (task.getTaskFinish().equals("1")) {
                    // Task is finished, open revision page
                    revisionPage revisionPage = new revisionPage(userKey, task);
                    revisionPage.setVisible(true);
                } else if (task.getTaskFinish().equals("0")) {
                    // Task is not finished, show a message
                    JOptionPane.showMessageDialog(null, "Task is not finished.");
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row; // Store the current row for reference
            button.setText("Review"); // Set button text
            return button;
        }
    }

    private String calculateTaskState(String taskDueDay, String taskFinish, String taskFinishTime) {
        int taskFinishValue = Integer.parseInt(taskFinish);

        if (taskFinishValue == 0) {
            boolean isOverdue = isTaskOverdue(taskDueDay, taskFinishTime);
            if (isOverdue) {
                return "<html><font color='#941616'>Overdue</font></html>";
            } else {
                return "<html><font color='#85abab'>Not Finished</font></html>";
            }
        } else if (taskFinishValue == 1) {
            boolean isOnTime = isTaskOnTime(taskDueDay, taskFinishTime);
            if (isOnTime) {
                return "<html><font color='#056608'>Finish as due</font></html>";
            } else {
                return "<html><font color='#b0ae74'>Finish overdue </font></html>";
            }
        }

        return "<html><font color='black'>Unknown</font></html>"; // Default state if taskFinishValue is not recognized
    }

    private boolean isTaskOverdue(String taskDueDay, String taskFinishTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dueDate = dateFormat.parse(taskDueDay);

            // Get the current date
            Date currentDate = new Date();

            // Compare dueDate and currentDate
            if (dueDate.before(currentDate)) {
                // Task is overdue
                return true;
            }

            return false;
        } catch (Exception e) {
            // Handle parsing and other exceptions
            return false; // Placeholder value
        }
    }

    private boolean isTaskOnTime(String taskDueDay, String taskFinishTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dueDate = dateFormat.parse(taskDueDay);
            Date finishTime = dateFormat.parse(taskFinishTime);

            // Compare dueDate and finishTime
            if (finishTime.before(dueDate)) {
                // Task was finished on time
                return true;
            }

            return false;
        } catch (Exception e) {
            // Handle parsing and other exceptions
            return false; // Placeholder value
        }
    }

}
