package com.luo.poi.util;


import com.luo.poi.model.ExceptionVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {


    /**
     * TODO 读取excel
     *
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcel(MultipartFile is, Class<T> type, List<ExceptionVo> exceptionVos) throws IOException {
        //检查文件
        checkFile(is);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(is);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<T> list = new ArrayList<>();
        if (workbook != null) {
            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return null;
            }
            list = ExcelReadUtil.readSheet(sheet, 0, 0, null, null, type, exceptionVos);
        }
        workbook.close();
        return list;
    }


    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return workbook;
    }

    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            throw new IOException(fileName + "不是excel文件");
        }
    }


}
