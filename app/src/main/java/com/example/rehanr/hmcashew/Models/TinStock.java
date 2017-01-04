package com.example.rehanr.hmcashew.Models;

/**
 * Created by rehan r on 03-01-2017.
 */
public class TinStock {
    int stockId,lotNo,tins,gradeId;
    String stockdate,gradetype,gradeName;


    public TinStock(int stockId, int lotNo, int tins, int gradeId, String stockdate,String gradetype,String gradeName) {
        this.stockId = stockId;
        this.lotNo = lotNo;
        this.tins = tins;
        this.gradeId = gradeId;
        this.stockdate = stockdate;
        this.gradetype = gradetype;
        this.gradeName = gradeName;
    }


    //currently using this construtor
    public TinStock(String gradeName,int tins){
        this.gradeName = gradeName;
        this.tins = tins;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getLotNo() {
        return lotNo;
    }

    public void setLotNo(int lotNo) {
        this.lotNo = lotNo;
    }

    public int getTins() {
        return tins;
    }

    public void setTins(int tins) {
        this.tins = tins;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getStockdate() {
        return stockdate;
    }

    public void setStockdate(String stockdate) {
        this.stockdate = stockdate;
    }

    public String getGradetype() {
        return gradetype;
    }

    public void setGradetype(String gradetype) {
        this.gradetype = gradetype;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
}
