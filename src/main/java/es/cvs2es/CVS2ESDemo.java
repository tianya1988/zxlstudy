package es.cvs2es;

import com.alibaba.fastjson.JSONObject;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


public class CVS2ESDemo {
    public static void main(String[] args) {
        File file = new File("/home/jason/Desktop/市政路桥/210324-1.csv");
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);

        CsvContainer csv = null;
        try {
            csv = csvReader.read(file, Charset.forName("GBK"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> headers = csv.getHeader();
        for (String header : headers) {
            System.out.println(header);
        }
        for (CsvRow row : csv.getRows()) {

/*            List<String> fields = row.getFields();
            for (String field : fields) {
                System.out.println(field);
            }
            break;*/
//            System.out.println("First column of line: " + row.getField("日 期"));

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", row.getField("日 期"));
            jsonObject.put("dateTime", row.getField("时 刻"));
            jsonObject.put("discNum", row.getField("盘号"));
            jsonObject.put("ratioNum", row.getField("配比号"));
            jsonObject.put("carNum", row.getField("车 号"));
            jsonObject.put("customerNum", row.getField("客户号"));
            jsonObject.put("aggregate6", row.getField("骨料6"));
            jsonObject.put("aggregate5", row.getField("骨料5"));
            jsonObject.put("aggregate4", row.getField("骨料4"));
            jsonObject.put("aggregate3", row.getField("骨料3"));
            jsonObject.put("aggregate2", row.getField("骨料2"));
            jsonObject.put("aggregate1", row.getField("骨料1"));
            jsonObject.put("stonePowder2", row.getField("石粉2"));
            jsonObject.put("stonePowder1", row.getField("石粉1"));
            jsonObject.put("asphalt", row.getField("沥青"));
            jsonObject.put("recycledMaterial", row.getField("再生料"));
            jsonObject.put("additive", row.getField("添加剂"));
            jsonObject.put("additive1", row.getField("添加剂1"));
            jsonObject.put("additive2", row.getField("添加剂2"));
            jsonObject.put("additive3", row.getField("添加剂3"));
            jsonObject.put("totalKG", row.getField("合计kg"));
            jsonObject.put("Warehouse1Temperature", row.getField("1仓温度"));
            jsonObject.put("mixtureTemperature", row.getField("混合料温度"));
            jsonObject.put("dusterEntranceTemperature", row.getField("除尘器入口温度"));
            jsonObject.put("asphaltTemperature", row.getField("沥青温度"));
            jsonObject.put("aggregateTemperature", row.getField("骨料温度"));

//            System.out.println(jsonObject.toJSONString());

            ESProcessor.indexData("scsc-test10", jsonObject);
        }
        ESProcessor.getProcessor().flush();
        ESProcessor.closeProcessor();
    }
}
