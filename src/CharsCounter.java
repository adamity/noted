import javax.swing.*;

public class CharsCounter implements Runnable {
    private String text;
    private JLabel label;

    CharsCounter(String text, JLabel label) {
        this.text = text;
        this.label = label;
    }

    @Override
    public void run() {
        label.setText("Characters: " + text.length());
    }

    public void setText(String text) {
        this.text = text;
    }
}
