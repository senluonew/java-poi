package com.luo.poi.enums;

/**
 * @author luosen
 * @version 0.0.1
 * @date 2018/12/18
 * @time 11:08
 * @function 功能:
 * @describe 版本描述:
 * @modifyLog 修改日志:
 */
public enum ErrorLog {

    /**
     * EXCEL上传数据有误
     */
    EXCEL_UPLOAD_ROW_ERROR("EXCEL数据上传-行有误", "行数[%s], 错误信息{%s}。"),

    EXCEL_UPLOAD_CEIL_ERROR("EXCEL数据上传-单元格有误", "列名[%s]:%s，值为(%s)");

    private String type;

    private String format;

    ErrorLog(String type, String format) {
        this.type = type;
        this.format = format;
    }

    public String getType() {
        return this.type;
    }

    public String getFormat() {
        return this.format;
    }
}
