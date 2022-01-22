import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.swing.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class LanguageDetector implements Runnable {
    private String text;
    private JLabel label;

    LanguageDetector(String text, JLabel label) {
        this.text = text;
        this.label = label;
    }

    LanguageDetector(String text) {
        this.text = text;
    }

    @Override
    public void run() {
        label.setText("Language: " + detectLanguage());
    }

    public String detectLanguage() {
        String response = "";

        try {
            URL url = new URL("https://translate.mentality.rip/detect");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("accept", "application/json");
            http.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

            if (text.isEmpty()) text = null;
            String data = "q="+ text;

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
                JSONArray array = (JSONArray) parse.parse(inline);
                JSONObject data_obj = (JSONObject) array.get(0);

                response = data_obj.get("language").toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
