package mainPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImagePanelDemo extends JFrame {
    private JLabel imageLabel;
    private JButton selectButton;

    public ImagePanelDemo() {
        setTitle("Image Panel Demo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLayout(new FlowLayout());

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(300, 300));
        add(imageLabel);

        selectButton = new JButton("Select Image");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
                int result = fileChooser.showOpenDialog(ImagePanelDemo.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    imageLabel.setIcon(imageIcon);
                }
            }
        });
        add(selectButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ImagePanelDemo();
            }
        });
    }
}
