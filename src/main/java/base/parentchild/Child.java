package base.parentchild;

/**
 * Created by jason on 17-1-9.
 */
public class Child extends Parent {
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void main(String[] args) {
        Child child = new Child();
        child.age = 10;
        child.phone = "sfa";
        //
    }
}
