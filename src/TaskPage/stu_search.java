package TaskPage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import sql.sql;

public class stu_search extends JFrame {
    private JTable table;
    private JTable emptyTable; // New table without rows
    private DefaultTableModel tableModel;
    private DefaultTableModel emptyTableModel;
    private String currentTable = "user_info";
    private JTextField tableSearchField;
    private JTextField emptyTableSearchField;
    public static String userKey = "";
    private static JLabel userInfoLabel;
    private static String username;
    private static String ClassValue;
    private static String userRoleL;

    private String names;
    private static int userKeys;
    private ArrayList<String> taskForShow;
    private ArrayList<String> taskForvalue;
    private String title;
    private String day;
    private String time;
    private String mode;
    private String detail;
    private int taskInIdExamvalue;

    private ArrayList<String> namesList;
    private ArrayList<String> userKeysList;

    public stu_search(int userKey, ArrayList<String> taskForShow, ArrayList<String> taskForvalue, String title,
            String day,
            String time,
            String mode, String detail, int taskInIdExamvalu) {
        this.userKeys = userKey;
        this.taskForShow = taskForShow;
        this.taskForvalue = taskForvalue;
        this.title = title;
        this.day = day;
        this.time = time;
        this.mode = mode;
        this.detail = detail;
        this.taskInIdExamvalue = taskInIdExamvalu;

        setTitle("Student Search");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        // Create table model
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        // Create empty table model
        emptyTableModel = new DefaultTableModel();
        emptyTable = new JTable(emptyTableModel);

        // Create scroll pane and add table to it
        JScrollPane tableScrollPane = new JScrollPane(table);
        JScrollPane emptyTableScrollPane = new JScrollPane(emptyTable);

        // Create buttons
        JButton addButton = new JButton("Add One");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Object[] rowData = new Object[table.getColumnCount()];
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        rowData[i] = table.getValueAt(selectedRow, i);
                    }

