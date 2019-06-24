package com.luo.poi.model;

import com.luo.poi.util.Title;

import java.io.Serializable;

/**
 * @author pudding
 * @version 1.0.0
 * @design
 * @date 2018/3/26.21:03
 * @see
 */
public class Goods implements Serializable{

    private static final long serialVersionUID = -1545653359322291809L;

    @Title(value = "商品编号", isNotNull = true)
    private String goodsNo;

    @Title(value = "库存", isNotNull = true)
    private Integer inventory;

    @Title(value = "价格")
    private Double price;


    public Goods() {
    }

    public Goods(String goodsNo, Integer inventory, Double price) {
        this.goodsNo = goodsNo;
        this.inventory = inventory;
        this.price = price;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
