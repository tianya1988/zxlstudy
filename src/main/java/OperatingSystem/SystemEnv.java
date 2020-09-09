package OperatingSystem;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by jason on 20-9-8.
 */
public class SystemEnv {
    public static void main(String[] args) {

        Properties props = System.getProperties();
        System.out.println("操作系统的名称：" + props.getProperty("os.name"));
        System.out.println("操作系统的版本号：" + props.getProperty("os.version"));
        System.out.println("操作系统的构架：" + props.getProperty("os.arch"));
    }
}