                    if (!isDuplicateRow(rowData)) {
                        emptyTableModel.addRow(rowData);
                    }
                }
            }
        });
        JButton addallButton = new JButton("Add All");
        addallButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the row count of the table
                int rowCount = table.getRowCount();

                // Iterate over each row in the table
                for (int row = 0; row < rowCount; row++) {
                    // Get the data of the current row
                    Object[] rowData = new Object[table.getColumnCount()];
                    for (int col = 0; col < table.getColumnCount(); col++) {
                        rowData[col] = table.getValueAt(row, col);
                    }

                    // Add the row data to the emptyTable if it's not a duplicate
                    if (!isDuplicateRow(rowData)) {
                        emptyTableModel.addRow(rowData);
                    }
                }

                // Clear the table

            }
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = emptyTable.getSelectedRow();
                if (selectedRow != -1) {
                    emptyTableModel.removeRow(selectedRow);
                }
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = emptyTable.getRowCount();
                namesList = new ArrayList<>();
                userKeysList = new ArrayList<>();

                if (rowCount > 0) {

                    for (int row = 0; row < emptyTableModel.getRowCount(); row++) {

                        String name = emptyTableModel.getValueAt(row, emptyTableModel.findColumn("name")).toString();
                        String userKey = emptyTableModel.getValueAt(row, emptyTableModel.findColumn("userKey"))
                                .toString();

                        namesList.add(name);
                        userKeysList.add(userKey);
                        System.out.println(namesList);
                        System.out.println(userKeysList);
                    }

                }
                System.out.println(String.join(", ", namesList));
                setTask setTask;
                setTask = new setTask(userKey, namesList, userKeysList, title, day, time, mode, detail,
                        taskInIdExamvalu);

                setTask.setVisible(true);
                setVisible(false);

            }
        });
        // Create upper panel
        JPanel upperPanel = new JPanel(new BorderLayout());

        JPanel infoPanel = new JPanel();

        userInfoLabel = new JLabel();

        tableSearchField = new JTextField(10);
        emptyTableSearchField = new JTextField(10);
        getInfo();
        infoPanel.add(userInfoLabel, BorderLayout.WEST);
        upperPanel.add(tableSearchField, BorderLayout.EAST);

        tableSearchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = tableSearchField.getText();
                searchTable(searchText);
            }
        });

        emptyTableSearchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = emptyTableSearchField.getText();
                searchEmptyTable(searchText);
            }
        });

        // Add buttons to the buttons panel

        // Create panel1
        JPanel panel1 = new JPanel();

        panel1.add(addButton);
        panel1.add(addallButton);

        // Create panel2
        JPanel panel2 = new JPanel(new BorderLayout());

        JLabel studentListLabel = new JLabel("Student List For " + ClassValue);

        panel2.add(studentListLabel, BorderLayout.CENTER);
        panel2.add(tableSearchField, BorderLayout.EAST);

        // Create panel3
        JPanel panel3 = new JPanel();
        panel3.add(updateButton);
        panel3.add(deleteButton);

        // Create panel4
        JPanel panel4 = new JPanel(new BorderLayout());
        JLabel appliedStudentListLabel = new JLabel("Applied Student List");

        panel4.add(appliedStudentListLabel, BorderLayout.CENTER);
        panel4.add(emptyTableSearchField, BorderLayout.EAST);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(upperPanel, BorderLayout.NORTH);

        // Create left panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel rightPanel = new JPanel(new BorderLayout());
        leftPanel.add(panel1, BorderLayout.SOUTH);
        leftPanel.add(panel2, BorderLayout.NORTH);
        leftPanel.add(tableScrollPane, BorderLayout.CENTER);
        rightPanel.add(panel3, BorderLayout.SOUTH);
        rightPanel.add(panel4, BorderLayout.NORTH);
        rightPanel.add(emptyTableScrollPane, BorderLayout.CENTER);

        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500); // Adjust the divider location as desired

        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        getContentPane().add(mainPanel);

        // Add action listeners

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + currentTable);

            // Clear existing table data
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            emptyTableModel.setColumnCount(0); // Clear empty table columns

            // Add table data to the table model
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                if (!columnName.equalsIgnoreCase("pass") && !columnName.equalsIgnoreCase("userRole")) {
                    tableModel.addColumn(columnName);
                    emptyTableModel.addColumn(columnName); // Add columns to empty table model
                }
            }

            while (rs.next()) {
                String classValue = rs.getString("class"); // Assuming the column name is "class"
                String userRole = rs.getString("userRole");

                // Check conditions for adding the row
                if (classValue.equalsIgnoreCase(ClassValue) && userRole.equalsIgnoreCase("S")) {
                    Object[] row = new Object[columnCount];
                    int rowIndex = 0;
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsmd.getColumnName(i);
                        if (!columnName.equalsIgnoreCase("pass") && !columnName.equalsIgnoreCase("userRole")) {
                            row[rowIndex] = rs.getObject(i);
                            rowIndex++;
                        }
                    }
                    tableModel.addRow(row);
                }
            }

            // Close database connection
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load table: " + currentTable + " :: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void searchTable(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    private void searchEmptyTable(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(emptyTableModel);
        emptyTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    public static void getInfo() {

        sql sql = new sql();
        sql.getUserInfo(userKeys);

        // Call the getUserInfo method and retrieve the values
        username = sql.getUsername();
        ClassValue = sql.getClassValue();
        userRoleL = sql.getUserRoleL();

        userInfoLabel.setText(
                "Username: " + username + " | Class: " + ClassValue + " | Role: " + userRoleL);
    }

    private boolean isDuplicateRow(Object[] rowData) {
        for (int row = 0; row < emptyTable.getRowCount(); row++) {
            boolean duplicate = true;
            for (int col = 0; col < emptyTable.getColumnCount(); col++) {
                Object value = emptyTable.getValueAt(row, col);
                if (!value.equals(rowData[col])) {
                    duplicate = false;
                    break;
                }
            }
            if (duplicate) {
                return true; // Row already exists, it is a duplicate
            }
        }
        return false; // Row is not a duplicate
    }

}
