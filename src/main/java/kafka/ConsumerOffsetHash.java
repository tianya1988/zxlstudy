package kafka;

/**
 * Created by jason on 17-12-26.
 */
public class ConsumerOffsetHash {
    public static void main(String[] args) {
        System.out.println(Math.abs("test-zxl7".hashCode()) % 51);
    }
}
