package analysis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grade.Grade;
import mainPage.ExamSet;
import question.Question;

public class recosys {
    private Grade userGrade;
    private ExamSet[] examSetList;
    private static float[][] topicList;
    private static Object[][] examSetData;

    public recosys(Grade userGrade, ExamSet[] examSetList) {
        this.userGrade = userGrade;
        this.examSetList = examSetList;

        float[] topics = {
                (float) userGrade.getTopic1Grade(),
                (float) userGrade.getTopic2Grade(),
                (float) userGrade.getTopic3Grade(),
                (float) userGrade.getTopic4Grade(),
                (float) userGrade.getTopic5Grade(),
                (float) userGrade.getTopic6Grade()
        };
        topicList = new float[topics.length][2];

        for (int i = 0; i < topics.length; i++) {
            float[] topicInfo = { i + 1, topics[i] }; // Save topic numbers and ratings as an array
            topicList[i] = topicInfo;
        }

        examSetData = new Object[examSetList.length][10];
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://13.50.31.134/IB_CS_IA",
                    "kado", "1130Coolhan@");
            String query = "SELECT * FROM q_set WHERE Q_examSet_id = ?";
            int flag_examSet = 0;
            for (ExamSet examSet : examSetList) {

                PreparedStatement statement = con.prepareStatement(query);
                int examId = examSet.getIdexam_set();
                statement.setInt(1, examId);
                ResultSet resultSet = statement.executeQuery();
                int exam_QNum = Integer.valueOf(examSet.getExam_QNum());
                Object[] setInfo = new Object[exam_QNum + 1];
                setInfo[0] = examSet;
                int flag_setInfo = 1;

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

                    Question question = new Question(idQ_sets, Q_questions, Q_examSet_ids,
                            Q_diffs, Q_images, Q_type,
                            Q_answers,
                            Q_points, Q_topic);

                    // Now set the value of x in the setInfo array

                    setInfo[flag_setInfo] = question; // -1 because arrays are 0-based
                    flag_setInfo++;

                    

                }
                examSetData[flag_examSet] = setInfo;
                flag_examSet++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("topicList: " + Arrays.deepToString(topicList));
        System.out.println("examSetData: " + Arrays.deepToString(examSetData));

    }

    private static double calculateDistance(float[][] topicList, Object[] setInfo) {
        // Calculate ExamSetInfo and topicListInfo
        float[] ExamSetInfo = new float[6];
        int[] topicQuestionCount = new int[6]; // To keep track of the number of questions for each topic

        for (int i = 1; i < setInfo.length; i++) {
            Question question = (Question) setInfo[i];
            int topic = Integer.parseInt(question.getIdQ_topic());
            float difficulty = 10 * Float.parseFloat(question.getQ_diff());

            ExamSetInfo[topic - 1] += difficulty; // -1 to convert topic number to 0-based index
            topicQuestionCount[topic - 1]++;
        }

        // Calculate average difficulty for each topic in topicList
        float[] topicListInfo = new float[6];

        for (float[] topicInfo : topicList) {
            int topic = (int) topicInfo[0];
            float difficulty = topicInfo[1];

            topicListInfo[topic - 1] = difficulty; // -1 to convert topic number to 0-based index
        }

        // Calculate average difficulty for each topic in ExamSetInfo
        for (int i = 0; i < ExamSetInfo.length; i++) {
            if (topicQuestionCount[i] > 0) {
                ExamSetInfo[i] /= topicQuestionCount[i];
            }
        }

        // Calculate Euclidean distance
        double distance = 0.0;
        for (int i = 0; i < ExamSetInfo.length; i++) {
            distance += Math.pow(ExamSetInfo[i] - topicListInfo[i], 2);
        }
        distance = Math.sqrt(distance);

        // Print the calculated arrays
        System.out.println("ExamSetInfo: " + Arrays.toString(ExamSetInfo));
        System.out.println("topicListInfo: " + Arrays.toString(topicListInfo));
        System.out.println(distance);

        return distance;
    }

    public List<Integer> recommendExamSet(int k) {
        Map<Integer, Double> distances = new HashMap<>();

        // Iterate through each setInfo in examSetData and calculate distances
        for (Object[] setInfo : examSetData) {
            ExamSet examSet = (ExamSet) setInfo[0];
            int examSetID = examSet.getIdexam_set();

            double distance = calculateDistance(topicList, setInfo);
            distances.put(examSetID, distance);
        }

        // Sort distances and find k-nearest neighbors
        List<Integer> recommendedExamSetIDs = new ArrayList<>();
        distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(k)
                .forEach(entry -> recommendedExamSetIDs.add(entry.getKey()));
        System.out.println(recommendedExamSetIDs);

        return recommendedExamSetIDs;
    }

    public static void main(String[] args) {
        Grade grade = new Grade(-1, 50, 10, 60, 20, 30, 70); // Initialize with appropriate values
        ExamSet[] examSetList = new ExamSet[3]; // Initialize with actual exam sets
        examSetList[0] = (new ExamSet(4, "test", "1", "1", "2"));
        examSetList[1] = (new ExamSet(3, "test", "3", "4", "1"));
        examSetList[2] = (new ExamSet(1, "test", "18", "16", "4"));
        // Create an instance of recosys
        recosys recommender = new recosys(grade, examSetList);
        recommender.recommendExamSet(1);

    }
}
