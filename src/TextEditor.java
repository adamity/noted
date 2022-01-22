import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextEditor implements Runnable {
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenHeight = (int) (screenSize.height * 0.8);
    private int screenWidth = (int) (screenSize.width * 0.8);
    private String filepath = "";
    private String[][] languagesArray = {
        {"en", "English"},
        {"ar", "Arabic"},
        {"zh", "Chinese"},
        {"fr", "French"},
        {"de", "German"},
        {"hi", "Hindi"},
        {"id", "Indonesian"},
        {"ga", "Irish"},
        {"it", "Italian"},
        {"ja", "Japanese"},
        {"ko", "Korean"},
        {"pl", "Polish"},
        {"pt", "Portuguese"},
        {"ru", "Russian"},
        {"es", "Spanish"},
        {"tr", "Turkish"},
        {"vi", "Vietnamese"}
    };

    private JFrame frame;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newWindow;
    private JMenuItem openFile;
    private JMenuItem saveFile;
    private JMenuItem saveAsFile;
    private JMenuItem exitFile;

    private JTextArea textArea;
    private JScrollPane scrollPane;

    private JPopupMenu popupMenu;
    private JMenu translateTo;
    private JMenuItem[] langsMenuItem;

    private JPanel panel;
    private JLabel countCharsLabel;
    private JLabel countWordsLabel;
    private JLabel countSentencesLabel;
    private JLabel languageLabel;
    private JLabel sentimentLabel;

    public TextEditor() {
        frame = new JFrame("Noted");

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        newWindow = new JMenuItem("New Window");
        openFile = new JMenuItem("Open");
        saveFile = new JMenuItem("Save");
        saveAsFile = new JMenuItem("Save As");
        exitFile = new JMenuItem("Exit");

        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);

        popupMenu = new JPopupMenu();
        translateTo = new JMenu("Translate To ...");
        langsMenuItem = new JMenuItem[languagesArray.length];

        panel = new JPanel(new GridLayout());
        countCharsLabel = new JLabel("Characters: 0");
        countWordsLabel = new JLabel("Words: 0");
        countSentencesLabel = new JLabel("Sentences: 0");
        languageLabel = new JLabel("Language: ");
        sentimentLabel = new JLabel("Sentiment: ");
    }

    @Override
    public void run() {
        newWindow.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        newWindow.addActionListener(l -> this.newWindow());

        openFile.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        openFile.addActionListener(l -> this.openFile());

        saveFile.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        saveFile.addActionListener(l -> this.saveFile());

        saveAsFile.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        saveAsFile.addActionListener(l -> this.saveAsFile());

        exitFile.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        exitFile.addActionListener(l -> this.exitFile());

        fileMenu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        fileMenu.add(newWindow);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(exitFile);
        menuBar.add(fileMenu);

        int i = 0;
        for (String[] language : languagesArray) {
            langsMenuItem[i] = new JMenuItem(language[1]);
            langsMenuItem[i].setActionCommand(language[0]);

            langsMenuItem[i].setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
            langsMenuItem[i].addActionListener(l -> this.translate(l.getActionCommand()));

            translateTo.add(langsMenuItem[i]);
            i++;
        }

        translateTo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        popupMenu.add(translateTo);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textArea.addMouseListener(this.mouseListen());
        textArea.addKeyListener(this.keyListen());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(countCharsLabel);
        panel.add(countWordsLabel);
        panel.add(countSentencesLabel);
        panel.add(languageLabel);
        panel.add(sentimentLabel);

        frame.add(menuBar, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        updateStatusBar();
    }

    private void newWindow() {
        TextEditor editor = new TextEditor();
        Thread thread = new Thread(editor);

        thread.start();
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");

        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Open File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            filepath = fileChooser.getSelectedFile().getAbsolutePath();

            try {
                BufferedReader br = new BufferedReader(new FileReader(filepath));
                String tempLine = "", tempText = "";

                while ((tempLine = br.readLine()) != null) {
                    tempText += tempLine + "\n";
                }

                textArea.setText(tempText);
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveFile() {
        if (filepath.equals("")) {
            saveAsFile.doClick();
        } else {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
                bw.write(textArea.getText());
                bw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");

        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Save File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            filepath = fileChooser.getSelectedFile().getAbsolutePath();
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
            bw.write(textArea.getText());
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void exitFile() {
        frame.dispose();
    }

    private void translate(String targetLang) {
        LanguageDetector tempLangDetector = new LanguageDetector(textArea.getSelectedText());
        String sourceLang = tempLangDetector.detectLanguage();

        Translation translation = new Translation(textArea, textArea.getSelectionStart(), textArea.getSelectionEnd(), sourceLang, targetLang);
        Thread translationThread = new Thread(translation);
        translationThread.start();
    }

    private void updateStatusBar() {
        CharsCounter countChars = new CharsCounter(textArea.getText(), countCharsLabel);
        Thread countCharsThread = new Thread(countChars);
        countCharsThread.start();

        WordsCounter countWords = new WordsCounter(textArea.getText(), countWordsLabel);
        Thread countWordsThread = new Thread(countWords);
        countWordsThread.start();

        SentencesCounter countSentences = new SentencesCounter(textArea.getText(), countSentencesLabel);
        Thread countSentencesThread = new Thread(countSentences);
        countSentencesThread.start();

        LanguageDetector languageDetector = new LanguageDetector(textArea.getText(), languageLabel);
        Thread languageDetectorThread = new Thread(languageDetector);
        languageDetectorThread.start();

        SentimentDetector sentimentDetector = new SentimentDetector(textArea.getText(), sentimentLabel);
        Thread sentimentDetectorThread = new Thread(sentimentDetector);
        sentimentDetectorThread.start();
    }

    private MouseListener mouseListen() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    String selectedText = textArea.getSelectedText();
                    if (selectedText != null) popupMenu.show(textArea, e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
    }

    private KeyListener keyListen() {
        return new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateStatusBar();
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        };
    }
}