import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.swing.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Translation implements Runnable {
    private JTextArea textArea;
    private String sourceLanguage;
    private String targetLanguage;
    private int startText;
    private int endText;

    Translation(JTextArea textArea, int startText, int endText, String sourceLanguage, String targetLanguage) {
        this.textArea = textArea;
        this.startText = startText;
        this.endText = endText;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    @Override
    public void run() {
        translate();
    }

    public String translate() {
        String text = textArea.getText();
        String textToTranslate = text.substring(startText, endText);
        String response = "";

        textArea.setEditable(false);
        try {
            URL url = new URL("https://translate.mentality.rip/translate");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("accept", "application/json");
            http.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

            if (textToTranslate.isEmpty()) textToTranslate = null;
            String data = "q="+ textToTranslate + "&source=" + sourceLanguage + "&target=" + targetLanguage + "&format=text";

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

                response = data_obj.get("translatedText").toString();
                textArea.replaceRange(response, startText, endText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        textArea.setEditable(true);
        textArea.setCaretPosition(textArea.getText().length());
        textArea.getCaret().setVisible(true);

        return response;
    }
}
