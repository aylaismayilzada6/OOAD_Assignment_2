import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

final class TestSupport {
    @FunctionalInterface
    interface ThrowingRunnable {
        void run() throws Exception;
    }

    private TestSupport() {
    }

    static String captureStandardOut(ThrowingRunnable runnable) throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PrintStream capturedOut = new PrintStream(outputStream, true, StandardCharsets.UTF_8.name())) {
            System.setOut(capturedOut);
            runnable.run();
        } finally {
            System.setOut(originalOut);
        }

        return normalizeNewlines(new String(outputStream.toByteArray(), StandardCharsets.UTF_8)).trim();
    }

    static String normalizeNewlines(String value) {
        return value.replace("\r\n", "\n");
    }
}
