package mainPage;

public class ExamSet {
    private int idexam_set;
    private String exam_name;
    private String exam_QNum;
    private String exam_diff;
    private String exam_context;

    // Constructor
    public ExamSet(int idexam_set, String exam_name, String exam_QNum, String exam_diff, String exam_context) {
        this.idexam_set = idexam_set;
        this.exam_name = exam_name;
        this.exam_QNum = exam_QNum;
        this.exam_diff = exam_diff;
        this.exam_context = exam_context;
    }

    // Getters and Setters

    public int getIdexam_set() {
        return idexam_set;
    }

    public void setIdexam_set(int idexam_set) {
        this.idexam_set = idexam_set;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getExam_QNum() {
        return exam_QNum;
    }

    public void setExam_QNum(String exam_QNum) {
        this.exam_QNum = exam_QNum;
    }

    public String getExam_diff() {
        return exam_diff;
    }

    public void setExam_diff(String exam_diff) {
        this.exam_diff = exam_diff;
    }

    public String getExam_context() {
        return exam_context;
    }

    public void setExam_context(String exam_context) {
        this.exam_context = exam_context;
    }
}
