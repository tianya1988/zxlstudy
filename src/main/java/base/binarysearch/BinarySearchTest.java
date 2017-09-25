package base.binarysearch;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by jason on 17-9-20.
 */
public class BinarySearchTest {

    @Test
    public void testBinarySearch() {
        ArrayList<AssetInfo> assetInfoList = new ArrayList<AssetInfo>(12000);

        AssetInfo assetInfo = new AssetInfo();
        assetInfo.setStartIp(1l);
        assetInfo.setEndIp(100l);
        assetInfo.setUnitName("1");
        assetInfo.setDepartmentName("1");
        assetInfoList.add(assetInfo);


        AssetInfo assetInfo2 = new AssetInfo();
        assetInfo2.setStartIp(101l);
        assetInfo2.setEndIp(200l);
        assetInfo2.setUnitName("2");
        assetInfo2.setDepartmentName("2");
        assetInfoList.add(assetInfo2);

        AssetInfo assetInfo3 = new AssetInfo();
        assetInfo3.setStartIp(201l);
        assetInfo3.setEndIp(300l);
        assetInfo3.setUnitName("3");
        assetInfo3.setDepartmentName("3");
        assetInfoList.add(assetInfo3);

        AssetInfo assetInfo4 = new AssetInfo();
        assetInfo4.setStartIp(301l);
        assetInfo4.setEndIp(400l);
        assetInfo4.setUnitName("4");
        assetInfo4.setDepartmentName("4");
        assetInfoList.add(assetInfo4);

        AssetInfo temp = binarySearch(assetInfoList, 250l);
        System.out.println(temp.getUnitName());


    }

    private AssetInfo binarySearch(ArrayList<AssetInfo> assetInfoList, Long ipLong) {
        int low = 0;
        int high = assetInfoList.size() - 1;

        while (low <= high) {
            int middle = low + ((high - low) >> 1);
            AssetInfo assetInfo = assetInfoList.get(middle);
            if (ipLong >= assetInfo.getStartIp() && ipLong <= assetInfo.getEndIp()) {
                return assetInfo;
            } else {
                if (ipLong < assetInfo.getStartIp()) {
                    high = middle - 1;
                } else {
                    low = middle + 1;
                }
            }

        }
        return null;
    }
}
