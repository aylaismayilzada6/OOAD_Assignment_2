import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        RingBufferTests.class,
        ReaderTests.class,
        MainTests.class
})
public class TestRunner {
}
