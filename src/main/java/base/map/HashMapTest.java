package base.map;

import java.util.HashMap;

/**
 * Created by jason on 17-10-13.
 */
public class HashMapTest {
    public static void main(String[] args) {
        long size = 1400000;
        HashMap<Long, IpBean> ipHashMap = new HashMap<Long, IpBean>((int)size);

        for (long i = 0; i < size; i++) {
            IpBean ipBean = new IpBean();
            ipBean.setIp(i);
            ipBean.setMask(32);
            ipBean.setDescription("中国石油西沙屯桥西我也不知道该怎么写了");
            ipHashMap.put(i, ipBean);
        }
        IpBean ipBean = new IpBean();
        ipBean.setIp(10001l);
        ipBean.setMask(32);
        ipBean.setDescription("中国石油西沙屯桥西我也不知道该怎么写了");
        ipHashMap.put(10001l, ipBean);


        System.out.println(ipHashMap.size());



    }
}
