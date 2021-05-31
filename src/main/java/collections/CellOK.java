package collections;

/**
 * Created by jason on 21-5-27.
 */

public class CellOK {
    private int x;
    private int y;

    public CellOK(int x, int y) {
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

}
