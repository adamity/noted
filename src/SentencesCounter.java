import javax.swing.*;

public class SentencesCounter implements Runnable {
    private String text;
    private JLabel label;

    SentencesCounter(String text, JLabel label) {
        this.text = text;
        this.label = label;
    }

    @Override
    public void run() {
        label.setText("Sentences: " + countSentences());
    }

    public int countSentences() {
        int count = 0;
        String[] sentences = text.split("[.!?]");
        for (String sentence : sentences) {
            if (!sentence.equals("")) {
                count++;
            }
        }

        return count;
    }

    public void setText(String text) {
        this.text = text;
    }
}
