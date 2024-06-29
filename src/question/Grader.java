package question;

import org.json.JSONObject;

import AI.AIclient;
import AI.AIclient2;

public class Grader {

    private static String justification;
    private static int awardMark = 0;
    private static int maxMark;
    private static String questionString;
    private static String markSheetAnswer;
    private static String prompt;
    private static String userAnswer;
    private static AIclient AIclient = new AIclient("https://open-ai21.p.rapidapi.com/chatmpt",
            "5bf756e926mshd6d1b61242471cfp19f75cjsnfd3519390356");

    private static String response;

    public Grader(Question question, String userAnswer) {
        questionString = question.getQ_question();
        markSheetAnswer = question.getQ_answer();
        maxMark = question.getQ_points();
        this.userAnswer = userAnswer;

        maxMark = question.getQ_points();
        String generatedPrompt = settingPrompt(questionString, markSheetAnswer, userAnswer, maxMark);
        AIclient2 AIclient = new AIclient2("https://chatgpt-best-price.p.rapidapi.com/v1/chat/completions");
        String content = generatedPrompt;
        String respeonse = AIclient.requestToAI(content);

        ResponseParser(respeonse);

        // Marking();
        // cutter(response);

    }

    public static String settingPrompt(String question, String markSheet, String userAnswer, int maxMark) {
        prompt = String.format(
                "you are grader, the IGCSE student answered determine awardmark and justfication     " +
                        "Question is " + question + "   " +
                        "marksheet is " + markSheet + "     " +
                        "useranswer is " + userAnswer + "   " +
                        "maxMark is " + maxMark + "     " + "    " +
                        "Please answer in the following format with the above information.      " +
                        "     " +
                        "Award Mark:            " +
                        "Justification:"

        );
        return prompt;
    }

    public static void ResponseParser(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject messageObject = jsonObject.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message");

        String content = messageObject.getString("content");
        int startIndex = content.indexOf("Award Mark: ") + 12;
        int endIndex = content.indexOf("\n", startIndex);

        awardMark = Integer.parseInt(content.substring(startIndex, endIndex).trim());

        startIndex = content.indexOf("Justification: ") + 15;
        justification = content.substring(startIndex).trim();

        System.out.println(awardMark);
        System.out.println("\n");
        System.out.println(justification);
    }

    public int awardMark() {

        return awardMark;
    }

    public String justification() {

        return justification;
    }

    public static void main(String[] args) {

        String generatedPrompt = settingPrompt("what is Newton's second law?",
                "F = ma, or net force is equal to mass times acceleration,", "force is equal to mass times velocity",
                5);

        AIclient2 AIclient = new AIclient2("https://chatgpt-best-price.p.rapidapi.com/v1/chat/completions");
        String content = generatedPrompt;
        System.out.println(content);
        String respeonse = AIclient.requestToAI(content);
        System.out.println(respeonse);

        ResponseParser(respeonse);

    }
}
