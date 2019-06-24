package com.luo.poi.utilnew;

import com.luo.poi.model.Test;
import com.luo.poi.util.ExcelProcessor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author pudding
 * @version 1.0
 * @design
 * @date 2018\3\25 0025/18:54.
 * @see
 */
public class ExcelWriteUtilNew extends ExcelProcessor {

    /**
     * 每个sheet中存放的row的数量，默认为200，超过默认值，自动新建sheet
     */
    private static final int DEFAULT_SHEET_SIZE = 200;

    private static final String DEFAULT_SHEET_NAME = "sheet";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static int write(String path, Object... data) throws IOException {
        return write(path, null, null, 0, 0, data);
    }

    public static int write(String path, Collection data) throws IOException {
        return write(path, null, null, 0, 0, data);
    }

    public static int write(String path, String sheetName, Object... data) throws IOException {
        return write(path, sheetName, null, 0, 0, data);
    }

    public static int write(String path, String sheetName, Collection data) throws IOException {
        return write(path, sheetName, null, 0, 0, data);
    }

    /**
     * 将数据写入excel表格
     * @param path excel表格的路径
     * @param sheetSize 每个sheet存放的数据的行数
     * @param initRowNum 从第几行开始填入数据
     * @param initColumnNum 从第几列开始填入数据
     * @param data 数据的数组
     * @return
     * @throws IOException
     */
    public static int write(String path, String sheetName, Integer sheetSize, int initRowNum, int initColumnNum, Object... data) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        return write(path, sheetName, sheetSize, initRowNum, initColumnNum, Arrays.asList(data));
    }

    public static <E> int write(String path, String sheetName, Integer sheetSize, int initRowNum, int initColumnNum, Collection<E> data) throws IOException {
       return write(path, sheetName, sheetSize, initRowNum, initColumnNum, null, null, data);
    }

    public static int write(String path, List<String> titleList, List<String> fieldList, Collection data) throws IOException {
        return write(path, null, null, 0, 0, titleList, fieldList, data);
    }

    public static int write(String path, String sheetName, List<String> titleList, List<String> fieldList, Collection data) throws IOException {
        return write(path, sheetName, null, 0, 0, titleList, fieldList, data);
    }

    public static int write(String path, String sheetName, Integer sheetSize, List<String> titleList, List<String> fieldList, Collection data) throws IOException {
        return write(path, sheetName, sheetSize, 0, 0, titleList, fieldList, data);
    }

    public static int write(String path, String sheetName, Integer sheetSize, int initRowNum, int initColumnNum, List<String> titleList, List<String> fieldList, Collection data) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        Workbook workbook = createExcel(path);
        sheetSize = sheetSize != null ? sheetSize : DEFAULT_SHEET_SIZE;
        List<String> sheetNameList = parseSheetName(sheetName, sheetSize, data.size());
        if (titleList == null || fieldList == null) {
            fieldList = new ArrayList<>();
            titleList = parseTitle(data.iterator().next().getClass(), fieldList);
        }
        List<List<Object>> dataList = parseData(data, fieldList);

        for (String actualSheetName : sheetNameList) {
            Sheet sheet = createSheel(workbook, actualSheetName);
            writeTitle(sheet, initRowNum, initColumnNum, titleList);
            writeData(sheet, initRowNum, initColumnNum, dataList, sheetSize);
        }

        write(workbook, path);
        workbook.close();
        return 0;
    }

    /**
     * 解析sheet的名称，当有多个sheet时，每个sheet不同
     * @param sheetName
     * @param sheetSize
     * @param dataSize
     * @return
     */
    private static List<String> parseSheetName(String sheetName, Integer sheetSize, int dataSize) {
        if (sheetName == null) {
            sheetName = DEFAULT_SHEET_NAME;
        }
        int num = dataSize % sheetSize == 0 ? dataSize / sheetSize : dataSize / sheetSize + 1;
        List<String> sheetNameList = new ArrayList<>();
        sheetNameList.add(sheetName);
        if (num > 1){
            for (int i = 1; i < num; i++) {
                sheetNameList.add(sheetName + i);
            }
        }
        return sheetNameList;
    }

    private static List<List<Object>> parseData(Collection datas, List<String> fieldList) {
        List<List<Object>> dataListList = new ArrayList<>();
//        datas.forEach(data -> {
//            Map<String, Object> dataMap = ReflectUtil.getFieldValue(data);
//            List<Object> dataList = new ArrayList<>();
//            fieldList.forEach(field -> {
//                dataList.add(dataMap.get(field));
//            });
//            dataListList.add(dataList);
//        });      //1.8
        return dataListList;
    }

    /**
     * 写入标题
     * @param initRowNum
     * @param initColumnNum
     * @param titleList
     */
    private static void writeTitle(Sheet sheet, int initRowNum, int initColumnNum, List<String> titleList) {
        Row row = createRow(sheet, initRowNum);
        for (int i = initColumnNum; i < initColumnNum + titleList.size(); i++) {
            createCell(row, i, titleList.get(i));
        }
    }

    /**
     * 写入数据
     * @param <E>
     * @param sheet
     * @param initRowNum
     * @param initColumnNum
     * @param dataList
     * @param sheetSize
     */
    private static <E> void writeData(Sheet sheet, int initRowNum, int initColumnNum, List<List<Object>> dataList, Integer sheetSize) {
        int size = dataList.size();
        for (int i = 0; i < (sheetSize > size ? size : sheetSize); i++) {
            Row row = createRow(sheet, initRowNum + 1 + i);
            List<Object> datas = dataList.remove(0);
            for (int j = 0; j < datas.size(); j++) {
                createCell(row, initColumnNum + j, datas.get(j));
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 创建excel文件
     * @param path
     */
    private static Workbook createExcel(String path) throws IOException {
        if (path == null) {
            return null;
        }
        path = path.trim();

        Workbook workbook;
        if (path.endsWith(XLS)) {
            workbook = new HSSFWorkbook();
        } else if (path.endsWith(XLSX)) {
            workbook = new XSSFWorkbook();
        } else {
            throw new IllegalArgumentException("the path suffix is not excel file name, path suffix must be .xls or .xlsx");
        }
        return workbook;
    }

    private static Sheet createSheel(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    private static List<Sheet> createSheel(Workbook workbook, List<String> sheetNameList) {
        List<Sheet> sheetList = new ArrayList<>();
//        sheetNameList.forEach(sheetName -> {
//            sheetList.add(createSheel(workbook, sheetName));
//        });      //1.8
        return sheetList;
    }

    private static Row createRow(Sheet sheet, int rowIndex) {
        return sheet.createRow(rowIndex);
    }

    private static Cell createCell(Row row, int cellIndex, Object cellValue) {
        Cell cell = row.createCell(cellIndex);
        if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        } else if (cellValue instanceof Number) {
            cell.setCellValue(((Number) cellValue).doubleValue());
        }
        return cell;
    }

    private static void write(Workbook workbook, String path) throws IOException {
        OutputStream outputStream = new FileOutputStream(new File(path));
        workbook.write(outputStream);
    }

    public static void main(String[] args) throws Exception {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            //tests.add(new Test(i, "这是第" + i + "条数据！", "199999999", i * 1000000L));
        }
        List<String> titileList = new ArrayList<>();
        titileList.add("ID");
        titileList.add("不是描述");
        List<String> fieldList = new ArrayList<>();
        fieldList.add("id");
        fieldList.add("descri");

        write("C:\\Users\\Administrator\\Desktop\\test1.xlsx", "我的表格", tests);
    }
}
