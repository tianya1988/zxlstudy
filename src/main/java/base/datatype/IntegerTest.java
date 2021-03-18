package base.datatype;

/**
 * Created by jason on 21-2-23.
 */
public class IntegerTest {

    public static void main(String[] args) {
        Integer a = 127;
        Integer b = 127;

        Integer c = 129;
        Integer d = 129;

        System.out.println(a == b); // true
        System.out.println(c == d); // false


        Long a2 = 127l;
        Long b2 = 127l;

        Long c2 = 129l;
        Long d2 = 129l;

        System.out.println(a2 == b2); // true
        System.out.println(c2 == d2); // false

        Double a3 = 127d;
        Double b3 = 127d;

        Double c3 = 129d;
        Double d3 = 129d;

        System.out.println(a3 == b3); // false
        System.out.println(c3 == d3); // false
    }
}
