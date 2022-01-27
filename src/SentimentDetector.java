import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.swing.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class SentimentDetector implements Runnable {
    private String text;
    private JLabel label;

    SentimentDetector(String text, JLabel label) {
        this.text = text;
        this.label = label;
    }

    @Override
    public void run() {
        label.setText("Sentiment: " + detectSentiment());
    }

    public String detectSentiment() {
        String response = "";

        try {
            URL url = new URL("https://sentim-api.herokuapp.com/api/v1/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("accept", "application/json");
            http.setRequestProperty("Content-type", "application/json");

            if (text.isEmpty()) text = null;
            String data = "{ \"text\": \""+ text +"\" }";

            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            OutputStream stream = http.getOutputStream();
            stream.write(out);

            if (http.getResponseCode() != 200) {
                throw new RuntimeException("HttpResponseCode: " + http.getResponseCode() + " " + http.getResponseMessage());
            } else {
                Scanner scanner = new Scanner(http.getInputStream());
                String inline = "";

                while (scanner.hasNextLine()) {
                    inline += scanner.nextLine();
                }
                scanner.close();
                http.disconnect();

                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);
                JSONObject result = (JSONObject) data_obj.get("result");

                response = result.get("type").toString().substring(0, 1).toUpperCase() + result.get("type").toString().substring(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
