package question;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class QuestionReviewPanel extends JPanel {
    private JTextArea reviewTextArea;

    public QuestionReviewPanel() {
        setLayout(new BorderLayout());
        reviewTextArea = new JTextArea(10, 60);
        JScrollPane scrollPane = new JScrollPane(reviewTextArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateReviewText(java.util.List<Question> questionList, java.util.List<String> userAnswersList) {
        StringBuilder reviewText = new StringBuilder("Review your answers:\n");
        for (int i = 0; i < questionList.size(); i++) {
            Question question = questionList.get(i);
            String userAnswer = userAnswersList.get(i);
            reviewText.append("Q ").append(i + 1).append(": ").append(userAnswer).append("\n");
        }
        reviewTextArea.setText(reviewText.toString());
    }

    public void updateReviewTextAsString(String String) {

        reviewTextArea.setText(String);
    }

}