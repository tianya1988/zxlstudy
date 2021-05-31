package collections;

import java.util.Comparator;

/**
 * Created by jason on 21-5-27.
 */
public class MyComparator implements Comparator<CellOK> {
    @Override
    public int compare(CellOK o1, CellOK o2) {
        return o1.getX() - o2.getX();
    }
}
