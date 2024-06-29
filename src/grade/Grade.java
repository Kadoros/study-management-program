package grade;

public class Grade {
    private int userKey;
    private int topic1Grade;
    private int topic2Grade;
    private int topic3Grade;
    private int topic4Grade;
    private int topic5Grade;
    private int topic6Grade;

    public Grade(int userKey, int topic1Grade, int topic2Grade, int topic3Grade,
            int topic4Grade, int topic5Grade, int topic6Grade) {
        this.userKey = userKey;
        this.topic1Grade = topic1Grade;
        this.topic2Grade = topic2Grade;
        this.topic3Grade = topic3Grade;
        this.topic4Grade = topic4Grade;
        this.topic5Grade = topic5Grade;
        this.topic6Grade = topic6Grade;
    }

    public int getUserKey() {
        return userKey;
    }

    public void setUserKey(int userKey) {
        this.userKey = userKey;
    }

    public int getTopic1Grade() {
        return topic1Grade;
    }

    public void setTopic1Grade(int topic1Grade) {
        this.topic1Grade = topic1Grade;
    }

    public int getTopic2Grade() {
        return topic2Grade;
    }

    public void setTopic2Grade(int topic2Grade) {
        this.topic2Grade = topic2Grade;
    }

    public int getTopic3Grade() {
        return topic3Grade;
    }

    public void setTopic3Grade(int topic3Grade) {
        this.topic3Grade = topic3Grade;
    }

    public int getTopic4Grade() {
        return topic4Grade;
    }

    public void setTopic4Grade(int topic4Grade) {
        this.topic4Grade = topic4Grade;
    }

    public int getTopic5Grade() {
        return topic5Grade;
    }

    public void setTopic5Grade(int topic5Grade) {
        this.topic5Grade = topic5Grade;
    }

    public int getTopic6Grade() {
        return topic6Grade;
    }

    public void setTopic6Grade(int topic6Grade) {
        this.topic6Grade = topic6Grade;
    }
}
