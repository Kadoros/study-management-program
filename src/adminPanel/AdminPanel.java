package adminPanel;

import sql.sql;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import adminPanel.AdminPanel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminPanel extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private String currentTable = "grade";
    private JTextField searchField;
    public static int userKey;
    private static JLabel userInfoLabel;

    public AdminPanel(int userKey2) {
        this.userKey = userKey2;

        setTitle("Admin Panel");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);

        // Create table model
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        // Create scroll pane and add table to it
        JScrollPane scrollPane = new JScrollPane(table);

        // Create buttons
        JButton addButton = new JButton("Add Row");
        JButton deleteButton = new JButton("Delete Row");
        JButton updateButton = new JButton("Update");
        JButton loadButton = new JButton("Load");

        JComboBox<String> tableComboBox = new JComboBox<>();

        // Create upper panel
        JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel infoPanel = new JPanel();
        JLabel tableNameLabel = new JLabel("Name of Table");
        userInfoLabel = new JLabel();

        searchField = new JTextField(10);
        getInfo();
        infoPanel.add(userInfoLabel, BorderLayout.WEST);
        upperPanel.add(searchField, BorderLayout.EAST);

        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                searchTable(searchText);
            }
        });

        // Add buttons to the buttons panel
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(tableComboBox);
        buttonsPanel.add(loadButton);

        // Add components to the info panel

        // make detailPanel
        JPanel detailPanel = new JPanel(new GridLayout(0, 6, 25, 25));
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                // Check if the selection is adjusting and if there is a selected row
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    // Clear the detail panel
                    detailPanel.removeAll();

                    // Get the selected row
                    int selectedRow = table.getSelectedRow();

                    // Get the column names and corresponding values from the table model
                    TableColumnModel columnModel = table.getColumnModel();
                    int columnCount = columnModel.getColumnCount();
                    for (int col = 0; col < columnCount; col++) {
                        String columnName = columnModel.getColumn(col).getHeaderValue().toString();
                        Object columnValue = table.getValueAt(selectedRow, col);
                        detailPanel.add(new JLabel(columnName + ":"));
                        detailPanel.add(new JLabel(String.valueOf(columnValue)));
                    }

                    // Revalidate and repaint the detail panel to update the UI
                    detailPanel.revalidate();
                    detailPanel.repaint();
                }
            }

        });

        // Add components to the upper panel
        upperPanel.add(buttonsPanel, BorderLayout.WEST);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(detailPanel, BorderLayout.SOUTH);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        getContentPane().add(mainPanel);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddRowWindow();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteRow();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDatabase();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadTable();
            }
        });

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            // Clear existing data in the table
            Statement clearStmt = con.createStatement();
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[] { "TABLE" });
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                tableComboBox.addItem(tableName);

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        tableComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedTable = (String) tableComboBox.getSelectedItem();
                // Use the selectedTable text as needed
                currentTable = selectedTable;
            }
        });

    }

    private void searchTable(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }

    private void openAddRowWindow() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + currentTable);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = rsmd.getColumnName(i);
            }

            // Create an array to store the column data types
            int[] columnTypes = new int[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnTypes[i - 1] = rsmd.getColumnType(i);
            }

            AddRowWindow addRowWindow = new AddRowWindow(columnNames, columnTypes, tableModel);
            addRowWindow.setLocationRelativeTo(null);
            addRowWindow.setVisible(true);

            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve column names: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "No row selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            // Clear existing data in the table
            Statement clearStmt = con.createStatement();
            clearStmt.executeUpdate("DELETE FROM " + currentTable);

            // Insert new data from the table model
            int rowCount = tableModel.getRowCount();
            int columnCount = tableModel.getColumnCount();
            for (int row = 0; row < rowCount; row++) {
                StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + currentTable + " VALUES (");
                for (int col = 0; col < columnCount; col++) {
                    Object value = tableModel.getValueAt(row, col);
                    if (value != null) {
                        queryBuilder.append("'").append(value.toString()).append("'");
                    } else {
                        queryBuilder.append("NULL");
                    }
                    if (col != columnCount - 1) {
                        queryBuilder.append(", ");
                    }
                }
                queryBuilder.append(")");
                Statement insertStmt = con.createStatement();
                insertStmt.executeUpdate(queryBuilder.toString());
            }

            // Close database connection
            con.close();

            JOptionPane.showMessageDialog(this, "Database updated successfully." + currentTable, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update database: " + currentTable + " :: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTable() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + currentTable);

            // Clear existing table data
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            // Add table data to the table model
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(rsmd.getColumnName(i));
            }
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }

            // Close database connection
            con.close();
            JOptionPane.showMessageDialog(this, "Table loaded successfully. :" + currentTable, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load table : " + currentTable + " :: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void addRowToTableModel(String[] rowData, DefaultTableModel tableModel) {
        tableModel.addRow(rowData);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection(
                            "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7632350",
                            "sql7632350",
                            "NPNuEsGVn5");
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM grade");

                    // Create the admin panel
                    AdminPanel adminPanel = new AdminPanel(1);
                    adminPanel.setLocationRelativeTo(null);
                    // Add table data to the table model
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        adminPanel.tableModel.addColumn(rsmd.getColumnName(i));
                    }
                    while (rs.next()) {
                        Object[] row = new Object[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            row[i - 1] = rs.getObject(i);
                        }
                        adminPanel.tableModel.addRow(row);
                    }

                    // Close database connection
                    con.close();

                    // Show the admin panel
                    adminPanel.setVisible(true);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }
}
