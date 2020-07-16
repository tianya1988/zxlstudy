
package excel;


import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ReadExcelDemoXLSX {

    public static void main(String[] args) throws IOException {
        readExcel("/home/jason/Desktop/APPbushu/jiaotong/screen/ftp-data/osscan_excel_info2.xlsx");
    }

    public static void readExcel(String path) throws IOException {
        File file = new File(path);
        FileInputStream fileInputStream = null;
        Workbook workBook = null;

        if (file.exists()) {
            fileInputStream = new FileInputStream(file);
            // workBook = WorkbookFactory.create(fileInputStream);
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet xssfSheet = xssfWorkbook.getSheet("漏洞详情");


            int numberOfSheets = xssfWorkbook.getNumberOfSheets();
            System.out.println("number of sheets is : " + numberOfSheets);
            System.out.println("number of rows is : " + xssfSheet.getPhysicalNumberOfRows());

            // 此list是为了能够记录合并的单元格,设计的数据结构; 表格中总行数为拆分后的单元格(与表行的行号对应).
            List<HashMap<Integer, String>> list = new LinkedList<HashMap<Integer, String>>();
            int physicalNumberOfRows = xssfSheet.getPhysicalNumberOfRows();
            for (int rowNum = 0; rowNum < physicalNumberOfRows; rowNum++) {
                XSSFRow row = xssfSheet.getRow(rowNum);
                int numberOfCells = row.getPhysicalNumberOfCells();

                HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
                for (int column = 0; column < numberOfCells; column++) {
                    if (StringUtils.isEmpty(row.getCell(column).getStringCellValue())) {
                        /**
                         * 合并后的单元格,在实际读取的时候,相当于是首行的值,其他行值为空
                         * 当遇到空的时候,应该从上一行记录中读取值
                         */
                        hashMap.put(column, list.get(rowNum -1).get(column));
                    } else {
                        hashMap.put(column, row.getCell(column).getStringCellValue());
                    }
                }

                list.add(hashMap);
            }
            System.out.println(list.size());
        }
    }
}

