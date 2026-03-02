package RingBuffer;


public class RingBuffer {
    private final int[] buffer;
    private final int capacity; 
    private int WriteCnt  = 0;

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new int [capacity];

    }


    public void  write(int val) {
        
        int i = (WriteCnt % capacity);
        buffer[i] = val; 
        WriteCnt++; 
    }

    protected int read(long sequence ) {
        int i = (int) (sequence % capacity);
        return buffer[i];
    }

    public int getWriteCnt(){
        return WriteCnt;
    }
    public int getCapacity(){

        return capacity;
    }



}
