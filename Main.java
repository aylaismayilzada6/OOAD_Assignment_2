package RingBuffer;

public class Main {

    public static void main(String[] args) {

        RingBuffer buffer = new RingBuffer(3);

        Reader r1 = new Reader(buffer);
        Reader r2 = new Reader(buffer);

        buffer.write(10);
        buffer.write(20);
        buffer.write(30);

        System.out.println(r1.read());
        System.out.println(r2.read());

        buffer.write(40); // overwrite happens

        System.out.println(r1.read());
        System.out.println(r2.read());
    }
}