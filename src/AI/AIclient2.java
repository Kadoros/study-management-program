package AI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class AIclient2 {
    private String url;
    private String model = "gpt-3.5-turbo";

    public AIclient2(String url) {
        this.url = url;
    }

    public String requestToAI(String content) {
        String responseString = "";

        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .header("X-RapidAPI-Key", "5bf756e926mshd6d1b61242471cfp19f75cjsnfd3519390356")
                    .header("X-RapidAPI-Host", "chatgpt-best-price.p.rapidapi.com")
                    .POST(HttpRequest.BodyPublishers.ofString("{ \"model\": \"" + model
                            + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + content + "\"}] }"))
                    .build();
                    // For a more accurate answer, we can expect 
                    // AI's answers to be more accurate 
                    // by attaching examples of basic and basic answers 
                    // to the payload as additional information.

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            responseString = response.body();
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return responseString;
    }

}
