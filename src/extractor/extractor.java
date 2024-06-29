package extractor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import sql.sql;

public class extractor extends JFrame {
    private static int selectedIdExamSet = -1;
    private static JButton selectButton;
    private static String firstColumnValue;
    private static JTable selectedtable;
    private static JTable table;
    private static DefaultTableModel tableModel;
    private static JTable table2;
    static DefaultTableModel tableModel2;
    private static JTable table3;
    public static DefaultTableModel tableModel3;
    private static DefaultTableModel currentTableModel;
    private String currentTable = "non";
    private JTextField searchField;
    protected Point clickPoint;
    public static int userKey;
    private static JLabel userInfoLabel;
    private static double zoomLevel = 1.0;
    private static JLabel imageLabel;
    private static JPanel imagePanel;
    private static ImageIcon imageIcon;
    private int dragStartX, dragStartY;
    private int imageOffsetX, imageOffsetY;
    private static JScrollPane scrollPane4;
    private static JPanel infoPane5;
    private static int selectedRow;
    private static int columnCount;
    private static int loadcount = -1;

    // da
    File f = null;
    String path = null;
    private ImageIcon format = null;
    String fname = null;
    int S = 0;

    Connection con;
    PreparedStatement pst;
    ResultSet IS;

    private ImageIcon formet = null;
    byte[] images;

    // da

