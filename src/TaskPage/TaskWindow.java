package TaskPage;

import javax.swing.*;

import question.QuestionnaireApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskWindow extends JFrame {
    private Tasks task;
    private int userKey;

    public TaskWindow(Tasks task, int userKeys) {
        this.task = task;
        this.userKey = userKeys;

        setTitle("Task Information");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // Create components for task information
        JLabel titleLabel = new JLabel("Task Title: " + task.getTaskTitle());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));

        JLabel setByLabel = new JLabel("Task Set By: " + task.getTaskSetBy());
        JLabel dueDayLabel = new JLabel("Task Due Day: " + task.getTaskDueDay());
        JLabel timeLabel = new JLabel("Task Time: " + task.getTaskTime() + " min");
        JLabel detailLabel = new JLabel("Task Detail: " + task.getTaskDetail());
        JLabel percentageLabel = new JLabel("Task percentage: " + task.getTaskPercentage() + "%");
        JLabel forLabel = new JLabel("Task For: " + task.getTaskFor());
        JLabel finishLabel = new JLabel("Task finish: " + task.getTaskFinish());
        JLabel gradeLabel = new JLabel("Task grade: " + task.getTaskGrade());
        JLabel modeLabel = new JLabel("Task mode: " + task.getTaskMode());

        // Create small labels for idTaskSQL and Task_in_idExam
        JLabel idLabel = new JLabel("idTaskSQL: " + String.valueOf(task.getIdTaskSQL()));
        idLabel.setFont(idLabel.getFont().deriveFont(Font.BOLD, 8));
        JLabel examLabel = new JLabel("Task_in_idExam: " + String.valueOf(task.getTaskInIdExam()));
        examLabel.setFont(examLabel.getFont().deriveFont(Font.BOLD, 8));

        // Create buttons for start and cancel
        JButton startButton = new JButton("Start");
        JButton cancelButton = new JButton("Cancel");

        // Create a panel to hold the components
        JPanel taskInfoPanel = new JPanel(new GridLayout(9, 1));
        taskInfoPanel.add(titleLabel);
        taskInfoPanel.add(setByLabel);
        taskInfoPanel.add(dueDayLabel);
        taskInfoPanel.add(timeLabel);
        taskInfoPanel.add(detailLabel);
        taskInfoPanel.add(percentageLabel);
        taskInfoPanel.add(forLabel);
        taskInfoPanel.add(finishLabel);
        taskInfoPanel.add(gradeLabel);
        taskInfoPanel.add(modeLabel);

        taskInfoPanel.add(startButton);
        taskInfoPanel.add(cancelButton);
        taskInfoPanel.add(idLabel);
        taskInfoPanel.add(examLabel);

        // Set the alignment of the small labels to the bottom right corner
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        examLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Add action listeners for the buttons
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                QuestionnaireApp QuestionnaireApp = new QuestionnaireApp(userKey, task);
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Set the content pane
        setContentPane(taskInfoPanel);
    }

}
