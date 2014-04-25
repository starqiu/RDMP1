package ustc.sse.datamining;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

    private OutputStream outputStream = null;
    private String       sheetName    = "sheet1";
    private XSSFWorkbook workbook;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * @param fileOutputStream
     * @param forName
     * @throws IOException
     */
    public ExcelWriter(String filePath) throws IOException {
        workbook = new XSSFWorkbook();
        this.outputStream = new FileOutputStream(filePath);
    }

    public void close() {
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeRecord(String[] strings) throws IOException {
        int sheetNum = workbook.getNumberOfSheets();
        XSSFSheet sheet = null;
        if (sheetNum == 0) {
            sheet = workbook.createSheet();
            workbook.setSheetName(0, sheetName);
        } else {
            sheet = workbook.getSheetAt(0);
        }
        XSSFCell cell = null;
        XSSFCellStyle cs = null;
        XSSFRichTextString xssfValue = null;
        int rowNum = sheet.getLastRowNum();
        rowNum++;
        XSSFRow row = sheet.createRow(rowNum);
        for (int n = 0; n < strings.length; n++) {// 写出列
            cell = row.createCell(n);
            cs = cell.getCellStyle();
            cs.setFillPattern(XSSFCellStyle.ALIGN_GENERAL);
            cs.setWrapText(true);
            cs.setVerticalAlignment(XSSFCellStyle.ALIGN_LEFT);
            cell.setCellStyle(cs);
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            xssfValue = new XSSFRichTextString(strings[n]);
            cell.setCellValue(xssfValue);
        }
    }
}