    public extractor(int userKey2) throws IOException {
        this.userKey = userKey2;

        setTitle("Exam Set Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 800);

        // Create table model
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        tableModel2 = new DefaultTableModel();
        table2 = new JTable(tableModel2);
        tableModel3 = new DefaultTableModel();
        table3 = new JTable(tableModel3);

        JScrollPane scrollPane = new JScrollPane(table);
        JScrollPane scrollPane2 = new JScrollPane(table2);
        JScrollPane scrollPane3 = new JScrollPane(table3);
        String path = System.getProperty("user.dir") + "\\src\\resuorces\\ExamSet.PNG";
        File file = new File(path);

        BufferedImage image = ImageIO.read(file);

        imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(null);
        imageLabel.setBounds(0, 0, imageLabel.getIcon().getIconWidth(), imageLabel.getIcon().getIconHeight());

        imagePanel.add(imageLabel);

        JScrollPane scrollPane4 = new JScrollPane(imagePanel);

        imagePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                dragStartX = e.getX();
                dragStartY = e.getY();
            }
        });

        imagePanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                int dragDistanceX = e.getX() - dragStartX;
                int dragDistanceY = e.getY() - dragStartY;

                imageOffsetX += dragDistanceX;
                imageOffsetY += dragDistanceY;

                imageLabel.setLocation(imageOffsetX, imageOffsetY);

                dragStartX = e.getX();
                dragStartY = e.getY();
            }
        });

        imagePanel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

                int scrollDirection = e.getWheelRotation();

                zoomLevel += scrollDirection * 0.1;

                scaleAndPositionImage();

                imagePanel.repaint();
            }
        });
        imagePanel.setPreferredSize(new Dimension(scrollPane4.getPreferredSize()));

        selectButton = new JButton("Select Image");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
                int result = fileChooser.showOpenDialog(extractor.imagePanel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    imageLabel.setIcon(imageIcon);
                }
            }
        });

        JPanel infoPane5 = new JPanel();

        JPanel tableButtonPanel = new JPanel();
        JButton add_table_Button = new JButton("add");
        add_table_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentTable = "exam_set";
                currentTableModel = tableModel;

                openAddRowWindow();
            }
        });
        JButton delete_table_Button = new JButton("delete");
        delete_table_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedtable = table;
                currentTableModel = tableModel;
                deleteRow();
            }
        });
        tableButtonPanel.add(add_table_Button);
        tableButtonPanel.add(delete_table_Button);

        JPanel table2ButtonPanel = new JPanel();
        JButton add_table2_Button = new JButton("add");
        add_table2_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentTable = "q_set";
                currentTableModel = tableModel2;

                openAddRowWindow();
            }
        });
        JButton delete_table2_Button = new JButton("delete");
        delete_table2_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedtable = table2;
                currentTableModel = tableModel2;
                deleteRow();
            }
        });
        table2ButtonPanel.add(add_table2_Button);
        table2ButtonPanel.add(delete_table2_Button);

        JPanel table3ButtonPanel = new JPanel();
        JButton add_table3_Button = new JButton("add");
        add_table3_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentTable = "q_set";

                currentTableModel = tableModel3;
                openAddRowWindow();
            }
        });
        JButton delete_table3_Button = new JButton("delete");
        delete_table3_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedtable = table3;
                currentTableModel = tableModel3;
                deleteRow();
            }
        });
        table3ButtonPanel.add(add_table3_Button);
        table3ButtonPanel.add(delete_table3_Button);

        JButton showQButton = new JButton("Show Q");
        JButton showAllButton = new JButton("Show All");
        JButton updateButton = new JButton("Update set");
        JButton loadButton = new JButton("Load set");
        JButton loadPDFButton = new JButton("load pdf");

        int tableWidth = (int) (getWidth() * 0.15);
        scrollPane2.setPreferredSize(new Dimension(tableWidth, getHeight()));
        scrollPane4.setPreferredSize(new Dimension(tableWidth, getHeight()));
        tableButtonPanel.setPreferredSize(new Dimension(tableWidth, getHeight()));
        table2ButtonPanel.setPreferredSize(new Dimension(tableWidth, getHeight()));
        JPanel tablePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;

        scrollPane.setPreferredSize(new Dimension(tableWidth, getHeight()));
        tablePanel.add(scrollPane, gbc);

        gbc.gridx = 1;
        tablePanel.add(scrollPane2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0.5;
        tablePanel.add(scrollPane3, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.02;
        tablePanel.add(tableButtonPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.02;
        tablePanel.add(table2ButtonPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.02;
        tablePanel.add(table3ButtonPanel, gbc);

        JPanel detailPanel = new JPanel(new GridLayout(0, 6, 10, 10));

        JPanel detailPanel2 = new JPanel(new GridLayout(0, 6, 10, 10));

        JPanel detailPanel3 = new JPanel(new GridLayout(0, 6, 10, 10));

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

        buttonsPanel.add(showQButton);
        buttonsPanel.add(showAllButton);
        buttonsPanel.add(updateButton);

        buttonsPanel.add(loadButton);
        buttonsPanel.add(loadPDFButton);
        buttonsPanel.add(selectButton);

        upperPanel.add(buttonsPanel, BorderLayout.WEST);

        JButton zoomInButton = new JButton("+");
        JButton zoomOutButton = new JButton("-");
        JButton applyButton = new JButton("apply");
        JPanel zoomButtonPanel = new JPanel();
        zoomButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        zoomButtonPanel.add(applyButton);
        zoomButtonPanel.add(zoomInButton);
        zoomButtonPanel.add(zoomOutButton);

        JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane4, zoomButtonPanel);
        splitPane2.setDividerLocation(400);
        splitPane2.setResizeWeight(0.5);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane2, detailPanel);
        splitPane.setDividerLocation(450);

        splitPane.setResizeWeight(0.5);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(upperPanel, BorderLayout.NORTH);

        mainPanel.add(tablePanel, BorderLayout.WEST);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

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
        loadPDFButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        showQButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showQ();
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAll();
            }
        });
        zoomInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoomLevel += 0.1;
                scaleAndPositionImage();
            }
        });

        zoomOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoomLevel -= 0.1;
                scaleAndPositionImage();
            }
        });
        ListSelectionModel selectionModel = table.getSelectionModel();

        ListSelectionModel selectionModel2 = table2.getSelectionModel();
        selectionModel2.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting() && table2.getSelectedRow() != -1) {

                    detailPanel2.removeAll();

                    int selectedRow = table2.getSelectedRow();

                    firstColumnValue = String.valueOf(table2.getValueAt(selectedRow, 0));

                    TableColumnModel columnModel = table2.getColumnModel();
                    int columnCount = columnModel.getColumnCount();
                    for (int col = 0; col < columnCount; col++) {
                        String columnName = columnModel.getColumn(col).getHeaderValue().toString();
                        Object columnValue = table2.getValueAt(selectedRow, col);

                        JLabel columnNameLabel = new JLabel(columnName + ":");
                        detailPanel2.add(columnNameLabel);

                        JTextField columnValueField = new JTextField(String.valueOf(columnValue));

                        detailPanel2.add(columnValueField);
                    }

                    detailPanel2.revalidate();
                    detailPanel2.repaint();
                }
                splitPane.setRightComponent(detailPanel2);
                splitPane.revalidate();
                splitPane.repaint();
                splitPane.setDividerLocation(450);
                splitPane2.setDividerLocation(400);

                String selectedID = String.valueOf(table2.getValueAt(selectedRow, 0));
                System.out.println(selectedID);
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

                    PreparedStatement stmt = con.prepareStatement("SELECT Q_image FROM q_set WHERE idQ_set = ?");
                    stmt.setString(1, selectedID);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        images = rs.getBytes("Q_image");
                    }

                    rs.close();
                    stmt.close();

                    imageIcon = new ImageIcon(images);
                    imageLabel.setIcon(imageIcon);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Handle any exceptions related to the database query.
                } catch (ClassNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }

        });
        ListSelectionModel selectionModel3 = table3.getSelectionModel();
        selectionModel3.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table3.getSelectedRow() != -1) {
                    detailPanel3.removeAll();
                    int selectedRow = table3.getSelectedRow();
                    firstColumnValue = String.valueOf(table3.getValueAt(selectedRow, 0));
                    TableColumnModel columnModel = table3.getColumnModel();
                    int columnCount = columnModel.getColumnCount();
                    for (int col = 0; col < columnCount; col++) {
                        String columnName = columnModel.getColumn(col).getHeaderValue().toString();
                        Object columnValue = table3.getValueAt(selectedRow, col);
                        JLabel columnNameLabel = new JLabel(columnName + ":");
                        detailPanel3.add(columnNameLabel);
                        JTextField columnValueField = new JTextField(String.valueOf(columnValue));
                        detailPanel3.add(columnValueField);
                    }
                    detailPanel3.revalidate();
                    detailPanel3.repaint();
                    showiamge(firstColumnValue);
                }
                splitPane.setRightComponent(detailPanel3);
                splitPane.revalidate();
                splitPane.repaint();
                splitPane.setDividerLocation(450);
                splitPane2.setDividerLocation(400);
            }
        });

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Object[] rowData = new Object[table.getColumnCount()];
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        rowData[i] = table.getValueAt(selectedRow, i);
                    }

                    // Get the idexam_set value from the selected row
                    String idexamSet = rowData[0].toString();
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
                        Statement stmt = con.createStatement();
                        String query = "SELECT * FROM q_set WHERE Q_examSet_id = " + idexamSet;
                        ResultSet rs = stmt.executeQuery(query);

                        // Clear existing table data and columns
                        tableModel2.setRowCount(0);
                        tableModel2.setColumnCount(0);

                        // Add table data to the table model
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = rsmd.getColumnName(i);
                            tableModel2.addColumn(columnName);
                        }

                        // Add the fetched row to the empty table model
                        if (rs.next()) {
                            Object[] row = new Object[columnCount];
                            int rowIndex = 0;
                            for (int i = 1; i <= columnCount; i++) {
                                row[rowIndex] = rs.getObject(i);
                                rowIndex++;
                            }
                            tableModel2.addRow(row);
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
                    detailPanel.revalidate();
                    detailPanel.repaint();
                }
            }
        });

        loadTable();

    }

    private void searchTable(String searchText) {
        if (currentTable == "exam_set") {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            table.setRowSorter(sorter);
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));

        }
        if (currentTable == "q_set") {
            TableRowSorter<DefaultTableModel> sorter2 = new TableRowSorter<>(tableModel2);
            table2.setRowSorter(sorter2);
            sorter2.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));

        }
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

            int[] columnTypes = new int[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnTypes[i - 1] = rsmd.getColumnType(i);
                System.out.println(rsmd.getColumnType(i));
            }

            AddRowWindow addRowWindow = new AddRowWindow(columnNames, columnTypes, currentTableModel, currentTable);
            addRowWindow.setLocationRelativeTo(null);
            addRowWindow.setVisible(true);

            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve column names: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRow() {
        int selectedRow = selectedtable.getSelectedRow();
        if (selectedRow != -1) {
            currentTableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "No row selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            Statement clearStmt = con.createStatement();
            clearStmt.executeUpdate("DELETE FROM " + "exam_set");

            int rowCount = tableModel.getRowCount();
            int columnCount = tableModel.getColumnCount();
            for (int row = 0; row < rowCount; row++) {
                StringBuilder queryBuilder = new StringBuilder("INSERT INTO " + "exam_set" + " VALUES (");
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

            con.close();

            JOptionPane.showMessageDialog(this, "Database updated successfully." + "exam_set", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update database: " + "exam_set" + " :: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            int rowCount = tableModel2.getRowCount();
            int columnCount = tableModel2.getColumnCount();
            for (int row = 0; row < rowCount; row++) {
                StringBuilder queryBuilder = new StringBuilder("REPLACE INTO q_set VALUES (");
                for (int col = 0; col < columnCount - 1; col++) {
                    Object value = tableModel2.getValueAt(row, col);
                    if (value != null) {
                        queryBuilder.append("'").append(value.toString()).append("'");
                    } else {
                        queryBuilder.append("NULL");
                    }
                    if (col != columnCount - 2) {
                        queryBuilder.append(", ");
                    }
                }

                queryBuilder.append(", '").append("Q_image").append("')");

                Statement insertStmt = con.createStatement();
                insertStmt.executeUpdate(queryBuilder.toString());
            }

            con.close();

            JOptionPane.showMessageDialog(this, "Database updated successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update database: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    private void loadTable() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
            Statement stmt = con.createStatement();

            ResultSet rs1 = stmt.executeQuery("SELECT * FROM exam_set");
            ResultSetMetaData rsmd1 = rs1.getMetaData();
            int columnCount1 = rsmd1.getColumnCount();

            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            for (int i = 1; i <= columnCount1; i++) {
                tableModel.addColumn(rsmd1.getColumnName(i));
            }

            while (rs1.next()) {
                Object[] row = new Object[columnCount1];
                for (int i = 1; i <= columnCount1; i++) {
                    row[i - 1] = rs1.getObject(i);
                }
                tableModel.addRow(row);
            }

            ResultSet rs2 = stmt.executeQuery("SELECT * FROM q_set");
            ResultSetMetaData rsmd2 = rs2.getMetaData();
            int columnCount2 = rsmd2.getColumnCount();

            tableModel2.setRowCount(0);
            tableModel2.setColumnCount(0);

            for (int i = 1; i <= columnCount2; i++) {
                if (loadcount == -1) {
                    tableModel3.addColumn(rsmd2.getColumnName(i));
                }

                tableModel2.addColumn(rsmd2.getColumnName(i));
            }

            // Add rows to table2
            while (rs2.next()) {
                Object[] row = new Object[columnCount2];
                for (int i = 1; i <= columnCount2; i++) {
                    Object columnValue = rs2.getObject(i);
                    if (rsmd2.getColumnTypeName(i).equalsIgnoreCase("BLOB")) {
                        row[i - 1] = "image";
                    } else {
                        row[i - 1] = columnValue;
                    }
                }
                tableModel2.addRow(row);
            }

            loadcount += 1;

            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load tables: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void addRowToTableModel(String[] rowData, DefaultTableModel currentTableModel) {
        currentTableModel.addRow(rowData);
    }

    public static void getInfo() {

        sql sql = new sql();
        sql.getUserInfo(userKey);

        String username = sql.getUsername();
        String Class = sql.getClassValue();
        String userRoleL = sql.getUserRoleL();

        userInfoLabel.setText(
                "Username: " + username + " | Class: " + Class + " | Role: " + userRoleL);
    }

    private static void showAll() {
        table2.clearSelection();
        TableRowSorter<DefaultTableModel> sorter2 = new TableRowSorter<>(tableModel2);
        table2.setRowSorter(sorter2);
        sorter2.setRowFilter(null);
    }

    private static void showQ() {
        int selectedIdExamSet1 = (int) table.getValueAt(table.getSelectedRow(), 0);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel2);
        table2.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter(String.valueOf(selectedIdExamSet1), 2));

        System.out.println(selectedIdExamSet1);
    }

    private void scaleAndPositionImage() {

        int scaledWidth = (int) (imageIcon.getIconWidth() * zoomLevel);
        int scaledHeight = (int) (imageIcon.getIconHeight() * zoomLevel);

        imageLabel.setSize(scaledWidth, scaledHeight);

        JViewport viewport = scrollPane4.getViewport();
        int viewWidth = viewport.getWidth();
        int viewHeight = viewport.getHeight();
        int newOffsetX = (viewWidth - scaledWidth) / 2;
        int newOffsetY = (viewHeight - scaledHeight) / 2;
        viewport.setViewPosition(new Point(newOffsetX, newOffsetY));
    }

    private void showRowInfo() {

        infoPane5.removeAll();

        int selectedRow = table.getSelectedRow();

        int columnCount = currentTableModel.getColumnCount();
        Object[] rowData = new Object[columnCount];
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            rowData[columnIndex] = currentTableModel.getValueAt(selectedRow, columnIndex);
        }

        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            JLabel label = new JLabel(currentTableModel.getColumnName(columnIndex));
            JTextField textField = new JTextField(rowData[columnIndex].toString());

            infoPane5.add(label);
            infoPane5.add(textField);
        }

        infoPane5.revalidate();
        infoPane5.repaint();
    }

    public static byte[] stringToByteArray(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    public static void showiamge(String idQ_pri) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
            Statement stmt = con.createStatement();

            ResultSet rs1 = stmt.executeQuery("SELECT * FROM q_set");
            ResultSetMetaData rsmd1 = rs1.getMetaData();
            int columnCount1 = rsmd1.getColumnCount();

            String query = "SELECT Q_image FROM q_set WHERE idQ_set = " + idQ_pri;
            ResultSet rs2 = stmt.executeQuery(query);
            if (rs2.next()) {
                Blob blob = rs2.getBlob("Q_image");
                InputStream inputStream = blob.getBinaryStream();

                BufferedImage image = ImageIO.read(inputStream);
                String filename = "image.png";
                ImageIO.write(image, "png", new File(filename));

                ImageIcon imageIcon = new ImageIcon(filename);
                JLabel imageLabel = new JLabel(imageIcon);
                imagePanel.add(imageLabel);

                imagePanel.revalidate();
                imagePanel.repaint();

                inputStream.close();
            }

            stmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Failed to load tables: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        extractor extractor = new extractor(userKey);
        extractor.setVisible(true);
    }
}
