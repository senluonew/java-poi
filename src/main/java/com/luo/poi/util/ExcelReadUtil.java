package com.luo.poi.util;


import com.luo.poi.model.ExceptionVo;
import com.luo.poi.model.RowExceptionVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author pudding
 * @version 1.0
 * @design
 * @date 2018\3\25 0025/22:46.
 * @see
 */
public class ExcelReadUtil extends ExcelProcessor {


    public static <T> List<T> readSheet(Sheet sheet, int initRowNum, int initColumnNum, List<String> titleList,
                                        List<String> fieldList, Class<T> type, List<ExceptionVo> exceptionVos) throws IOException {
        if (Objects.isNull(sheet)) {
            return null;
        }
        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }
        readTitle(sheet, initRowNum, initColumnNum, type, titleList, fieldList);

        List<T> dataList = new ArrayList<>();
        dataList.addAll(readData(sheet, initRowNum, initColumnNum, fieldList, type, exceptionVos));
        return dataList;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static List<String> readTitle(Sheet sheet, int initRowNum, int initColumnNum) {
        Row titleRow = getRow(sheet, initRowNum);
        List<String> titleList = new ArrayList<>();
        for (int i = initColumnNum; i < titleRow.getLastCellNum(); i++) {
            titleList.add((String) getCell(titleRow, i));
        }
        return titleList;
    }

    private static List<String> readTitle(Sheet sheet, int initRowNum, int initColumnNum, Class type,
                                          List<String> desTitleList, List<String> fieldList) {
        List<String> titleList = readTitle(sheet, initRowNum, initColumnNum);

        if (desTitleList == null) {
            desTitleList = parseTitle(type, fieldList);
        }

        int orginalSize = fieldList.size();
        for (String title : titleList) {
            int index = desTitleList.indexOf(title);
            if (index > -1) {
                fieldList.add(fieldList.get(index));
            } else {
                fieldList.add(null);
            }
        }
        for (int i = 0; i < orginalSize; i++) {
            fieldList.remove(0);
        }
        return titleList;
    }

    private static <T> List<T> readData(Sheet sheet, int initRowNum, int initColumnNum, List<String> fieldList, Class<T> type, List<ExceptionVo> exceptionVos) {
        List<T> dataList = new ArrayList<>();
        for (int i = initRowNum + 1; i <= sheet.getLastRowNum(); i++) {
            try {
                Row dataRow = getRow(sheet, i);
                int fieldIndex = 0;
                T t = type.newInstance();
                boolean flag = true;
                ExceptionVo exceptionVo = new ExceptionVo.Builder().row(i+1).rowExceptionVos(new ArrayList<>()).build();
                for (int j = initColumnNum; j < dataRow.getLastCellNum(); j++) {
                    String fieldName = fieldList.get(fieldIndex++);
                    Title title = parseSingleTitle(type, fieldName);
                    try {
                        Object value = convetParameterType(type, fieldName, getCell(dataRow, j), title);
                        if (Objects.isNull(value)) {
                            continue;
                        }
                        ReflectUtil.getSetter(type, fieldName, value.getClass()).invoke(t, value);
                    } catch (Exception e) {
                        flag = false;
                        RowExceptionVo rowExceptionVo = new RowExceptionVo.Builder().titleName(title.value())
                                .value(getCell(dataRow, j)).errorInfo(e.getMessage()).build();
                        exceptionVo.getRowExceptionVos().add(rowExceptionVo);
                    }
                }
                if (flag) {
                    dataList.add(t);
                }else {
                    exceptionVos.add(exceptionVo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    private static <T> Object convetParameterType(Class<T> type, String fieldName, Object srcValue, Title title) throws Exception {
        Method method = ReflectUtil.getGetter(type, fieldName);
        Class returnType = method.getReturnType();
        if(title.isNotNull() && (Objects.isNull(srcValue) || Objects.toString(srcValue).trim().isEmpty())){
            throw new Exception("数据为空");
        }
        if (returnType == int.class || returnType == Integer.class) {
            if (isNumber(srcValue)) {
                return (Double.valueOf(srcValue + "")).intValue();
            } else {
                throw new Exception("数据格式有误");
            }
        } else if (returnType == long.class || returnType == Long.class) {
            if (isNumber(srcValue)) {
                return ((Double) srcValue).longValue();
            } else {
                throw new Exception("数据格式有误");
            }
        } else if (returnType == String.class) {
            if (Objects.isNull(srcValue) || StringUtils.isEmpty(String.valueOf(srcValue))
                    || Objects.equals(String.valueOf(srcValue), "NULL") ||
                    Objects.equals(String.valueOf(srcValue), "null")) {
                return null;
            }
            try {
                BigDecimal one = new BigDecimal(srcValue + "");
                return one.toPlainString();
            } catch (Exception e) {
                return String.valueOf(srcValue);
            }
        } else if (returnType == double.class || returnType == Double.class) {
            if (isNumber(srcValue)) {
                return Double.valueOf(srcValue + "");
            } else {
                throw new Exception("数据格式有误");
            }
        }
        return null;
    }


    public static boolean isNumber(Object obj) {
        if (obj instanceof Number) {
            return true;
        } else if (obj instanceof String) {
            try {
                Double.parseDouble((String) obj);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


    // 基础方法/////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static Row getRow(Sheet sheet, int index) {
        return sheet.getRow(index);
    }

    private static Object getCell(Row row, int index) {
        Cell cell = row.getCell(index);
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            default:
                return null;
        }
    }

}
