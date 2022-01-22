public class App {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        Thread thread = new Thread(editor);
        thread.start();
    }
}