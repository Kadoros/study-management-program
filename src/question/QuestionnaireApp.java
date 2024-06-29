package question;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import TaskPage.Tasks;
import sql.sql;

public class QuestionnaireApp extends JFrame {
    public static int userKey;
    private JFrame frame;
    private JLabel timerLabel;
    private JTextArea questionTextArea;
    private JPanel questionPanel;
    private JLabel imageLabel;
    private JTextArea answerTextArea;
    private JScrollPane answerScrollPane;
    private JPanel answerButtonPane;
    private JButton previousButton;
    private JButton nextButton;
    private int currentQuestionIndex = 0;
    private static JLabel userInfoLabel;
    private int ExamID;
    private JPanel mainPanel;
    private JButton answerButtonA;
    private JButton answerButtonB;
    private JButton answerButtonC;
    private JButton answerButtonD;
    private JLabel answerLabelA;
    private JLabel answerLabelB;
    private JLabel answerLabelC;
    private JLabel answerLabelD;
    private JPanel answerPanel;
    private Timer timer;
    private int timeRemaining;
    private JButton submitButton;
    private JButton BackToQuestionButton;
    private JScrollPane SP;
    private JPanel timerPanel = new JPanel();

    private JPanel submitPanel;
    private JLabel submitStatusLabel;
    private JLabel closeWindowLabel;

    private JButton reviewButton;

    private List<Question> questionList = new ArrayList<>();
    private static List<String> userAnswersList = new ArrayList<>();

    private static QuestionReviewPanel reviewPanel;

    private static Tasks tasks;

    

    public QuestionnaireApp(int userKey, Tasks Task) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.userKey = userKey;
        this.ExamID = Task.getTaskInIdExam();
        this.timeRemaining = Integer.parseInt(Task.getTaskTime()) * 60;
        this.tasks = Task;
        createAndShowGUI();
        getInfo();
        setLocationRelativeTo(null);
        userAnswersList.clear();
        try {
            questionList = getQuestionInfo(userKey, ExamID); // Retrieve the list of Question objects

            for (int i = 0; i < questionList.size(); i++) {
                userAnswersList.add("");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception if needed
        }
        updateQuestionDisplay();
    }

