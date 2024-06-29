package AI;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIclient {
    private String url;
    private String apiKey;

    public AIclient(String url, String apiKey) {
        this.url = url;
        this.apiKey = apiKey;
    }

    public String requestToAI(String message) {
        String responseString = "";

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Add request headers
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("X-RapidAPI-Key", apiKey);
            con.setRequestProperty("X-RapidAPI-Host", "open-ai21.p.rapidapi.com");

            // Prepare the payload
            String payload = "{\"message\": \"" + message + "\"}";

            // Send POST request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(payload);
            wr.flush();
            wr.close();

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            responseString = response.toString();
            System.out.println(responseString);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }

    public static void main(String[] args) {
        AIclient AIclient = new AIclient("https://open-ai21.p.rapidapi.com/chatmpt",
                "5bf756e926mshd6d1b61242471cfp19f75cjsnfd3519390356");
        System.out.println(AIclient.requestToAI("hi"));
    }
}
