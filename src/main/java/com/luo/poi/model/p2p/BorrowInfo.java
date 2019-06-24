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
public class BorrowInfo {

    @Title(value = "标编号",index = 0)
    private String borrowNo;

    @Title(value = "姓名",index = 1)
    private String name;

    @Title(value = "性别",index = 2)
    private String sex;

    @Title(value = "年龄",index = 3)
    private Integer age;

    @Title(value = "婚姻状态",index = 4)
    private String maritalStatus;

    @Title(value = "身份证",index = 5)
    private String cardId;

    @Title(value = "车辆信息",index = 6)
    private String carInfo;

    @Title(value = "购买价格",index = 7)
    private Double buyPrice;

    @Title(value = "抵押价格",index = 8)
    private Double mortgagePrice;

    @Title(value = "行驶公里",index = 9)
    private Double mileage;

    @Title(value = "车牌号",index = 10)
    private String plateNumber;

    @Title(value = "信用评级",index = 11)
    private String creditRating;

    public BorrowInfo() {
    }

    public BorrowInfo(String borrowNo, String name, String sex, Integer age, String maritalStatus, String cardId, String carInfo, Double buyPrice, Double mortgagePrice, Double mileage, String plateNumber, String creditRating) {
        this.borrowNo = borrowNo;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.cardId = cardId;
        this.carInfo = carInfo;
        this.buyPrice = buyPrice;
        this.mortgagePrice = mortgagePrice;
        this.mileage = mileage;
        this.plateNumber = plateNumber;
        this.creditRating = creditRating;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getMortgagePrice() {
        return mortgagePrice;
    }

    public void setMortgagePrice(Double mortgagePrice) {
        this.mortgagePrice = mortgagePrice;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    @Override
    public String toString() {
        return "BorrowInfo{" +
                "borrowNo='" + borrowNo + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", cardId='" + cardId + '\'' +
                ", carInfo='" + carInfo + '\'' +
                ", buyPrice=" + buyPrice +
                ", mortgagePrice=" + mortgagePrice +
                ", mileage=" + mileage +
                ", plateNumber='" + plateNumber + '\'' +
                ", creditRating='" + creditRating + '\'' +
                '}';
    }
}
