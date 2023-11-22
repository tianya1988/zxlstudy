package base.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by jason on 17-6-27.
 */
public class SearchDir {
    public static void main(String[] args) throws Exception {
        File dir = new File("E:\\工地综合数据集\\Annotations");

        File[] files = dir.listFiles();

        HashMap<String, String> allMap = new HashMap<>();


        for (File file : files) {
//            System.out.println(file.getName());
            allMap.put(file.getName(), file.getAbsolutePath());

        }

//        File dir2 = new File("F:\\AI数据集\\外网\\JPEGImages");
//        File[] files2 = dir2.listFiles();
//
//        for (File file : files2) {
//            allMap.remove(file.getName());
//        }

        File dir3 = new File("E:\\ConstructionSiteDataset\\Annotations");
        File[] files3 = dir3.listFiles();

        for (File file : files3) {
            allMap.remove(file.getName());
        }

        Set<String> names = allMap.keySet();

        for (String name : names) {
            System.out.println(name);

            File f1 = new File("E:\\工地综合数据集\\Annotations\\"+name);
            // 目标文件
            File f2 = new File("F:\\AI数据集\\外网\\Annotations\\"+name);

            try {
                // 这个 win10系统 大概是 3.50G 的 复制过程 花了 156 秒 == 2 分6 秒
                CopyFile.copyFile(f1, f2);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        System.out.println(names.size());


    }
}
