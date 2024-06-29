package mainPage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TaskPanel extends JPanel {

    private ArrayList<TaskPage.Tasks> tasks; // Update to use TaskPage.Tasks
    private int userKeys;

    public TaskPanel(int userKeys, List<TaskPage.Tasks> tasksList) {
        this.userKeys = userKeys;
        tasks = new ArrayList<>();
        setPreferredSize(new Dimension(400, 600)); // Set the preferred size of the panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Use BoxLayout to stack the tasks vertically

        for (TaskPage.Tasks task : tasksList) {
            addTask(task);
        }
    }

    public void addTask(TaskPage.Tasks task) { // Update the parameter type to TaskPage.Tasks
        tasks.add(task);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int y = 0; // The y-coordinate for each task rectangle

        for (TaskPage.Tasks task : tasks) { // Update the loop to use TaskPage.Tasks
            g.setColor(Color.BLUE); // Set a default color, you can customize this based on task properties
            g.fillRect(0, y, getWidth(), 100); // Fills a rectangle from the top (y) to y + 100
            y += 100; // Increment the y-coordinate for the next task
        }
    }

    // Make the panel scrollable using JScrollPane
    public JScrollPane makeScrollable() {
        return new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
}
