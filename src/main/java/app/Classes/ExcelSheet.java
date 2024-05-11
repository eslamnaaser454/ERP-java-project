package app.Classes;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelSheet {
    private String path;
    private File file;
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet xssfSheet;
    private FileOutputStream fileOutputStream;
    public ExcelSheet(String path,String fileName){
        Date date = new Date();
        this.path = path+"\\"+fileName+date.getYear()+"-"+date.getMonth()+"-"+date.getDay()+"-"+date.getHours()+"-"+date.getMinutes()+"-"+date.getSeconds()+"-"+date.getTimezoneOffset()+".xlsx";
    }


    public List<Map<Integer,String>> Read(int start) throws IOException, InvalidFormatException {
        file = new File(path);
        xssfWorkbook = new XSSFWorkbook(file); //i use this class to do access for this sheet
        //xssfSheet sheet=xwb.getSheet("sheet1");// here i want to access sheet1 in my file
        List<Map<Integer, String>> data = new ArrayList<>();
        int SheetsNumer = xssfWorkbook.getNumberOfSheets();
        int Rows;
        int Coulmns;
        for (int i = 0; i < SheetsNumer; i++) {
            xssfSheet = xssfWorkbook.getSheetAt(i);
            Rows = xssfSheet.getPhysicalNumberOfRows();
            Coulmns = xssfSheet.getRow(0).getPhysicalNumberOfCells();
            System.out.println("Sheet " + i + " " + xssfSheet.getSheetName());

            for (int row = start; row < Rows; row++) {
                Map<Integer, String> map = new HashMap<>();
                for (int column = 0; column < Coulmns; column++) {
                    xssfSheet.getRow(row).getCell(column);
                    Cell cell = xssfSheet.getRow(row).getCell(column);
                    String cellValue = "";
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                cellValue = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                DataFormatter dataFormatter = new DataFormatter();
                                cellValue = dataFormatter.formatCellValue(cell);
                                break;
                            // Handle other cell types as needed
                        }
                    }
                    map.put(column, cellValue);
                }
                data.add(map);
            }
        }
        return data;
    }






    public void write (List<Map<Integer,String>> list) throws IOException{
        xssfWorkbook = new XSSFWorkbook();
        xssfSheet = xssfWorkbook.createSheet("sheet");
        int Row=list.size()   ;

        for (int row = 0; row < Row; row++) {
            XSSFRow xssfRow = xssfSheet.createRow(row);
            Map<Integer, String> rowData = list.get(row);
            for (int column = 0; column < rowData.size(); column++) {
                XSSFCell xssfCell = xssfRow.createCell(column);
                String cellValue = rowData.get(column);
                xssfCell.setCellValue(cellValue);
            }
        }

        fileOutputStream = new FileOutputStream(path);
        xssfWorkbook.write(fileOutputStream);
        fileOutputStream.close();
    }



}
