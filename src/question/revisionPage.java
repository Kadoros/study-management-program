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
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import TaskPage.Tasks;
import sql.sql;

public class revisionPage extends JFrame {
    public static int userKey;
    private JFrame frame;
    private JLabel timerLabel;
    private JTextArea questionTextArea;
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
    private JTextArea userAnswerTextArea;
    private JTextArea correctAnswerTextArea;
    private JLabel answerLabelA;
    private JLabel answerLabelB;
    private JLabel answerLabelC;
    private JLabel answerLabelD;
    private JPanel answerPanel;
    private Timer timer;
    private int timeRemaining;
    private JButton submitButton;
    private JButton BackToQuestionButton;

    private JPanel submitPanel;
    private JLabel submitStatusLabel;
    private JLabel closeWindowLabel;

    private JButton reviewButton;

    private List<Question> questionList = new ArrayList<>();
    private List<String> userAnswersList = new ArrayList<>();

    private static QuestionReviewPanel reviewPanel;

    private static Tasks tasks;

    private List<String> userAnswerList = new ArrayList<>();
    private List<String> correctAnswerList = new ArrayList<>();
    private List<String> commentaryList = new ArrayList<>();
    private List<Integer> maxMarklist = new ArrayList<>();
    private List<Integer> awardMarklist = new ArrayList<>();
    private List<Integer> difficultylist = new ArrayList<>();
    // Replace these with your actual question data
    private String[] questions = {
            "Question 1: What is your favorite color?",
            "Question 2: What is your favorite animal?",
            "Question 3: What is your favorite food?"
    };
    private JLabel userAnswerLabel;
    private JLabel correctAnswerLabel;
    private JTextArea commentaryTextArea;
    private JScrollPane commentaryScrollPane;
    private int totalAwardedMark;
    private int totalMaxMark;
    private JScrollPane userAnswerScrollPane;
    private JScrollPane correctAnswerScrollPane;

    private JLabel MarkJLabel;
    private JLabel difficulyLabel;
    private JLabel totalMarkLabel;
    private JScrollPane SP;

    // Replace these with your actual image paths or URLs

    public revisionPage(int userKey, Tasks Task) {
        this.userKey = userKey;
        this.ExamID = Task.getTaskInIdExam();
        this.timeRemaining = Integer.parseInt(Task.getTaskTime()) * 60;
        this.tasks = Task;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try {
            questionList = getQuestionInfo(userKey, ExamID); // Retrieve the list of Question objects

            for (int i = 0; i < questionList.size(); i++) {
                userAnswersList.add("");
                correctAnswerList.add("");
                commentaryList.add("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception if needed
        }
        parseGradeCode(Task.getTaskGradecode());
        createAndShowGUI();
        getInfo();
        setLocationRelativeTo(null);

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
                timerLabel.setText("");

                // Check if the time has run out
                if (timeRemaining <= 0) {
                    timer.stop();

                }
            }
        });
        timer.start();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        timerLabel = new JLabel();
        questionTextArea = new JTextArea();
        questionTextArea.setLineWrap(true); 
        questionTextArea.setWrapStyleWord(true);
        imageLabel = new JLabel();

        userAnswerLabel = new JLabel("Your Answer:");
        userAnswerTextArea = new JTextArea(2, 60);
        userAnswerTextArea.setLineWrap(true); 
        userAnswerTextArea.setWrapStyleWord(true);
        userAnswerScrollPane = new JScrollPane(userAnswerTextArea);

        userAnswerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        correctAnswerLabel = new JLabel("Correct Answer:");
        correctAnswerTextArea = new JTextArea(2, 60);
        correctAnswerTextArea.setLineWrap(true); 
        correctAnswerTextArea.setWrapStyleWord(true);
        correctAnswerScrollPane = new JScrollPane(correctAnswerTextArea);

        correctAnswerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        commentaryTextArea = new JTextArea(2, 60);
        commentaryTextArea.setLineWrap(true); 
        commentaryTextArea.setWrapStyleWord(true);

        commentaryScrollPane = new JScrollPane(commentaryTextArea);
        commentaryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the answer panel
        answerPanel = new JPanel();

        MarkJLabel = new JLabel();
        difficulyLabel = new JLabel();// for showing marks of each questions
        totalMarkLabel = new JLabel();
        answerPanel.setLayout(new GridLayout(5, 2));
        answerPanel.add(userAnswerLabel);
        answerPanel.add(userAnswerScrollPane);

