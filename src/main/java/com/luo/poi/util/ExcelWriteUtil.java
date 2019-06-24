package com.luo.poi.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author pudding
 * @version 1.0
 * @design
 * @date 2018\3\25 0025/18:54.
 * @see
 */
public class ExcelWriteUtil extends ExcelProcessor {

    /**
     * 每个sheet中存放的row的数量，默认为100000，超过默认值，自动新建sheet
     */
    private static final int DEFAULT_SHEET_SIZE = 100000;

    private static final String DEFAULT_SHEET_NAME = "sheet";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param data      数据
     * @param sheetSize sheet行数
     * @param fileType  文件类型（不为空则返回数据流，不存本地）
     * @return
     * @throws IOException
     */
    public static int write(ServletOutputStream out, Collection data, Integer sheetSize, String fileType) throws IOException {
        return write(out, null, sheetSize, 0, 0, data, fileType);
    }


    public static <E> int write(ServletOutputStream out, String sheetName, Integer sheetSize, int initRowNum, int initColumnNum, Collection<E> data, String fileType) throws IOException {
        return write(out, sheetName, sheetSize, initRowNum, initColumnNum, null, null, data, fileType);
    }


    public static int write(ServletOutputStream out, String sheetName, Integer sheetSize, int initRowNum, int initColumnNum, List<String> titleList, List<String> fieldList, Collection data, String fileType) throws IOException {
        if (fileType == null) {
            throw new IllegalArgumentException("fileType must not be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        Workbook workbook = createExcel(fileType);
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

        write(workbook, out);
        workbook.close();
        return 0;
    }

    /**
     * 解析sheet的名称，当有多个sheet时，每个sheet不同
     *
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
        if (num > 1) {
            for (int i = 1; i < num; i++) {
                sheetNameList.add(sheetName + i);
            }
        }
        return sheetNameList;
    }

    private static List<List<Object>> parseData(Collection datas, List<String> fieldList) {
        List<List<Object>> dataListList = new ArrayList<>();
        datas.forEach(data -> {
            Map<String, Object> dataMap = ReflectUtil.getFieldValue(data);
            List<Object> dataList = new ArrayList<>();
            fieldList.forEach(field -> {
                dataList.add(dataMap.get(field));
            });
            dataListList.add(dataList);
        });
        return dataListList;
    }

    /**
     * 写入标题
     *
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
     *
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
     *
     * @param fileType
     */
    private static Workbook createExcel(String fileType) throws IOException {
        if (fileType == null) {
            return null;
        }
        fileType = fileType.trim();

        Workbook workbook;
        if (fileType.endsWith(XLS)) {
            workbook = new HSSFWorkbook();
        } else if (fileType.endsWith(XLSX)) {
            workbook = new XSSFWorkbook();
        } else {
            throw new IllegalArgumentException("the path suffix is not excel file name, path suffix must be .xls or .xlsx");
        }
        return workbook;
    }

    private static Sheet createSheel(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
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

    private static void write(Workbook workbook, ServletOutputStream out) throws IOException {
        workbook.write(out);
    }

    public static void main(String[] args) throws Exception {
        try {
            System.out.println(1);
        }catch (Exception e){
            throw new Exception("111");
        }finally {
            System.out.println(222);
        }
    }
}
