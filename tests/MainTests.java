import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTests {
    @Test
    void mainPrintsTheExpectedDemoOutput() throws Exception {
        String actual = TestSupport.captureStandardOut(() -> Main.main(new String[0]));

        String expected = String.join("\n",
                "After writing 10,20,30:",
                "10",
                "10",
                "20",
                "20",
                "30",
                "30",
                "After writing 40 (overwrites oldest):",
                "40",
                "40",
                "After writing 50 (overwrites next oldest):",
                "50",
                "50",
                "No new data:",
                "null",
                "null");

        assertEquals(expected, actual, "Main should print the documented demonstration output.");
    }
}
