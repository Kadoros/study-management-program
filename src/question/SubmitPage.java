package question;

import javax.swing.*;

import TaskPage.Tasks;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SubmitPage extends JFrame {
    private List<Boolean> gradingStatus;
    private List<Integer> resultList;
    private static JPanel statusPanel;
    private static int j = 0;
    private Timer timer;
    private Tasks Tasks;
    private int totalAwardedMark = 0;
    private int totalMaxMark = 0;
    private List<String> commentaryList;
    private int UserKey;
    private ArrayList<Float> userTopicGradesList;
    float flags = 0;

    public SubmitPage(int userkey, List<Question> questionList, List<String> userAnswerList, Tasks Task) {
        this.Tasks = Task;
        this.UserKey = userkey;
        setTitle("Submit Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel successLabel = new JLabel("Don't close this window");
        successLabel.setFont(new Font("Arial", Font.BOLD, 20));
        successLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(successLabel, BorderLayout.NORTH);

        // Initialize the gradingStatus and resultList lists
        gradingStatus = new ArrayList<>(questionList.size());
        resultList = new ArrayList<>(questionList.size());
        commentaryList = new ArrayList<>(questionList.size());

        for (int i = 0; i < questionList.size(); i++) {

            // For any other question type, set the gradingStatus to false
            gradingStatus.add(false);
            resultList.add(0); // Initialize all marks to zero
            commentaryList.add("");

        }

        statusPanel = new JPanel(new GridLayout(gradingStatus.size(), 1));
        for (int i = 0; i < gradingStatus.size(); i++) {
            String statusText = "Q" + (i + 1) + ": 🔄"; // Default statusText for each question
            JLabel statusLabel = new JLabel(statusText);
            statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
            statusPanel.add(statusLabel);
        }

        add(statusPanel, BorderLayout.CENTER);

        JLabel closeLabel = new JLabel("grading");
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        closeLabel.setForeground(Color.RED); // Set the foreground color to red
        add(closeLabel, BorderLayout.SOUTH);

        // Calculate the preferred height based on the number of items in the
        // gradingStatus list
        int preferredHeight = Math.min(400, gradingStatus.size() * 50 + 100);
        setSize(400, preferredHeight);

        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        timer = new Timer(1000, e -> {
            if (areAllResultsTrue() != true) {
                UpdateStatus(userAnswerList);
                j++;
                checkUserAnswers(questionList, userAnswerList);
            } else {
                UpdateTaskGradeCode(Task, gradeCodeSetter(questionList, userAnswerList));
                if (Task.getIdTaskSQL() <= 0) {
                    System.out.println("pass");
                } else {
                    updateGrade(adjustGrade(questionList, userAnswerList, setUserTopicGradesList(userkey)), userkey);
                }

                JOptionPane.showMessageDialog(this,
                        "Questions have been successfully marked. You can now close the window..");
                timer.stop();
                successLabel.setText("Close this window");
                closeLabel.setText("graded");
                closeLabel.setForeground(Color.GREEN);

            }
        });
        timer.start();
        UpdateStatus(userAnswerList);

        System.out.println(resultList);
        System.out.println(gradingStatus);
    }

    private void checkUserAnswers(List<Question> questionList, List<String> userAnswerList) {

        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            String userAnswer = userAnswerList.get(i);

            // Check if the question is already graded (true or false) in the resultList
            // If the question is not graded (false), then evaluate the answer
            if (!gradingStatus.get(i)) {
                int awardedMark = 0;
                String commentary = "";

                if ("e".equals(question.getQ_type())) {
                    // For essay questions, call the method to evaluate the answer
                    Grader Grader = new Grader(question, userAnswer);
                    awardedMark = Grader.awardMark();
                    gradingStatus.set(i, true);
                    commentary = Grader.justification();

                } else if ("s".equals(question.getQ_type())) {
                    // For single-choice questions, check if the user's answer is correct
                    awardedMark = userAnswer.equals(question.getQ_answer()) ? question.getQ_points() : 0;
                    gradingStatus.set(i, true);
                    commentary = "Multiple choice question type does not support AI commentary ";
                } else if ("w".equals(question.getQ_type())) {
                    // For single-choice questions, check if the user's answer is correct
                    awardedMark = userAnswer.equals(question.getQ_answer()) ? question.getQ_points() : 0;
                    gradingStatus.set(i, true);
                    commentary = "Simple subjective question type does not support AI commentary";
                } else {
                    // For any other question type, set the awarded mark to zero
                    awardedMark = 0;
                    gradingStatus.set(i, true);
                }

                // Update the gradingStatus and resultList based on the evaluated result

                resultList.set(i, awardedMark);
                commentaryList.set(i, commentary);
                UpdateStatus(userAnswerList);

                // Update the statusText in the statusPanel with the evaluated result

            }
        }
        System.out.println(resultList);
        System.out.println(gradingStatus);
        System.out.println(commentaryList);

    }

    private void UpdateStatus(List<String> userAnswerList) {
        for (int i = 0; i < userAnswerList.size(); i++) {
            ((JLabel) statusPanel.getComponent(i))
                    .setText("Q" + (i + 1) + ": " + (gradingStatus.get(i) ? "✔️" : "🔄"));

        }
    }

    private boolean areAllResultsTrue() {
        for (boolean result : gradingStatus) {
            if (!result) {
                return false; // If any element is false, return false immediately
            }
        }
        return true;
    }

    private String gradeCodeSetter(List<Question> questionList, List<String> userAnswerList) {
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            String userAnswer = userAnswerList.get(i);
            int awardedMark = resultList.get(i);
            int maxMark = question.getQ_points();
            String difficulty = question.getQ_diff();
            String commentary = commentaryList.get(i);

            // Append data for each question
            sb.append(question.getIdQ_set()).append("&")
                    .append(userAnswer).append("&")
                    .append(question.getQ_answer()).append("&")
                    .append(awardedMark).append("&")
                    .append(maxMark).append("&")
                    .append(difficulty).append("&")
                    .append(commentary).append("|");

            totalAwardedMark += awardedMark;
            totalMaxMark += maxMark;
        }

        // Append the total awarded mark and total available mark

        // Output the formatted string
        System.out.println(sb.toString());
        return sb.toString();
    }

    public void UpdateTaskGradeCode(Tasks Tasks, String newGradeCode) {
        int idtaskSQL = Tasks.getIdTaskSQL();
        if (idtaskSQL != -30) {
            int percentage = (totalAwardedMark * 100) / totalMaxMark;

            try (Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@")) {

                // Prepare the SQL update statement with a WHERE clause for idtaskSQL
                String sql = "UPDATE task SET Task_gradecode = ?, Task_percent = ? , Task_finish = ? , Task_finishTime = ? WHERE idtaskSQL = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate currentDate = LocalDate.now();
                String formattedDate = currentDate.format(dateFormatter);

                // Set the parameters for the prepared statement
                pstmt.setString(1, newGradeCode);
                pstmt.setString(2, String.valueOf(percentage));
                pstmt.setString(4, formattedDate);
                pstmt.setString(3, "1");
                pstmt.setInt(5, idtaskSQL);

                // Execute the update
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Task_gradecode updated successfully for idtaskSQL: " + idtaskSQL);
                } else {
                    System.out.println("No rows found with idtaskSQL: " + idtaskSQL);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            this.Tasks.setTaskGradecode(newGradeCode);
            revisionPage revisionPage = new revisionPage(UserKey, Tasks);
            revisionPage.setVisible(true);
        }

    }

    public List<Float> setUserTopicGradesList(int userKey) {
        userTopicGradesList = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@")) {

            String query = "SELECT topic1, topic2, topic3, topic4, topic5, topic6 FROM grade WHERE userKey = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);

            // Set the userKey parameter in the query

            preparedStatement.setInt(1, userKey);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Retrieve topic grades from the resultSet
                for (int i = 1; i <= 6; i++) { // Assuming there are 6 topics (topic1 to topic6)
                    float topicGrade = resultSet.getFloat("topic" + i);
                    userTopicGradesList.add(topicGrade);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
        System.out.println(userTopicGradesList);
        return userTopicGradesList;
    }

    public List<Integer> adjustGrade(List<Question> questionList, List<String> userAnswerList,
            List<Float> userTopicGrades) {
        List<Question> topic1List = new ArrayList<>();
        List<Question> topic2List = new ArrayList<>();
        List<Question> topic3List = new ArrayList<>();
        List<Question> topic4List = new ArrayList<>();
        List<Question> topic5List = new ArrayList<>();
        List<Question> topic6List = new ArrayList<>();

        float Userstopic1Grade = userTopicGrades.get(0) / 10;
        float Userstopic2Grade = userTopicGrades.get(1) / 10;
        float Userstopic3Grade = userTopicGrades.get(2) / 10;
        float Userstopic4Grade = userTopicGrades.get(3) / 10;
        float Userstopic5Grade = userTopicGrades.get(4) / 10;
        float Userstopic6Grade = userTopicGrades.get(5) / 10;

        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            String topic = question.getIdQ_topic();

            // Add the question to the appropriate topic list
            switch (topic) {
                case "1":
                    topic1List.add(question);
                    break;
                case "2":
                    topic2List.add(question);
                    break;
                case "3":
                    topic3List.add(question);
                    break;
                case "4":
                    topic4List.add(question);
                    break;
                case "5":
                    topic5List.add(question);
                    break;
                case "6":
                    topic6List.add(question);
                    break;
                default:
                    System.out.println("tf");
                    break;
            }
        }
        System.out.println(topic1List);
        System.out.println(topic2List);
        System.out.println(topic3List);
        System.out.println(topic4List);
        System.out.println(topic5List);
        System.out.println(topic6List);

        // Calculate and update the user's topic grades
        Userstopic1Grade = calculateTopicGrade(topic1List, Userstopic1Grade);
        Userstopic2Grade = calculateTopicGrade(topic2List, Userstopic2Grade);
        Userstopic3Grade = calculateTopicGrade(topic3List, Userstopic3Grade);
        Userstopic4Grade = calculateTopicGrade(topic4List, Userstopic4Grade);
        Userstopic5Grade = calculateTopicGrade(topic5List, Userstopic5Grade);
        Userstopic6Grade = calculateTopicGrade(topic6List, Userstopic6Grade);

        // Update the userTopicGrades list
        List<Integer> userTopicGradesIntList = new ArrayList<Integer>(5);
        userTopicGradesIntList.add((int) Userstopic1Grade);
        userTopicGradesIntList.add((int) Userstopic2Grade);
        userTopicGradesIntList.add((int) Userstopic3Grade);
        userTopicGradesIntList.add((int) Userstopic4Grade);
        userTopicGradesIntList.add((int) Userstopic5Grade);
        userTopicGradesIntList.add((int) Userstopic6Grade);

        return userTopicGradesIntList;
    }

    public void updateGrade(List<Integer> currentGradeList, int userKey) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA", "kado","1130Coolhan@")) {

            String updateQuery = "UPDATE grade SET topic1 = ?, topic2 = ?, topic3 = ?, topic4 = ?, topic5 = ?, topic6 = ? WHERE userKey = ?";
            PreparedStatement preparedStatement = con.prepareStatement(updateQuery);

            // Assuming currentGradeList contains 6 grades corresponding to topic1 to topic6
            for (int i = 0; i < 6; i++) {
                preparedStatement.setInt(i + 1, currentGradeList.get(i)); // PreparedStatement uses 1-based index
            }

            // Set the userKey parameter in the query

            preparedStatement.setInt(7, userKey);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Grades updated successfully.");
            } else {
                System.out.println("No rows updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }

    private int calculateTopicGrade(List<Question> topicList, float currentGrade) {
        // Calculate the total awarded marks for the topic
        System.out.println(topicList);
        int flag = 0;
        float adjustedGrade = 0;
        float totalAdjustedDifficulty = 0;
        float diffgrade = 0;
        float adjustPlusGrade = 0;

        for (Question question : topicList) {
            float AwardedMarks = (float) resultList.get(flag);
            System.out.println(AwardedMarks);
            float MaxMarks = (float) question.getQ_points();
            System.out.println(MaxMarks);
            float difficulty = (float) Integer.valueOf(question.getQ_diff());
            System.out.println(difficulty);
            float MarkRatio = AwardedMarks / MaxMarks;
            System.out.println(MarkRatio);
            float AdjustedDifficulty = difficulty * MarkRatio;
            System.out.println(AdjustedDifficulty);
            totalAdjustedDifficulty += AdjustedDifficulty;
            System.out.println(totalAdjustedDifficulty);
            flags++;
        }
        diffgrade = totalAdjustedDifficulty / flags - currentGrade;
        System.out.println(totalAdjustedDifficulty);
        System.out.println(diffgrade);

        if (diffgrade >= 0) {
            float base = (diffgrade / 3);
            adjustPlusGrade = PowerFunction(base, 2);
            System.out.println("adjustPlusGrade" + adjustPlusGrade);
        } else {
            float base = (-1 * diffgrade) / 6;
            System.out.println("base:" + base);
            adjustPlusGrade = -1 * PowerFunction(base, 2);
            System.out.println("adjustPlusGrade" + adjustPlusGrade);

        }

        if (currentGrade == 0) {
            adjustedGrade = currentGrade + adjustPlusGrade + 2.5f;
        } else {
            adjustedGrade = currentGrade + adjustPlusGrade;
        }
        System.out.println(adjustedGrade);
        return (int) Math.ceil(adjustedGrade * 10);
    }

    public float PowerFunction(float base, int power) {

        if (power == 1) {
            return base;
        } else if (power > 0) {
            return base * PowerFunction(base, power - 1);
        } else if (power < 0) {
            return 1 / PowerFunction(base, -power);
        } else {
            return 1.0f;
        }
    }
}
