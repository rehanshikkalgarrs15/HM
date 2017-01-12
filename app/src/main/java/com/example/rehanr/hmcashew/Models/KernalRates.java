package com.example.rehanr.hmcashew.Models;

/**
 * Created by rehan r on 12-01-2017.
 */
public class KernalRates {

    String gradeName,date1,date2;
    double rate1,rate2;

    public KernalRates(String gradeName, String date1, String date2, double rate1, double rate2) {
        this.gradeName = gradeName;
        this.date1 = date1;
        this.date2 = date2;
        this.rate1 = rate1;
        this.rate2 = rate2;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public double getRate1() {
        return rate1;
    }

    public void setRate1(double rate1) {
        this.rate1 = rate1;
    }

    public double getRate2() {
        return rate2;
    }

    public void setRate2(double rate2) {
        this.rate2 = rate2;
    }
}
