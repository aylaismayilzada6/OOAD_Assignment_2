import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReaderTests {
    @Test
    void readerCreatedBeforeWritesConsumesFromTheBeginning() {
        RingBuffer buffer = new RingBuffer(3);
        Reader reader = new Reader(buffer);

        buffer.write(10);
        buffer.write(20);

        assertEquals(Integer.valueOf(10), reader.read(), "Reader should see the first value written.");
        assertEquals(Integer.valueOf(20), reader.read(), "Reader should see the second value written.");
        assertNull(reader.read(), "Reader should return null after consuming all available values.");
    }

    @Test
    void independentReadersDoNotInterfereWithEachOther() {
        RingBuffer buffer = new RingBuffer(3);
        Reader firstReader = new Reader(buffer);
        Reader secondReader = new Reader(buffer);

        buffer.write(10);
        buffer.write(20);
        buffer.write(30);

        assertEquals(Integer.valueOf(10), firstReader.read(), "First reader should start at the first value.");
        assertEquals(Integer.valueOf(20), firstReader.read(), "First reader should advance independently.");
        assertEquals(Integer.valueOf(10), secondReader.read(), "Second reader should not be affected by the first reader.");
        assertEquals(Integer.valueOf(20), secondReader.read(), "Second reader should maintain its own position.");
        assertEquals(Integer.valueOf(30), firstReader.read(), "First reader should continue from its own position.");
        assertEquals(Integer.valueOf(30), secondReader.read(), "Second reader should still see the final value.");
    }

    @Test
    void readerReturnsNullWhenNoDataIsAvailable() {
        RingBuffer buffer = new RingBuffer(2);
        Reader reader = new Reader(buffer);

        assertNull(reader.read(), "Reader should return null when the buffer is empty.");

        buffer.write(5);

        assertEquals(Integer.valueOf(5), reader.read(), "Reader should return the only available value.");
        assertNull(reader.read(), "Reader should return null again after the value is consumed.");
    }

    @Test
    void slowReaderSkipsOverwrittenItemsAndCatchesUp() {
        RingBuffer buffer = new RingBuffer(3);
        Reader reader = new Reader(buffer);

        buffer.write(10);
        buffer.write(20);
        buffer.write(30);
        buffer.write(40);
        buffer.write(50);

        assertEquals(Integer.valueOf(30), reader.read(), "Slow reader should catch up to the oldest available value.");
        assertEquals(Integer.valueOf(40), reader.read(), "Reader should continue sequentially after catching up.");
        assertEquals(Integer.valueOf(50), reader.read(), "Reader should reach the most recent value.");
        assertNull(reader.read(), "Reader should return null after consuming the remaining values.");
    }

    @Test
    void readerCreatedAfterPartialWritesStartsAtTheEarliestBufferedValue() {
        RingBuffer buffer = new RingBuffer(5);
        buffer.write(11);
        buffer.write(22);
        Reader reader = new Reader(buffer);

        assertEquals(Integer.valueOf(11), reader.read(), "Reader created before overflow should start at sequence zero.");
        assertEquals(Integer.valueOf(22), reader.read(), "Reader should continue through existing buffered values.");
        assertNull(reader.read(), "Reader should stop after consuming the buffered data.");
    }

    @Test
    void readerCreatedAfterOverflowStartsAtTheOldestAvailableValue() {
        RingBuffer buffer = new RingBuffer(3);
        buffer.write(10);
        buffer.write(20);
        buffer.write(30);
        buffer.write(40);
        buffer.write(50);
        Reader reader = new Reader(buffer);

        assertEquals(Integer.valueOf(30), reader.read(), "Late reader should start at the oldest available sequence after overflow.");
        assertEquals(Integer.valueOf(40), reader.read(), "Late reader should continue through the retained window.");
        assertEquals(Integer.valueOf(50), reader.read(), "Late reader should reach the latest value.");
        assertNull(reader.read(), "Late reader should return null after exhausting the retained window.");
    }

    @Test
    void readerContinuesCorrectlyAfterCatchUpAndFurtherWrites() {
        RingBuffer buffer = new RingBuffer(3);
        Reader reader = new Reader(buffer);

        buffer.write(1);
        buffer.write(2);
        buffer.write(3);
        buffer.write(4);

        assertEquals(Integer.valueOf(2), reader.read(), "Reader should skip the overwritten value and catch up.");

        buffer.write(5);

        assertEquals(Integer.valueOf(3), reader.read(), "Reader should continue from the next unread sequence.");
        assertEquals(Integer.valueOf(4), reader.read(), "Reader should read the value written before the latest overwrite.");
        assertEquals(Integer.valueOf(5), reader.read(), "Reader should read newly written data after catching up.");
        assertNull(reader.read(), "Reader should return null after consuming all available values.");
    }

    @Test
    void slowAndFastReadersRemainIndependentAcrossOverwrites() {
        RingBuffer buffer = new RingBuffer(3);
        Reader fastReader = new Reader(buffer);
        Reader slowReader = new Reader(buffer);

        buffer.write(10);
        buffer.write(20);
        buffer.write(30);

        assertEquals(Integer.valueOf(10), fastReader.read(), "Fast reader should consume the first value.");
        assertEquals(Integer.valueOf(20), fastReader.read(), "Fast reader should advance without affecting the slow reader.");
        assertEquals(Integer.valueOf(30), fastReader.read(), "Fast reader should consume the third value.");

        buffer.write(40);
        buffer.write(50);

        assertEquals(Integer.valueOf(30), slowReader.read(), "Slow reader should catch up to the oldest value still available.");
        assertEquals(Integer.valueOf(40), slowReader.read(), "Slow reader should continue through the retained window.");
        assertEquals(Integer.valueOf(50), slowReader.read(), "Slow reader should reach the newest value.");

        assertEquals(Integer.valueOf(40), fastReader.read(), "Fast reader should continue from its own position after additional writes.");
        assertEquals(Integer.valueOf(50), fastReader.read(), "Fast reader should see the newest value independently.");
    }
}
