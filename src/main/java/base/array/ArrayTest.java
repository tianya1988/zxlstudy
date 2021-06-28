package base.array;

import base.test.Person;

/**
 * Created by jason on 21-5-31.
 */
public class ArrayTest {
    public static void main(String[] args) {
        int[] ints = new int[10];
        ints[0] = 0;
        ints[1] = 1;
        System.out.println(ints.length);//10
        System.out.println(ints[2]);//0


        Person[] persons = new Person[10];
        Person person = new Person();
        person.setName("zhangsan");
        person.setAddress("beijing");
        person.setAge(10);
        persons[0] = person;
        System.out.println(persons.length);// 10
        System.out.println(persons[1]);// null

    }
}
