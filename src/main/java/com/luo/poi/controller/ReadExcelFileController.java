package com.luo.poi.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.luo.poi.enums.ErrorLog;
import com.luo.poi.model.ExceptionVo;
import com.luo.poi.model.Goods;
import com.luo.poi.util.ExcelUtils;
import com.luo.poi.util.ExcelWriteUtil;
import com.luo.poi.util.SerialNumberUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luosen
 * @version 0.0.1
 * @date 2018/11/19
 * @time 10:17
 * @function 功能:
 * @describe 版本描述:
 * @modifyLog 修改日志:
 */
@RestController
@RequestMapping("/template")
public class ReadExcelFileController {

    private static final String XLS = ".xls";

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) {
        List<Goods> goodsList = new ArrayList<>();
        List<ExceptionVo> exceptionVos = new ArrayList<>();
        try {
            goodsList = ExcelUtils.readExcel(file, Goods.class, exceptionVos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(goodsList));
        List<String> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(exceptionVos)) {
            exceptionVos.forEach(exceptionVo -> {
                StringBuilder error = new StringBuilder();
                exceptionVo.getRowExceptionVos().forEach(rowExceptionVo -> {
                    error.append(String.format(ErrorLog.EXCEL_UPLOAD_CEIL_ERROR.getFormat(), rowExceptionVo.getTitleName(), rowExceptionVo.getErrorInfo(), rowExceptionVo.getValue()) + " | ");
                });

                result.add(String.format(ErrorLog.EXCEL_UPLOAD_ROW_ERROR.getFormat(), exceptionVo.getRow(), error.toString()));
            });
        }
        return "EXCEL上传数据有误行:" + JSON.toJSONString(result);
    }

    /**
     * 描述：下载外部案件导入模板
     *
     * @throws Exception
     */
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    @ResponseBody
    public void downloadExcel(HttpServletResponse res, HttpServletRequest req, String name) throws Exception {
        String fileName = name + ".xls";
        ServletOutputStream out;
        res.setContentType("multipart/form-data");
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html");
        String filePath = getClass().getResource("/template/" + fileName).getPath();
        String userAgent = req.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String((fileName).getBytes("UTF-8"), "ISO-8859-1");
        }
        filePath = URLDecoder.decode(filePath, "UTF-8");
        res.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        FileInputStream inputStream = new FileInputStream(filePath);
        out = res.getOutputStream();
        int b = 0;
        byte[] buffer = new byte[1024];
        while ((b = inputStream.read(buffer)) != -1) {
            // 4.写到输出流(out)中
            out.write(buffer, 0, b);
        }
        inputStream.close();

        if (out != null) {
            out.flush();
            out.close();
        }

    }


    /**
     * 描述：下载外部案件导入模板
     *
     * @throws Exception
     */
    @RequestMapping(value = "/downloadListToExcel", method = RequestMethod.GET)
    @ResponseBody
    public void downloadListToExcel(HttpServletResponse res, HttpServletRequest req) throws Exception {


        String fileName = SerialNumberUtil.getSerialNumber() + XLS;// 默认Excel名称
        String userAgent = req.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String((fileName).getBytes("UTF-8"), "ISO-8859-1");
        }
        res.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        ServletOutputStream out = res.getOutputStream();
        List<Goods> goods = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            goods.add(new Goods("code_" + i, i, (double) (i + 100)));
        }
        ExcelWriteUtil.write(out, goods, 500, XLS);
        if (out != null) {
            out.flush();
            out.close();
        }

    }
}