    private void createAndShowGUI() {

        setTitle("Questionnaire App");

        setSize(1000, 800);
        setLayout(new BorderLayout());

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Decrease the time remaining by 1 second
                timeRemaining--;

                // Update the timer label with the remaining time
                String timeStr = String.format("%02d:%02d", timeRemaining / 60, timeRemaining % 60);
                timerLabel.setText("Time Remaining: " + timeStr);

                // Check if the time has run out
                if (timeRemaining <= 0) {
                    timer.stop();
                    showReviewPage();
                    BackToQuestionButton.setVisible(false);
                }
            }
        });
        timer.start();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        timerLabel = new JLabel();
        questionTextArea = new JTextArea();
        imageLabel = new JLabel();
        answerTextArea = new JTextArea(5, 60);

        answerScrollPane = new JScrollPane(answerTextArea);
        answerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        answerButtonA = new JButton("\u274E A");
        answerButtonB = new JButton("\u274E B");
        answerButtonC = new JButton("\u274E C");
        answerButtonD = new JButton("\u274E D");

        answerButtonPane = new JPanel(new GridLayout(1, 4));
        answerButtonPane.add(answerButtonA);
        answerButtonPane.add(answerButtonB);
        answerButtonPane.add(answerButtonC);
        answerButtonPane.add(answerButtonD);

        answerButtonA.addActionListener(e -> {
            answerButtonA.setText("\u2714\uFE0F A");
            answerButtonB.setText("\u274E B");
            answerButtonC.setText("\u274E C");
            answerButtonD.setText("\u274E D");
            userAnswersList.set(currentQuestionIndex, "A");
        });
        answerButtonB.addActionListener(e -> {
            answerButtonA.setText("\u274E A");
            answerButtonB.setText("\u2714\uFE0F B");
            answerButtonC.setText("\u274E C");
            answerButtonD.setText("\u274E D");
            userAnswersList.set(currentQuestionIndex, "B");
        });
        answerButtonC.addActionListener(e -> {
            answerButtonA.setText("\u274E A");
            answerButtonB.setText("\u274E B");
            answerButtonC.setText("\u2714\uFE0F C");
            answerButtonD.setText("\u274E D");
            userAnswersList.set(currentQuestionIndex, "C");
        });
        answerButtonD.addActionListener(e -> {
            answerButtonA.setText("\u274E A");
            answerButtonB.setText("\u274E B");
            answerButtonC.setText("\u274E C");
            answerButtonD.setText("\u2714\uFE0F D");
            userAnswersList.set(currentQuestionIndex, "D");
        });

        answerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");

        JPanel containerPanel = new JPanel(new BorderLayout());
        JPanel totalPanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel();
        userInfoLabel = new JLabel();
        infoPanel.add(userInfoLabel, BorderLayout.EAST);
        Dimension preferredSize = new Dimension(infoPanel.getPreferredSize().width, 30);
        infoPanel.setPreferredSize(preferredSize);

        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        navigationPanel.add(previousButton);
        navigationPanel.add(nextButton);

        previousButton.addActionListener(e -> {
            if (currentQuestionIndex > 0) {

                updateUserAnswerList();
                currentQuestionIndex--;
                System.out.println(userAnswersList);
                // answerTextArea.removeAll();
                updateQuestionDisplay();
            }
        });

        nextButton.addActionListener(e -> {
            if (currentQuestionIndex < questionList.size() - 1) {
                updateUserAnswerList();
                currentQuestionIndex++;
                // answerTextArea.removeAll();
                System.out.println(userAnswersList);
                updateQuestionDisplay();
            }
        });
        BackToQuestionButton = new JButton("Back to Question");
        navigationPanel.add(BackToQuestionButton);
        BackToQuestionButton.addActionListener(e -> backToPreviousQuestion());
        BackToQuestionButton.setVisible(false);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> submit());
        navigationPanel.add(submitButton);
        submitButton.setVisible(false);

        answerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        questionTextArea.setLineWrap(true); 
        questionTextArea.setWrapStyleWord(true);

        SP = new JScrollPane(questionTextArea);
        questionTextArea.setBorder(new LineBorder(Color.decode("#4C8CD6"), 3));

        mainPanel.add(SP, BorderLayout.NORTH);
        Font boldFont = new Font("SansSerif", Font.BOLD, 16); 
        questionTextArea.setFont(boldFont);
        questionTextArea.setEditable(false);

        
        questionTextArea.setFocusable(false);

        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(answerPanel, BorderLayout.SOUTH);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        JPanel timerPanel = new JPanel();
        timerPanel.add(timerLabel);
        containerPanel.add(timerPanel, BorderLayout.NORTH);
        containerPanel.add(navigationPanel, BorderLayout.SOUTH);
        totalPanel.add(containerPanel, BorderLayout.CENTER);

        totalPanel.add(infoPanel, BorderLayout.SOUTH);

        reviewPanel = new QuestionReviewPanel();
        reviewButton = new JButton("Review");
        reviewButton.addActionListener(e -> showReviewPage());
        navigationPanel.add(reviewButton);
        reviewButton.setVisible(false);

        add(totalPanel);
        updateQuestionDisplay();
        setVisible(true);

        // design
        infoPanel.setBackground(Color.decode("#4C8CD6"));
        userInfoLabel.setForeground(Color.white);
        timerPanel.setBackground(Color.decode("#4C8CD6"));
        timerLabel.setForeground(Color.white);
        mainPanel.setBackground(Color.decode("#BBD8FA"));
        answerPanel.setBackground(Color.decode("#BBD8FA"));
        navigationPanel.setBackground(Color.decode("#BBD8FA"));
        reviewButton.setBackground(Color.decode("#BBD8FA"));

        SP.setBackground(Color.decode("#BBD8FA"));
        questionTextArea.setBackground(Color.decode("#BBD8FA"));

        reviewButton.setBorderPainted(false);
        previousButton.setBackground(Color.decode("#BBD8FA"));

        previousButton.setBorderPainted(false);
        nextButton.setBackground(Color.decode("#BBD8FA"));

        nextButton.setBorderPainted(false);
        submitButton.setBackground(Color.decode("#BBD8FA"));

        submitButton.setBorderPainted(false);
        BackToQuestionButton.setBackground(Color.decode("#BBD8FA"));

        BackToQuestionButton.setBorderPainted(false);
        answerPanel.setBackground(Color.decode("#BBD8FA"));

        answerButtonA.setBorderPainted(false);
        answerButtonB.setBorderPainted(false);
        answerButtonC.setBorderPainted(false);
        answerButtonD.setBorderPainted(false);
        answerButtonA.setBackground(Color.white);
        answerButtonB.setBackground(Color.white);
        answerButtonC.setBackground(Color.white);
        answerButtonD.setBackground(Color.white);

    }

    private void backToPreviousQuestion() {
        // Reset the currentQuestionIndex to 0 to go back to the first question
        currentQuestionIndex = 0;
        mainPanel.removeAll();
        mainPanel.add(SP, BorderLayout.NORTH);

        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(answerPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();

        updateQuestionDisplay();

        // Hide the BackToQuestionButton and SubmitButton and show the NextButton and
        // ReviewButton
        BackToQuestionButton.setVisible(false);
        submitButton.setVisible(false);
        nextButton.setVisible(true);
        reviewButton.setVisible(false);
    }

    private void submit() {
        List<Boolean> gradingStatus = List.of(true, true, false);
        JFrame submitPage = new SubmitPage(userKey, questionList, userAnswersList, tasks);
        this.dispose();
        submitPage.setVisible(true);

    }

    private void updateQuestionDisplay() {
        // Check if the currentQuestionIndex is within the bounds of the questionList
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questionList.size()) {
            Question question = questionList.get(currentQuestionIndex);
            String previousAnswer = "";
            previousAnswer = userAnswersList.get(currentQuestionIndex);
            questionTextArea.setText("Q " + (currentQuestionIndex + 1) + " :" + question.getQ_question());

            // Check the question type (e for essay, s for single-choice)
            if ("e".equals(question.getQ_type())) {
                // Show the text area for essay type
                answerPanel.removeAll();
                answerPanel.add(answerScrollPane);
                answerTextArea.setText("");

                answerTextArea.append(userAnswersList.get(currentQuestionIndex));

            } else if ("w".equals(question.getQ_type())) {
                answerPanel.removeAll();
                answerPanel.add(answerScrollPane);
                answerTextArea.setText("");

                answerTextArea.append(userAnswersList.get(currentQuestionIndex));

            } else if ("s".equals(question.getQ_type())) {
                // Hide the text area and show the radio buttons for single-choice type
                answerPanel.removeAll();
                answerPanel.add(answerButtonPane);
                switch (previousAnswer) {
                    case "A":
                        answerButtonA.setText("\u2714\uFE0F A");
                        answerButtonB.setText("\u274E B");
                        answerButtonC.setText("\u274E C");
                        answerButtonD.setText("\u274E D");
                        break;
                    case "B":
                        answerButtonA.setText("\u274E A");
                        answerButtonB.setText("\u2714\uFE0F B");
                        answerButtonC.setText("\u274E C");
                        answerButtonD.setText("\u274E D");
                        break;
                    case "C":
                        answerButtonA.setText("\u274E A");
                        answerButtonB.setText("\u274E B");
                        answerButtonC.setText("\u2714\uFE0F C");
                        answerButtonD.setText("\u274E D");
                        break;
                    case "D":
                        answerButtonA.setText("\u274E A");
                        answerButtonB.setText("\u274E B");
                        answerButtonC.setText("\u274E C");
                        answerButtonD.setText("\u2714\uFE0F D");
                        break;
                    default:
                        // If the user hasn't answered this question yet, clear the selections
                        answerButtonA.setText("\u274E A");
                        answerButtonB.setText("\u274E B");
                        answerButtonC.setText("\u274E C");
                        answerButtonD.setText("\u274E D");
                        break;
                }

            }

            // Convert byte[] to ImageIcon and set it to the image label
            byte[] imageData = question.getQ_image();
            ImageIcon imageIcon = new ImageIcon(imageData);

            // Get the original image dimensions
            int originalWidth = imageIcon.getIconWidth();
            int originalHeight = imageIcon.getIconHeight();

            // Calculate the new width and height while maintaining the aspect ratio
            int maxWidth = 1000;
            int maxHeight = 450;
            int newWidth, newHeight;

            if (originalWidth > maxWidth || originalHeight > maxHeight) {
                double widthRatio = (double) maxWidth / originalWidth;
                double heightRatio = (double) maxHeight / originalHeight;
                double scaleFactor = Math.min(widthRatio, heightRatio);

                newWidth = (int) (originalWidth * scaleFactor);
                newHeight = (int) (originalHeight * scaleFactor);
            } else {
                // If the image is smaller than the maximum dimensions, use the original
                // dimensions
                newWidth = originalWidth;
                newHeight = originalHeight;
            }

            // Scale the image
            Image scaledImage = imageIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
            imageLabel.setIcon(scaledImageIcon);

            // Clear the answer for each new question

            if (currentQuestionIndex == 0) {
                previousButton.setVisible(false);
            } else {
                previousButton.setVisible(true);
            }
            if (currentQuestionIndex == questionList.size() - 1) {
                nextButton.setVisible(false);
                reviewButton.setVisible(true);
            } else {
                nextButton.setVisible(true);
                reviewButton.setVisible(false);
            }

            revalidate();
        }
    }

    private void showReviewPage() {
        updateUserAnswerList();

        mainPanel.removeAll();
        mainPanel.add(reviewPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        reviewButton.setVisible(false);
        previousButton.setVisible(false);
        submitButton.setVisible(true);
        BackToQuestionButton.setVisible(true);
        // Update the review panel with the user's answers
        reviewPanel.updateReviewText(questionList, userAnswersList);
    }

    private void updateUserAnswerList() {
        // Check if the currentQuestionIndex is within the bounds of the questionList
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questionList.size()) {
            Question question = questionList.get(currentQuestionIndex);

            // Check the question type (e for essay, s for single-choice)
            if ("e".equals(question.getQ_type())) {
                // For essay type, update the userAnswersList with the text from the JTextArea
                userAnswersList.set(currentQuestionIndex, answerTextArea.getText());

            } else if ("w".equals(question.getQ_type())) {
                // For essay type, update the userAnswersList with the text from the JTextArea
                userAnswersList.set(currentQuestionIndex, answerTextArea.getText());
            } else if ("s".equals(question.getQ_type())) {
                // For single-choice type, check which button is selected and update the
                // userAnswersList accordingly
                if (answerButtonA.getText().startsWith("\u2714")) {
                    userAnswersList.set(currentQuestionIndex, "A");
                } else if (answerButtonB.getText().startsWith("\u2714")) {
                    userAnswersList.set(currentQuestionIndex, "B");
                } else if (answerButtonC.getText().startsWith("\u2714")) {
                    userAnswersList.set(currentQuestionIndex, "C");
                } else if (answerButtonD.getText().startsWith("\u2714")) {
                    userAnswersList.set(currentQuestionIndex, "D");
                }
            }

        }
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

    private List<Question> getQuestionInfo(int userKey, int examId) throws SQLException {
        List<Question> questionList = new ArrayList<>();

        Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@");

        String query = "SELECT * FROM q_set WHERE Q_examSet_id = ?";
        PreparedStatement statement = con.prepareStatement(query);

        statement.setInt(1, examId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int idQ_sets = resultSet.getInt("idQ_set");
            String Q_questions = resultSet.getString("Q_question");
            int Q_examSet_ids = resultSet.getInt("Q_examSet_id");
            String Q_diffs = resultSet.getString("Q_diff");
            byte[] Q_images = resultSet.getBytes("Q_image");
            String Q_answers = resultSet.getString("Q_answer");

            String Q_type = resultSet.getString("Q_type");
            String Q_points = resultSet.getString("Q_points");
            String Q_topic = resultSet.getString("Q_topic");

            Question question = new Question(idQ_sets, Q_questions, Q_examSet_ids, Q_diffs, Q_images, Q_type, Q_answers,
                    Q_points, Q_topic);
            questionList.add(question);
        }

        return questionList;
    }

}