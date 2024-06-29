package TaskPage;

public class Tasks {
    private int idTaskSQL;
    private String taskTitle;
    private String taskDetail;
    private String taskSetBy;
    private String taskDueDay;
    private int taskInIdExam;
    private String taskTime;
    private String taskMode;
    private String taskFor;
    private String taksFinish;
    private String taskPercentage;
    private String taskGrade;
    private String taskGradecode;
    private String taskfinishtime;

    // constructor

    public Tasks(int idTaskSQL, String taskTitle, String taskDetail, String taskSetBy, String taskDueDay,
            int taskInIdExam, String taskTime, String taskMode, String taskFor, String taskPercentage,
            String taksFinish, String taskGrade, String taskGradecode, String taskfinishtimeString) {
        this.idTaskSQL = idTaskSQL;
        this.taskTitle = taskTitle;
        this.taskDetail = taskDetail;
        this.taskSetBy = taskSetBy;
        this.taskDueDay = taskDueDay;
        this.taskInIdExam = taskInIdExam;
        this.taskTime = taskTime;
        this.taskMode = taskMode;

        this.taskFor = taskFor;
        this.taksFinish = taksFinish;
        this.taskPercentage = taskPercentage;
        this.taskGrade = taskGrade;
        this.taskGradecode = taskGradecode;
        this.taskfinishtime = taskfinishtimeString;

    }

    public int getIdTaskSQL() {
        return idTaskSQL;
    }

    public void setIdTaskSQL(int idTaskSQL) {
        this.idTaskSQL = idTaskSQL;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getTaskSetBy() {
        return taskSetBy;
    }

    public void setTaskSetBy(String taskSetBy) {
        this.taskSetBy = taskSetBy;
    }

    public String getTaskDueDay() {
        return taskDueDay;
    }

    public void setTaskDueDay(String taskDueDay) {
        this.taskDueDay = taskDueDay;
    }

    public int getTaskInIdExam() {
        return taskInIdExam;
    }

    public void setTaskInIdExam(int taskInIdExam) {
        this.taskInIdExam = taskInIdExam;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getTaskMode() {
        return taskMode;
    }

    public void setTaskMode(String taskMode) {
        this.taskMode = taskMode;
    }

    public String getTaskFor() {
        return taskFor;
    }

    public void setTaskFor(String taskFor) {
        this.taskFor = taskFor;
    }

    public String getTaskFinish() {
        return taksFinish;
    }

    public void setTaskFinish(String taskFinish) {
        this.taksFinish = taskFinish;
    }

    public String getTaskPercentage() {
        return taskPercentage;
    }

    public void setTaskPercentage(String taskPercentage) {
        this.taskPercentage = taskPercentage;
    }

    public String getTaskGrade() {
        return taskGrade;
    }

    public void setTaskGrade(String taskGrade) {
        this.taskGrade = taskGrade;
    }

    public String getTaskGradecode() {
        return taskGradecode;
    }

    public void setTaskGradecode(String taskGradecode) {
        this.taskGradecode = taskGradecode;
    }

    public String getTaskFinishTime() {
        return taskfinishtime;
    }

    public void setTaskFinishTime(String taskfinishtime) {
        this.taskfinishtime = taskfinishtime;
    }
}
