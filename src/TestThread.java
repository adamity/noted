public class TestThread implements Runnable {
    TestThread() {
    }

    @Override
    public void run() {
        System.out.println("Hello from thread");
    }
}
