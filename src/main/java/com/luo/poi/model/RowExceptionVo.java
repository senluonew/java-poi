package com.luo.poi.model;

import java.io.Serializable;

/**
 * @author luosen
 * @version 0.0.1
 * @date 2018/12/18
 * @time 10:17
 * @function 功能:
 * @describe 版本描述:
 * @modifyLog 修改日志:
 */
public class RowExceptionVo implements Serializable{
    private static final long serialVersionUID = 4178981837941892940L;

    /**
     * 标题名
     */
    private String titleName;

    /**
     * 单元格值
     */
    private Object value;

    /**
     * 错误信息
     */
    private String errorInfo;

    private RowExceptionVo(Builder builder) {
        setTitleName(builder.titleName);
        setValue(builder.value);
        setErrorInfo(builder.errorInfo);
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }


    public static final class Builder {
        private String titleName;
        private Object value;
        private String errorInfo;

        public Builder() {
        }

        public Builder titleName(String val) {
            titleName = val;
            return this;
        }

        public Builder value(Object val) {
            value = val;
            return this;
        }

        public Builder errorInfo(String val) {
            errorInfo = val;
            return this;
        }

        public RowExceptionVo build() {
            return new RowExceptionVo(this);
        }
    }
}