        answerPanel.add(correctAnswerLabel);
        answerPanel.add(correctAnswerScrollPane);
        answerPanel.add(new JLabel("Commentary:"));

        answerPanel.add(commentaryScrollPane);

        answerPanel.add(MarkJLabel);
        answerPanel.add(difficulyLabel);
        answerPanel.add(totalMarkLabel);
        totalMarkLabel.setText("total Mark: " + String.valueOf(totalAwardedMark) + "/"
                + String.valueOf(totalMaxMark) + "   "
                + String.valueOf((float) totalAwardedMark / (float) totalMaxMark * 100) + "%");

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

                currentQuestionIndex--;
                System.out.println(userAnswersList);
                // answerTextArea.removeAll();
                updateQuestionDisplay();
            }
        });

        nextButton.addActionListener(e -> {
            if (currentQuestionIndex < questionList.size() - 1) {

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

        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(answerPanel, BorderLayout.SOUTH);

        questionTextArea.setLineWrap(true); 
        questionTextArea.setWrapStyleWord(true);

        SP = new JScrollPane(questionTextArea);
        questionTextArea.setBorder(new LineBorder(Color.decode("#4C8CD6"), 3));

        mainPanel.add(SP, BorderLayout.NORTH);
        Font boldFont = new Font("SansSerif", Font.BOLD, 16); 
        questionTextArea.setFont(boldFont);
        questionTextArea.setEditable(false);

        
        questionTextArea.setFocusable(false);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        containerPanel.add(timerLabel, BorderLayout.NORTH);
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

        infoPanel.setBackground(Color.decode("#4C8CD6"));
        userInfoLabel.setForeground(Color.white);

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

            questionTextArea.setText("Q " + (currentQuestionIndex + 1) + " :" + question.getQ_question());

            // Update the user answer label
            System.err.println("debug");
            System.out.println(userAnswerList);
            System.out.println(userAnswerList.get(currentQuestionIndex));
            System.err.println("debug");

            userAnswerTextArea.setText(userAnswerList.get(currentQuestionIndex));

            // Update the correct answer label
            correctAnswerTextArea.setText(question.getQ_answer());

            // Update the commentary text area
            commentaryTextArea.setText(commentaryList.get(currentQuestionIndex));

            MarkJLabel.setText("Mark: " + String.valueOf(awardMarklist.get(currentQuestionIndex)) + "/"
                    + String.valueOf(maxMarklist.get(currentQuestionIndex)) + "  "
                    + String.valueOf(
                            (float) awardMarklist.get(currentQuestionIndex)
                                    / (float) maxMarklist.get(currentQuestionIndex) * 100)
                    + "%");

            difficulyLabel.setText("Difficulty: " + String.valueOf(difficultylist.get(currentQuestionIndex)));

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

    private void parseGradeCode(String gradeCode) {
        // Initialize the lists
        userAnswerList.clear();
        correctAnswerList.clear();
        commentaryList.clear();

        // Split the gradeCode using the separator character "|"
        String[] parts = gradeCode.split("\\|");
        System.out.println(gradeCode);

        // Loop through the parts and extract the required information
        for (int i = 1; i < parts.length; i++) {
            String[] data = parts[i].split("&");
            String userAnswer = data[1];
            String correctAnswer = data[3];
            int awardedMark = Integer.parseInt(data[3]);
            int maxMark = Integer.parseInt(data[4]);
            int difficulty = Integer.parseInt(data[5]);
            String commentary = data[6];

            // Add the extracted information to the respective lists
            userAnswerList.add(userAnswer);
            correctAnswerList.add(correctAnswer);
            commentaryList.add(commentary);
            awardMarklist.add(awardedMark);
            maxMarklist.add(maxMark);
            difficultylist.add(difficulty);

            totalAwardedMark += awardedMark;
            totalMaxMark += maxMark;
        }

        System.err.println(userAnswerList);
        System.err.println(correctAnswerList);
        System.err.println(commentaryList);

    }

    private void showReviewPage() {

        mainPanel.removeAll();
        mainPanel.add(reviewPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        reviewButton.setVisible(false);
        previousButton.setVisible(false);
        submitButton.setVisible(false);
        BackToQuestionButton.setVisible(true);
        // Update the review panel with the user's answers
        reviewPanel.updateReviewTextAsString("you can close the window");
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