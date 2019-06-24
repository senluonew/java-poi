package com.luo.poi.model;

import com.luo.poi.util.Title;

import java.text.DecimalFormat;

/**
 * @author pudding
 * @version 1.0.0
 * @design
 * @date 2018/3/26.21:03
 * @see
 */
public class Test {

    @Title(value = "标编号",index = 0)
    private String borrowNo;

    @Title(value = "标状态",index = 1)
    private Integer hhdRepayStatus;

    @Title(value = "标还款金额",index = 2)
    private Double hhdRepaidAmount;




    public Test() {
    }

    public Test(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public Test(String borrowNo, Integer hhdRepayStatus, Double hhdRepaidAmount) {
        this.borrowNo = borrowNo;
        this.hhdRepayStatus = hhdRepayStatus;
        this.hhdRepaidAmount = hhdRepaidAmount;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public Integer getHhdRepayStatus() {
        return hhdRepayStatus;
    }

    public void setHhdRepayStatus(Integer hhdRepayStatus) {
        this.hhdRepayStatus = hhdRepayStatus;
    }

    public Double getHhdRepaidAmount() {
        return hhdRepaidAmount;
    }

    public void setHhdRepaidAmount(Double hhdRepaidAmount) {
        this.hhdRepaidAmount = hhdRepaidAmount;
    }

    public static void main(String[] args) {
        System.out.println(getTwoDecimal(2.88888));
    }

    /**
     * 将数据保留两位小数
     */
    public static Double getTwoDecimal(Double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String string = dFormat.format(num);
        Double temp = Double.valueOf(string);
        return temp;
    }
}
