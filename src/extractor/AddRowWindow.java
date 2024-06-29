package extractor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class AddRowWindow extends JFrame {
    private JComponent[] inputComponents;
    private DefaultTableModel tableModel;
    private String tablename;

    private static byte[] imageData;
    private static JPanel mainPanel;

    File f = null;
    String path = null;
    private ImageIcon format = null;
    String fname = null;
    int S = 0;
    byte[] photo = null;

    String imagePath = null;

    Connection con;
    PreparedStatement pst;
    ResultSet IS;

    private String highestId;

    public AddRowWindow(String[] columnNames, int[] columnTypes, DefaultTableModel tableModel, String tablename) {
        Connect();

        setTitle("Add Row");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setResizable(true);

        this.tableModel = tableModel;
        this.tablename = tablename;

        // Create main panel
        mainPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(columnNames.length + 1, 2);

        mainPanel.setLayout(gridLayout);

        JPanel mooPanel = new JPanel(new GridLayout(1, 2));
        mooPanel.add(mainPanel);

        // Create text fields
        inputComponents = new JComponent[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            JLabel label = new JLabel(columnNames[i]);
            if (columnTypes[i] == -4) {
                JButton button = new JButton("Select Image");
                JLabel imageLabel = new JLabel("No image selected"); // Initial text when no image is selected
                imageLabel.setPreferredSize(new Dimension(500, 500));
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        
                        try {
                            imageLabel.setText(null);
                            handleImageSelection(imageLabel);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                
                mainPanel.add(label);
                mainPanel.add(button);
                // Add an empty label as a placeholder
                mooPanel.add(imageLabel); // Add the imageLabel to the mainPanel
            } else {
                JTextField textField = new JTextField();
                inputComponents[i] = textField;
                mainPanel.add(label);
                mainPanel.add(textField);
                if (i == 0) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
                        String selectQuery = "SELECT MAX(" + columnNames[0] + ") FROM " + tablename;
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(selectQuery);

                        if (rs.next()) {
                            int highestValue = rs.getInt(1);
                            textField.setText(String.valueOf(highestValue + 1));
                        }

                        rs.close();
                        stmt.close();
                        con.close();
                    } catch (Exception e) {
                        // Handle any exceptions
                        e.printStackTrace();
                    }
                }

            }
        }

        // Create buttons
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the window
            }
        });

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        // Add components to the main panel
        getContentPane().add(mooPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow() {
        String[] rowData = new String[inputComponents.length];
        String[] columnNames = new String[inputComponents.length];

        for (int i = 0; i < inputComponents.length; i++) {
            JComponent inputComponent = inputComponents[i];
            JLabel label = (JLabel) mainPanel.getComponent(i * 2);
            columnNames[i] = label.getText();

            if (inputComponent instanceof JTextField) {
                rowData[i] = ((JTextField) inputComponent).getText();
                if (i == 0) {
                    highestId = ((JTextField) inputComponent).getText();
                }
            } else if (inputComponent instanceof JButton) {
                rowData[i] = "image";
            }
        }
        tableModel.addRow(rowData);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

            // Generate column names and placeholders dynamically
            StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tablename + " (");
            StringBuilder placeholders = new StringBuilder("VALUES (");

            for (int i = 0; i < columnNames.length; i++) {
                insertQuery.append(columnNames[i]);
                placeholders.append("?");

                if (i < columnNames.length - 1) {
                    insertQuery.append(", ");
                    placeholders.append(", ");
                }
            }

            insertQuery.append(") ");
            placeholders.append(")");

            insertQuery.append(placeholders);

            PreparedStatement preparedStatement = con.prepareStatement(insertQuery.toString());

            for (int i = 0; i < rowData.length; i++) {
                if (inputComponents[i] instanceof JButton) {

                    preparedStatement.setBytes(i + 1, photo);// Set the image data for Q_image column
                } else {
                    preparedStatement.setString(i + 1, rowData[i]); // Set the value for other columns
                }
            }

            preparedStatement.executeUpdate();
            preparedStatement.close();
            con.close();

            JOptionPane.showMessageDialog(this, "Row added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add row: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        try {

            pst = con.prepareStatement("UPDATE  q_set SET Q_image = ? WHERE idQ_set = ?");

            pst.setBytes(1, photo);
            pst.setString(2, highestId);
            int inserted = pst.executeUpdate();
            if (inserted > 0) {
                JOptionPane.showMessageDialog(null, "Image successfully inserted");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        dispose(); // Close the window
    }

    public static String byteArrayToString(byte[] byteArray) {
        return new String(byteArray, StandardCharsets.UTF_8);
    }

    private void handleImageSelection(JLabel imagePP) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fnwf = new FileNameExtensionFilter("PNG JPG AND JPEG", "png", "jpeg", "jpg");
        fileChooser.addChoosableFileFilter(fnwf);
        int load = fileChooser.showOpenDialog(null);
        if (load == fileChooser.APPROVE_OPTION)
            f = fileChooser.getSelectedFile();
        path = f.getAbsolutePath();

        ImageIcon ii = new ImageIcon(path);
        Image img = ii.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);

        imagePP.setIcon(new ImageIcon(img));

        try {
            File image = new File(path);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
            photo = bos.toByteArray();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Failed to load tables: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
