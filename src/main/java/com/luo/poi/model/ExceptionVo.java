package com.luo.poi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luosen
 * @version 0.0.1
 * @date 2018/12/18
 * @time 10:14
 * @function 功能:
 * @describe 版本描述:
 * @modifyLog 修改日志:
 */
public class ExceptionVo implements Serializable{
    private static final long serialVersionUID = 5172177970119415118L;

    /**
     * 行数
     */
    private Integer row;

    /**
     * 单元格错误信息
     */
    private List<RowExceptionVo> rowExceptionVos;

    public ExceptionVo() {
    }

    private ExceptionVo(Builder builder) {
        setRow(builder.row);
        setRowExceptionVos(builder.rowExceptionVos);
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public List<RowExceptionVo> getRowExceptionVos() {
        return rowExceptionVos;
    }

    public void setRowExceptionVos(List<RowExceptionVo> rowExceptionVos) {
        this.rowExceptionVos = rowExceptionVos;
    }


    public static final class Builder {
        private Integer row;
        private List<RowExceptionVo> rowExceptionVos;

        public Builder() {
        }

        public Builder row(Integer val) {
            row = val;
            return this;
        }

        public Builder rowExceptionVos(List<RowExceptionVo> val) {
            rowExceptionVos = val;
            return this;
        }

        public ExceptionVo build() {
            return new ExceptionVo(this);
        }
    }
}
