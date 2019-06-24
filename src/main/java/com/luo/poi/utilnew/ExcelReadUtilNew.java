package com.luo.poi.utilnew;


import com.alibaba.fastjson.JSON;
import com.ancun.http.HttpStatus;
import com.btjf.common.http.HttpHandleFactory;
import com.btjf.common.http.HttpResult;
import com.google.common.collect.Maps;
import com.luo.poi.model.Test;
import com.luo.poi.util.ExcelProcessor;
import com.luo.poi.util.ReflectUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pudding
 * @version 1.0
 * @design
 * @date 2018\3\25 0025/22:46.
 * @see
 */
public class ExcelReadUtilNew extends ExcelProcessor {

    public static List<Map<String, Object>> read(String path) throws IOException {
        return read(path, 0, 0);
    }

    public static List<Map<String, Object>> read(String path, int initRowNum, int initColumnNum) throws IOException {
        Workbook workbook = getWorkbook(path);
        Sheet sheet = getSheet(workbook, 0);
        List<String> titleList = readTitle(sheet, initRowNum, initColumnNum);

        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = getSheet(workbook, i);
            dataList.addAll(readData(sheet, initRowNum, initColumnNum, titleList));
        }

        workbook.close();
        return dataList;
    }

    public static <T> List<T> read(String path, Class<T> type) throws IOException {
        return read(path, 0, 0, null, null, type);
    }

    public static <T> List<T> read(String path, int initRowNum, int initColumnNum, Class<T> type) throws IOException {
        return read(path, initRowNum, initColumnNum, null, null, type);
    }

    public static <T> List<T> read(String path, List<String> titleList, List<String> fieldList, Class<T> type)
            throws IOException {
        return read(path, 0, 0, titleList, fieldList, type);
    }

    public static <T> List<T> read(String path, int initRowNum, int initColumnNum, List<String> titleList,
                                   List<String> fieldList, Class<T> type) throws IOException {
        Workbook workbook = getWorkbook(path);

        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }
        Sheet sheet = getSheet(workbook, 0);
        readTitle(sheet, initRowNum, initColumnNum, type, titleList, fieldList);

        List<T> dataList = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = getSheet(workbook, i);
            dataList.addAll(readData(sheet, initRowNum, initColumnNum, fieldList, type));
        }

        workbook.close();
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

    private static List<Map<String, Object>> readData(Sheet sheet, int initRowNum, int initColumnNum, List<String> titleList) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = initRowNum + 1; i <= sheet.getLastRowNum(); i++) {
            Map<String, Object> dataMap = new HashMap<>();
            Row dataRow = getRow(sheet, i);
            int k = 0;
            for (int j = initColumnNum; j < dataRow.getLastCellNum(); j++) {
                dataMap.put(titleList.get(k++), getCell(dataRow, j));
            }
            dataList.add(dataMap);
        }
        return dataList;
    }

    private static <T> List<T> readData(Sheet sheet, int initRowNum, int initColumnNum, List<String> fieldList, Class<T> type) {
        List<T> dataList = new ArrayList<>();
        for (int i = initRowNum + 1; i <= sheet.getLastRowNum(); i++) {
            try {
                Row dataRow = getRow(sheet, i);
                int fieldIndex = 0;
                T t = type.newInstance();
                dataList.add(t);
                for (int j = initColumnNum; j < dataRow.getLastCellNum(); j++) {
                    String fieldName = fieldList.get(fieldIndex++);
                    Object value = convetParameterType(type, fieldName, getCell(dataRow, j));
//                    if(Objects.isNull(value)){
//                        continue;
//                    }       //1.8
                    ReflectUtil.getSetter(type, fieldName, value.getClass()).invoke(t, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    private static <T> Object convetParameterType(Class<T> type, String fieldName, Object srcValue) {
        Method method = ReflectUtil.getGetter(type, fieldName);
        Class returnType = method.getReturnType();
        if (returnType == int.class || returnType == Integer.class) {
            if (isNumber(srcValue)) {
                return (Double.valueOf(srcValue + "")).intValue();
            }
            return null;
        } else if (returnType == long.class || returnType == Long.class) {
            return ((Double) srcValue).longValue();
        } else if (returnType == String.class) {
//            if(Objects.isNull(srcValue) || StringUtils.isEmpty(String.valueOf(srcValue))
//                    || Objects.equals(String.valueOf(srcValue),"NULL") ||
//                    Objects.equals(String.valueOf(srcValue),"null")){
//                return null;
//            }      //1.8
            try {
                BigDecimal one = new BigDecimal(srcValue + "");
                return one.toPlainString();
            } catch (Exception e) {
                return String.valueOf(srcValue);
            }
        } else if (returnType == double.class || returnType == Double.class) {
            if (isNumber(srcValue)) {
                return Double.valueOf(srcValue + "");
            }
            return null;
        }
        return null;
    }


    /**
     * 将数据保留两位小数
     */
    public static Double getTwoDecimal(Double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String string = dFormat.format(num);
        return Double.valueOf(string);
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

    private static Workbook getWorkbook(String path) {
        Workbook workbook = null;
        FileInputStream fileInputStream = null;
        InputStream inputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            inputStream = NPOIFSFileSystem.createNonClosingInputStream(fileInputStream);
            if (path.endsWith(XLS)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (path.endsWith(XLSX)) {
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return workbook;
    }

    private static Sheet getSheet(Workbook workbook, int index) {
        return workbook.getSheetAt(index);
    }

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

    public static void main(String[] args) throws Exception {
//        List<String> titileList = new ArrayList<>();
//        titileList.add("标编号");
//        titileList.add("标状态");
//        titileList.add("标还款金额");
//        List<String> fieldList = new ArrayList<>();
//        fieldList.add("borrowNo");
//        fieldList.add("hhdRepayStatus");
//        fieldList.add("hhdRepaidAmount");
        Long start = System.currentTimeMillis();
        List<Test> testList = read("E:\\projects\\test\\github\\7_8_9.xlsx",
                0, 0, null, null, Test.class);
        if (!CollectionUtils.isEmpty(testList)) {
            System.out.println("催收总条数：" + testList.size());
            List<String> testStrs = averageAssign(testList, 100);
            System.out.println("拆分数：" + testStrs.size());
//            testStrs.parallelStream().forEach(test -> {
//                hhdWithJson(test);
//                //System.out.println(test);
//
//            });      //1.8
//            testStrs.forEach(test -> {
//                hhdOverdueCollectedCount(test);
//            });
        }

//        hhdOverdueCollectedCount(str);
        System.out.println(System.currentTimeMillis() - start);
//        StringBuffer str = new StringBuffer();
//        for(Test test : testList){
//            str.append(test.getBorrowNo()).append(",");
//        }
//        System.out.println(str.toString());
//        testList.forEach(test -> {
//            System.out.println("borrowNo为：" + test.getBorrowNo() + "，状态为：" + test.getHhdRepayStatus() +
//            "，还款金额为：" + test.getHhdRepaidAmount());
//        });
//        testList.parallelStream().forEach(test -> {
//            System.out.println("borrowNo为：" + test.getBorrowNo() + "，状态为：" + test.getHhdRepayStatus() +
//                    "，还款金额为：" + test.getHhdRepaidAmount());
//        });

//        List<Map<String, Object>> dataMap = read("C:\\Users\\Administrator\\Desktop\\test1.xlsx");
//        System.out.println(dataMap);

    }

    //private static final String url = "http://192.168.129.43:8083/yuecaiwan/hhdOverdueCollectedCount";
    //private static final String url = "http://192.168.109.202:7080/yuecaiwan/hhdOverdueCollectedCount";
    private static final String url = "https://www.ycaiwan.com/yuecaiwan/hhdOverdueCollectedCount";

    private static String hhdOverdueCollectedCount(String pushJson) {
        Map<String, String> map = Maps.newHashMap();
        map.put("pushJson", pushJson);
        //System.out.println(pushJson);
        HttpResult httpUniteResult = HttpHandleFactory.getDefaultHandle().sendPostRequest(url, map);
        if (!httpUniteResult.getCode().equals(HttpStatus.SC_OK)) {
            System.out.println("执行失败：" + pushJson);
        }
        String result = JSON.parseObject(httpUniteResult.getBody(), String.class);
        System.out.println(result);
        return result;
    }


    private static String hhdOverdueCollectedCountWithJson(String pushJson) {
        Map<String, String> map = Maps.newHashMap();
        map.put("pushJson", pushJson);
        System.out.println(pushJson);
        HttpResult httpUniteResult = HttpHandleFactory.getDefaultHandle().sendPostRequestWithJSON(url, map, 10000000, 1000000);
        if (!httpUniteResult.getCode().equals(HttpStatus.SC_OK)) {
            System.out.println("执行失败：" + pushJson);
        }
        String result = JSON.parseObject(httpUniteResult.getBody(), String.class);
        System.out.println(result);
        return result;
    }


    private static String hhdWithJson(String pushJson) {
        System.out.println(pushJson);
        HttpResult httpUniteResult = HttpHandleFactory.getDefaultHandle().sendPostRequestWithJSON(url, pushJson, 10000000, 1000000);
        if (!httpUniteResult.getCode().equals(HttpStatus.SC_OK)) {
            System.out.println("执行失败：" + pushJson);
        }
        String result = JSON.parseObject(httpUniteResult.getBody(), String.class);
        System.out.println(result);
        return result;
    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param source
     * @return
     */
    public static <T> List<String> averageAssign(List<T> source, int n) {
        int size = 0;
        List<String> result = new ArrayList<String>();
        int remaider = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            size += value.size();
            result.add(JSON.toJSONString(value));
        }
        System.out.println("size:" + size);
        return result;
    }

}
