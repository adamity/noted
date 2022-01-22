import javax.swing.*;

public class WordsCounter implements Runnable {
    private String text;
    private JLabel label;

    WordsCounter(String text, JLabel label) {
        this.text = text;
        this.label = label;
    }

    @Override
    public void run() {
        label.setText("Words: " + countWords());
    }

    public int countWords() {
        int count = 0;
        String[] words = text.split(" ");
        for (String word : words) {
            if (!word.equals("")) {
                count++;
            }
        }

        return count;
    }

    public void setText(String text) {
        this.text = text;
    }
}
