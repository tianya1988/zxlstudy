package base.sign;

/**
 * Created by jason on 19-10-12.
 */
public class Extension {

    private String oid;

    private boolean critical;

    private byte[] value;

    public String getOid() {
        return oid;
    }

    public byte[] getValue() {
        return value;
    }

    public boolean isCritical() {
        return critical;
    }
}
