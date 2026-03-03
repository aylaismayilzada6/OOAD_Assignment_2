
public class Main {
    public static void main(String[] args) {

        RingBuffer buffer = new RingBuffer(3);

        // Create readers before writing so they start from the beginning (ReadCnt = 0)
        Reader r1 = new Reader(buffer);
        Reader r2 = new Reader(buffer);

        // Fill the buffer (no overwrite yet)
        buffer.write(10); // seq 0
        buffer.write(20); // seq 1
        buffer.write(30); // seq 2

        System.out.println("After writing 10,20,30:");
        System.out.println(r1.read()); // 10
        System.out.println(r2.read()); // 10
        System.out.println(r1.read()); // 20
        System.out.println(r2.read()); // 20
        System.out.println(r1.read()); // 30
        System.out.println(r2.read()); // 30

        // Now overwrite starts (capacity = 3)
        buffer.write(40); // seq 3 overwrites 10's slot (index 0)

        System.out.println("After writing 40 (overwrites oldest):");
        System.out.println(r1.read()); // 40
        System.out.println(r2.read()); // 40

        buffer.write(50); // seq 4 overwrites 20's slot (index 1)

        System.out.println("After writing 50 (overwrites next oldest):");
        System.out.println(r1.read()); // 50
        System.out.println(r2.read()); // 50

        // Nothing new
        System.out.println("No new data:");
        System.out.println(r1.read()); // null
        System.out.println(r2.read()); // null
    }
}
