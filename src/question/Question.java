package question;

public class Question {
    private int idQ_set;
    private String Q_question;
    private int Q_examSet_id;
    private String Q_diff;
    private byte[] Q_image;
    private String Q_answer;
    private int Q_points;
    private String Q_type;
    private String Q_topic;

    public Question(int idQ_sets, String Q_questions, int Q_examSet_ids, String Q_diffs, byte[] Q_images,
            String Q_type,
            String Q_answers, String Q_points, String Q_topic) {
        this.idQ_set = idQ_sets;
        this.Q_question = Q_questions;
        this.Q_examSet_id = Q_examSet_ids;
        this.Q_diff = Q_diffs;
        this.Q_image = Q_images;
        this.Q_answer = Q_answers;
        this.Q_points = Integer.parseInt(Q_points);
        this.Q_type = Q_type;
        this.Q_topic = Q_topic;
    }

    public int getIdQ_set() {
        return idQ_set;
    }

    public void setIdQ_set(int idQ_set) {
        this.idQ_set = idQ_set;
    }

    public String getQ_question() {
        return Q_question;
    }

    public void setQ_question(String Q_question) {
        this.Q_question = Q_question;
    }

    public int getQ_examSet_id() {
        return Q_examSet_id;
    }

    public void setQ_examSet_id(int Q_examSet_id) {
        this.Q_examSet_id = Q_examSet_id;
    }

    public String getQ_diff() {
        return Q_diff;
    }

    public void setQ_diff(String Q_diff) {
        this.Q_diff = Q_diff;
    }

    public byte[] getQ_image() {
        return Q_image;
    }

    public void setQ_image(byte[] Q_image) {
        this.Q_image = Q_image;
    }

    public String getQ_answer() {
        return Q_answer;
    }

    public void setQ_answer(String Q_answer) {
        this.Q_answer = Q_answer;
    }

    public int getQ_points() {
        return Q_points;
    }

    public void setQ_points(int Q_points) {
        this.Q_points = Q_points;
    }

    public String getQ_type() {
        return Q_type;
    }

    public void setQ_type(String Q_type) {
        this.Q_type = Q_type;
    }

    public String getIdQ_topic() {
        return Q_topic;
    }

    public void setIdQ_topic(String Q_topic) {
        this.Q_topic = Q_topic;
    }
}
