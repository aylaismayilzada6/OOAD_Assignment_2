
public class Reader{
    private int ReadCnt;
    private final RingBuffer buffer;

    public Reader(RingBuffer buffer) {
        this.buffer = buffer;
        this.ReadCnt = Math.max(0, buffer.getWriteCnt() - buffer.getCapacity());
    }

    public Integer read(){
        int currentWrite = buffer.getWriteCnt();
        int oldValue = Math.max(0, currentWrite-buffer.getCapacity());

     
        // main case, we need to check what happens when the reader 
        // is too slow 
        // If reader is too slow, it misses items. Catch up to oldest available
        
        if (ReadCnt < oldValue) {
            ReadCnt = oldValue;
        }

        // No new data to read
        if (ReadCnt >= currentWrite) {
            return null;
        }

        int val = buffer.read(ReadCnt); 
        ReadCnt++;
        return val ;
    }
    
}

