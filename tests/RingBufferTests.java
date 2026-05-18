import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RingBufferTests {
    @Test
    void constructorExposesCapacityAndStartsEmpty() {
        RingBuffer buffer = new RingBuffer(3);

        assertEquals(3, buffer.getCapacity(), "RingBuffer should expose the configured capacity.");
        assertEquals(0, buffer.getWriteCnt(), "RingBuffer should start with a zero write counter.");
    }

    @Test
    void writeIncrementsCounterAndStoresBySequence() {
        RingBuffer buffer = new RingBuffer(4);

        buffer.write(10);
        buffer.write(20);
        buffer.write(30);

        assertEquals(3, buffer.getWriteCnt(), "Each write should increment the write counter.");
        assertEquals(10, buffer.read(0), "Sequence 0 should map to the first value written.");
        assertEquals(20, buffer.read(1), "Sequence 1 should map to the second value written.");
        assertEquals(30, buffer.read(2), "Sequence 2 should map to the third value written.");
    }

    @Test
    void writeWrapsAroundAndPreservesTheLatestWindow() {
        RingBuffer buffer = new RingBuffer(3);

        buffer.write(10);
        buffer.write(20);
        buffer.write(30);
        buffer.write(40);

        assertEquals(4, buffer.getWriteCnt(), "Write counter should reflect all writes, including overwrites.");
        assertEquals(20, buffer.read(1), "Oldest available sequence should still be readable after wrap-around.");
        assertEquals(30, buffer.read(2), "Middle sequence should remain readable after wrap-around.");
        assertEquals(40, buffer.read(3), "Latest sequence should be readable after wrap-around.");
    }

    @Test
    void capacityOneRetainsOnlyTheLatestValue() {
        RingBuffer buffer = new RingBuffer(1);

        buffer.write(7);
        buffer.write(8);
        buffer.write(9);

        assertEquals(3, buffer.getWriteCnt(), "Capacity-one buffer should still count every write.");
        assertEquals(9, buffer.read(2), "The latest sequence should map to the only available slot.");
    }

    @Test
    void zeroCapacityFailsWhenWriting() {
        RingBuffer buffer = new RingBuffer(0);

        assertEquals(0, buffer.getCapacity(), "Zero capacity is currently accepted by the constructor.");
        assertThrows(ArithmeticException.class,
                () -> buffer.write(1),
                "Writing to a zero-capacity buffer should fail with the current implementation.");
    }

    @Test
    void negativeCapacityFailsDuringConstruction() {
        assertThrows(NegativeArraySizeException.class,
                () -> new RingBuffer(-1),
                "Negative capacity should fail during array allocation.");
    }
}
