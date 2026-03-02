package RingBuffer;


public class Reader{
    private int ReadCnt;
    private final RingBuffer buffer;

    public Reader(RingBuffer buffer) {
        this.buffer = buffer;
        this.ReadCnt = buffer.getWriteCnt();
    }

    public Integer read(){
        int currentWrite = buffer.getWriteCnt();

        // if no new data 
        if (ReadCnt >= currentWrite){
            return null;
        }

        // main case, we need to check what happens when the reader 
        // is too slow 
        // if so, we need to throw an exception 

        if (ReadCnt < currentWrite - buffer.getCapacity()){
            throw new IllegalStateException("Data was overwritten because the reader too slow.");
        }

        int val = buffer.read(ReadCnt); 
        ReadCnt++;
        return val ;
    }
    
}
