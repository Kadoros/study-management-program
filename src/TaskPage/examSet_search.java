package TaskPage;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import sql.sql;

public class examSet_search extends JFrame {
    private JTable table;
    private JTable emptyTable; // New table without rows
    private DefaultTableModel tableModel;
    private DefaultTableModel emptyTableModel;
    private String currentTable = "exam_set";
    private JTextField tableSearchField;
    private JTextField emptyTableSearchField;

    private static JLabel userInfoLabel;

    private String names;
    private static int userKeys;
    private ArrayList taskForShow;
    private ArrayList taskForvalue;
    private String titles;
    private String days;
    private String times;
    private String modes;
    private String details;
    private int taskInIdExamvalue;
    private int idexamSet;

    public examSet_search(int userKey, ArrayList taskForSho, ArrayList taskForvalu, String title, String day,
            String time,
            String mode, String detail, int taskInIdExamvalu) {
        this.userKeys = userKey;
        this.taskForShow = taskForSho;
        this.taskForvalue = taskForvalu;
        this.titles = title;
        this.days = day;
        this.times = time;
        this.modes = mode;
        this.details = detail;
        this.taskInIdExamvalue = taskInIdExamvalu;

        setTitle("Exam Set Search");
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

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setTask setTask;
                setTask = new setTask(userKeys, taskForShow, taskForvalue, titles, days, times, modes, details,
                        idexamSet);

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

        panel1.add(updateButton);

        // Create panel2
        JPanel panel2 = new JPanel(new BorderLayout());

        JLabel studentListLabel = new JLabel("Exam Set List");

        panel2.add(studentListLabel, BorderLayout.CENTER);
        panel2.add(tableSearchField, BorderLayout.EAST);

        JPanel panel3 = new JPanel();
        JPanel detailPanel = new JPanel(new GridLayout(0, 6));
        JPanel detailPanel2 = new JPanel(new GridLayout(0, 6));

        emptyTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = emptyTable.getSelectedRow();
                if (selectedRow != -1) {
                    Object[] rowData = new Object[emptyTable.getColumnCount()];
                    for (int i = 0; i < emptyTable.getColumnCount(); i++) {
                        rowData[i] = emptyTable.getValueAt(selectedRow, i);
                    }

                }
                if (!e.getValueIsAdjusting() && emptyTable.getSelectedRow() != -1) {
                    detailPanel2.removeAll();

                    TableColumnModel columnModel = emptyTable.getColumnModel();
                    for (int col = 0; col < columnModel.getColumnCount(); col++) {
                        String columnName = columnModel.getColumn(col).getHeaderValue().toString();
                        Object columnValue = emptyTable.getValueAt(selectedRow, col);

                        JLabel columnNameLabel = new JLabel(columnName + ":");
                        JTextField columnValueField = new JTextField(String.valueOf(columnValue));

                        detailPanel.add(columnNameLabel);
                        detailPanel.add(columnValueField);
                    }

                    panel3.add(detailPanel2);
                    panel3.revalidate();
                    panel3.repaint();
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Object[] rowData = new Object[table.getColumnCount()];
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        rowData[i] = table.getValueAt(selectedRow, i);
                    }

                    // Get the idexam_set value from the selected row
                    int idExamSetValue = (Integer) rowData[0];
                    idexamSet = idExamSetValue;

                    // Fetch the corresponding row from the q_set table based on idexam_set
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado",
                                "1130Coolhan@");
                        Statement stmt = con.createStatement();
                        String query = "SELECT * FROM q_set WHERE Q_examSet_id = " + String.valueOf(idExamSetValue);
                        ResultSet rs = stmt.executeQuery(query);

                        // Clear existing table data and columns
                        emptyTableModel.setRowCount(0);
                        emptyTableModel.setColumnCount(0);

                        // Add table data to the table model
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = rsmd.getColumnName(i);
                            emptyTableModel.addColumn(columnName);
                        }

                        // Add the fetched row to the empty table model
                        if (rs.next()) {
                            Object[] row = new Object[columnCount];
                            int rowIndex = 0;
                            for (int i = 1; i <= columnCount; i++) {
                                row[rowIndex] = rs.getObject(i);
                                rowIndex++;
                            }
                            emptyTableModel.addRow(row);
                        }

                        // Close database connection
                        con.close();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Failed to load table: " + ex.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    detailPanel.removeAll();
                    TableColumnModel columnModel = table.getColumnModel();
                    for (int col = 0; col < columnModel.getColumnCount(); col++) {
                        String columnName = columnModel.getColumn(col).getHeaderValue().toString();
                        Object columnValue = table.getValueAt(selectedRow, col);

                        JLabel columnNameLabel = new JLabel(columnName + ":");
                        JTextField columnValueField = new JTextField(String.valueOf(columnValue));

                        detailPanel.add(columnNameLabel);
                        detailPanel.add(columnValueField);
                    }
                    panel3.add(detailPanel);
                    panel3.revalidate();
                    panel3.repaint();
                }
            }
        });

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
        rightPanel.add(emptyTableScrollPane, BorderLayout.NORTH);

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
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado", "1130Coolhan@");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + currentTable);

            // Clear existing table data
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            // Add table data to the table model
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                if (!columnName.equalsIgnoreCase("pass") && !columnName.equalsIgnoreCase("userRole")) {
                    tableModel.addColumn(columnName);

                }
            }
            while (rs.next()) {
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

            // Close database connection
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load table: " + currentTable + " :: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado", "1130Coolhan@");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM q_set LIMIT 1"); // Query to fetch a single row from the
                                                                             // q_set table

            // Clear existing table data and columns
            emptyTableModel.setRowCount(0);
            emptyTableModel.setColumnCount(0);

            // Add table data to the table model
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                emptyTableModel.addColumn(columnName);
            }

            // Close database connection
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load table columns: " + e.getMessage(),
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
        String username = sql.getUsername();
        String Class = sql.getClassValue();
        String userRoleL = sql.getUserRoleL();

        userInfoLabel.setText(
                "Username: " + username + " | Class: " + Class + " | Role: " + userRoleL);
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

    private JPanel createDetailPanel(JTable targetTable) {
        JPanel detailPanel = new JPanel(new GridLayout(0, 2));

        int selectedRow = targetTable.getSelectedRow();
        TableColumnModel columnModel = targetTable.getColumnModel();
        for (int col = 0; col < columnModel.getColumnCount(); col++) {
            String columnName = columnModel.getColumn(col).getHeaderValue().toString();
            Object columnValue = targetTable.getValueAt(selectedRow, col);

            JLabel columnNameLabel = new JLabel(columnName + ":");
            JTextField columnValueField = new JTextField(String.valueOf(columnValue));

            detailPanel.add(columnNameLabel);
            detailPanel.add(columnValueField);
        }

        return detailPanel;
    }

}
