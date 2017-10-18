package base.map;

/**
 * Created by jason on 17-10-13.
 */
public class IpBean {
    private long ip;
    private int mask;
    private String description;

    public long getIp() {
        return ip;
    }

    public void setIp(long ip) {
        this.ip = ip;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IpBean ipBean = (IpBean) o;

        if (ip != ipBean.ip) return false;
        if (mask != ipBean.mask) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (ip ^ (ip >>> 32));
        result = 31 * result + mask;
        return result;
    }
}
