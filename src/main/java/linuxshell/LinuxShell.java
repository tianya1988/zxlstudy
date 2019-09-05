package linuxshell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 19-9-5.
 */
public class LinuxShell {

    public static void main(String[] args) {

        String directory = "/home/jason/Desktop/es/";
//        String indexsStr = "gly-scsc-alarm-20190903,bjg-scsc-alarm-20190905,bjg-scsc-alarm-20190902,zgzx-scsc-alarm-20190816,chj-scsc-alarm-20190820,bjg-scsc-alarm-20190903,gly-scsc-alarm-20190820,gly-scsc-alarm-20190821,bjg-scsc-alarm-20190818,bjg-scsc-alarm-20190824,gly-scsc-alarm-20190822,zgzx-scsc-alarm-20190905,bjg-scsc-alarm-20190817,bjg-scsc-alarm-20190826,bjg-scsc-alarm-20190831,bjg-scsc-alarm-20190819,zgzx-scsc-alarm-20190902,bjg-scsc-alarm-20190825,gly-scsc-alarm-20190816,zgzx-scsc-alarm-20190824,zgzx-scsc-alarm-20190817,gly-scsc-alarm-20190813,chj-scsc-alarm-20190826,zgzx-scsc-alarm-20190831,gly-scsc-alarm-20190830,chj-scsc-alarm-20190819,bjg-scsc-alarm-20190901,zgzx-scsc-alarm-20190820,chj-scsc-alarm-20190905,zgzx-scsc-alarm-20190830,chj-scsc-alarm-20190821,bjg-scsc-alarm-20190822,zgzx-scsc-alarm-20190903,bjg-scsc-alarm-20190823,chj-scsc-alarm-20190825,other-scsc-alarm-20190821,chj-scsc-alarm-20190904,chj-scsc-alarm-20190902,zgzx-scsc-alarm-20190825,chj-scsc-alarm-20190828,zgzx-scsc-alarm-20190815,zgzx-scsc-alarm-20190826,chj-scsc-alarm-20190903,zgzx-scsc-alarm-20190904,other-scsc-alarm-20190820,chj-scsc-alarm-20190827,zgzx-scsc-alarm-20190823,bjg-scsc-alarm-20190904,chj-scsc-alarm-20190831,zgzx-scsc-alarm-20190827,zgzx-scsc-alarm-20190821,zgzx-scsc-alarm-20190901,chj-scsc-alarm-20190823,gly-scsc-alarm-20190812,bjg-scsc-alarm-20190830,bjg-scsc-alarm-20190827,chj-scsc-alarm-20190824,zgzx-scsc-alarm-20190822,chj-scsc-alarm-20190830,gly-scsc-alarm-20190826,zgzx-scsc-alarm-20190829,chj-scsc-alarm-20190822,bjg-scsc-alarm-20190821,bjg-scsc-alarm-20190820,zgzx-scsc-alarm-20190819,gly-scsc-alarm-20190819";
        String indexsStr = "zgzx-pro-http-20190414";
        String[] indexs = indexsStr.split(",");
        for (String index : indexs) {

            String cmd1 = "/home/jason/soft/softsrc/elasticsearch-dump/bin/elasticdump" +
                    " --input=http://10.30.111.1:9200/" + index +
                    " --output=" + directory + index + "_mapping.json" +
                    " --type=mapping";

            String cmd2 = "/home/jason/soft/softsrc/elasticsearch-dump/bin/elasticdump" +
                    " --input=http://10.30.111.1:9200/" + index +
                    " --output=" + directory + index + ".json" +
                    " --type=data";

            executeLinuxCmd(cmd1);
            executeLinuxCmd(cmd2);
        }


    }

    private static List<String> executeLinuxCmd(String cmd) {
        System.out.println("get cmd job : " + cmd);
        Runtime run = Runtime.getRuntime();
        try {
            //Process process = run.exec(cmd);
            Process process = run.exec(new String[]{"/bin/sh", "-c", cmd});
            InputStream in = process.getInputStream();//获取子进程的输入流
            BufferedReader bs = new BufferedReader(new InputStreamReader(in));
            List<String> list = new ArrayList<String>();
            String result = null;
            while ((result = bs.readLine()) != null) {
                System.out.println("job result [" + result + "]");
                list.add(result);
            }
            in.close();
            // process.waitFor();
            process.destroy();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
