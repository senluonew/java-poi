package com.luo.poi.model.p2p;


import com.luo.poi.util.Title;

/**
 * @author luosen
 * @version 0.0.1
 * @date 2018/9/8
 * @time 15:33
 * @function 功能:
 * @describe 版本描述:
 * @modifyLog 修改日志:
 */
public class BorrowSql {

    @Title(value = "标编号",index = 0)
    private String borrowNo;

//    @Title(value = "项目描述",index = 1)
//    private String describle;

    @Title(value = "数据库脚本",index = 1)
    private String sql;

    @Title(value = "数据库脚本",index = 2)
    private String updateBorrow;


    public BorrowSql() {
    }

    public BorrowSql(String borrowNo, String sql, String updateBorrow) {
        this.borrowNo = borrowNo;
        this.sql = sql;
        this.updateBorrow = updateBorrow;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getUpdateBorrow() {
        return updateBorrow;
    }

    public void setUpdateBorrow(String updateBorrow) {
        this.updateBorrow = updateBorrow;
    }

    @Override
    public String toString() {
        return "BorrowSql{" +
                "borrowNo='" + borrowNo + '\'' +
                ", sql='" + sql + '\'' +
                ", updateBorrow='" + updateBorrow + '\'' +
                '}';
    }
}
