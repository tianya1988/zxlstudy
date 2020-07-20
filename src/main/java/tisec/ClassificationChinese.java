package tisec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jason on 20-7-17.
 */
public class ClassificationChinese {
    public static void main(String[] args) throws IOException {
        readExcel("/home/jason/Desktop/suricata/ET-rules-types.xlsx");
    }

    public static void readExcel(String path) throws IOException {
        File file = new File(path);

        if (file.exists()) {
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream(file);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet xssfSheet = xssfWorkbook.getSheet("工作表1");


            int numberOfSheets = xssfWorkbook.getNumberOfSheets();
            System.out.println("number of sheets is : " + numberOfSheets);
            System.out.println("number of rows is : " + xssfSheet.getPhysicalNumberOfRows());

            FileOutputStream outputStream = new FileOutputStream(new File("/home/jason/Desktop/suricata/classification.config.zh"));

            int physicalNumberOfRows = xssfSheet.getPhysicalNumberOfRows();
            for (int rowNum = 1; rowNum < physicalNumberOfRows; rowNum++) {
                XSSFRow row = xssfSheet.getRow(rowNum);

                //"config classification: "
                String typeName = row.getCell(0).getStringCellValue();
                // 读到空行了就结束
                if (StringUtils.isEmpty(typeName)) {
                    break;
                }
                String enDescription = row.getCell(2).getStringCellValue();
                int level = (int) row.getCell(3).getNumericCellValue();


                IOUtils.write("config classification: " + typeName + "," + enDescription + "," + level, outputStream);
                IOUtils.write("\r\n", outputStream);

            }

            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(fileInputStream);
        }
    }
}
