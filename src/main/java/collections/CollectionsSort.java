package collections;

import java.util.*;

/**
 * Created by jason on 21-5-27.
 * <p/>
 * 实际上，在使用Collection的sort排序的集合元素都必须是Comparable接口的实现类，该接口表示子类是可以比较的。因为实现接口必须重写抽象方法 - int compareTo(T t)。
 */
public class CollectionsSort {

    public static void main(String[] args) {
        // 基础数据类型, 数据类型底层已经实现了Comparable接口
        baseDataTyeSort();

        // 自定义对象,需要实现Comparable接口,实现 public int compareTo(Cell o) {} 比较逻辑方法
        // 改的代码越多，侵入性比较强，越不利于程序的扩展
        selfDefObject();

        // 用于比较的类不用修改, 自定义个比较器然后传入即可, 与匿名内部类其实一样.
        extraComparator();

        // 匿名内部类方式,推荐用法
        innerClassCompare();
    }

    private static void innerClassCompare() {
        List<CellOK> cells = new ArrayList<CellOK>();

        cells.add(new CellOK(2, 3));
        cells.add(new CellOK(5, 1));
        cells.add(new CellOK(3, 2));
        System.out.println(cells);

        Collections.sort(cells, new Comparator<CellOK>() {
            @Override
            public int compare(CellOK o1, CellOK o2) {
                return o1.getX() - o2.getX();
            }
        });
        System.out.println(cells);
    }

    private static void extraComparator() {
        List<CellOK> cells = new ArrayList<CellOK>();

        cells.add(new CellOK(2, 3));
        cells.add(new CellOK(5, 1));
        cells.add(new CellOK(3, 2));
        System.out.println(cells);
        MyComparator com = new MyComparator();
        Collections.sort(cells, com);
        System.out.println(cells);
    }

    private static void selfDefObject() {
        List<Cell> cells = new ArrayList<Cell>();
        cells.add(new Cell(2, 3));
        cells.add(new Cell(5, 1));
        cells.add(new Cell(3, 2));
        System.out.println(cells);//[Cell [x=2, y=3], Cell [x=5, y=1], Cell [x=3, y=2]]
        Collections.sort(cells);
        System.out.println(cells);//[Cell [x=2, y=3], Cell [x=3, y=2], Cell [x=5, y=1]]
    }

    private static void baseDataTyeSort() {
        List<Integer> list0 = new ArrayList<Integer>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            list0.add(random.nextInt(100));
        }
        System.out.println(list0);//[65, 15, 13, 64, 26, 32, 1, 42, 78, 7]
        /*
         * 对集合进行自然排序，从小到大
		 */
        Collections.sort(list0);
        System.out.println(list0);//[1, 7, 13, 15, 26, 32, 42, 64, 65, 78]

        List<String> list1 = new ArrayList<String>();
        list1.add("Alive");
        list1.add("Rose");
        list1.add("Jack");
        list1.add("Noname");
        System.out.println(list1);//[Alive, Rose, Jack, Noname]
        Collections.sort(list1);
        /*
         *对集合进行自然排序，显而易见是按照首字母顺序排序
	   	 */
        System.out.println(list1);//[Alive, Jack, Noname, Rose]
    }

}
