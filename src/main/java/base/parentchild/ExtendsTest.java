package base.parentchild;

/**
 * Created by jason on 17-9-19.
 */

/**
 * 父类的private变量，也会被继承并且初始化在子类父对象中，只不过对外不可见。
 */
public class ExtendsTest {

    public static void main(String[] args) {
        Child child = new Child();
        // 父类public
        child.age = 10;

        // 父类public static
        child.phone = "sfa";

        /*父类private int不可访问
        child.id;*/

       /* 子类private不能访问
        child.address();*/

        // 可以继承父类中的公共方法
        System.out.println(child.getId());


    }
}
