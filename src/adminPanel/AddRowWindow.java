package adminPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class AddRowWindow extends JFrame {
    private JTextField[] textFields;
    private DefaultTableModel tableModel;

    public AddRowWindow(String[] columnNames, int[] columnTypes, DefaultTableModel tableModel) {

        setTitle("Add Row");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setResizable(true);

        this.tableModel = tableModel;

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(columnNames.length + 1, 2));

        // Create text fields
        textFields = new JTextField[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) {
            JLabel label = new JLabel(columnNames[i]);
            textFields[i] = new JTextField();
            mainPanel.add(label);
            mainPanel.add(textFields[i]);
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
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow() {
        String[] rowData = new String[textFields.length];
        for (int i = 0; i < textFields.length; i++) {
            rowData[i] = textFields[i].getText();
        }
        tableModel.addRow(rowData);
        dispose(); // Close the window
    }
}
