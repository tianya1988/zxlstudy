package collections;

/**
 * Created by jason on 21-5-27.
 */

public class Cell implements Comparable<Cell> {//哪个类实现Comparable接口泛型就写哪个类
    private int x;
    private int y;

    public Cell(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Cell [x=" + x + ", y=" + y + "]";
    }

    @Override
    public int compareTo(Cell o) {//自定义排序逻辑
        // TODO Auto-generated method stub
        return this.x - o.x;//以传入的Cell的横坐标由小到大进行排序
    }

}
