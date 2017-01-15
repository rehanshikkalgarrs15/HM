package com.example.rehanr.hmcashew.Models;

/**
 * Created by rehan r on 15-01-2017.
 */
public class DealerPendingPayment {

    String PPdate,DealerName,BillNo,Billdate;
    int totalQuantity;
    double Billamount,PaymentMade,PendingAmount;

    public DealerPendingPayment(String PPdate, String dealerName, String billNo, String billdate, int totalQuantity, double billamount, double paymentMade, double pendingAmount) {
        this.PPdate = PPdate;
        DealerName = dealerName;
        BillNo = billNo;
        Billdate = billdate;
        this.totalQuantity = totalQuantity;
        Billamount = billamount;
        PaymentMade = paymentMade;
        PendingAmount = pendingAmount;
    }

    public DealerPendingPayment() {
    }

    public DealerPendingPayment(String dealerName, double pendingAmount) {
        DealerName = dealerName;
        PendingAmount = pendingAmount;
    }

    public String getPPdate() {
        return PPdate;
    }

    public void setPPdate(String PPdate) {
        this.PPdate = PPdate;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getBilldate() {
        return Billdate;
    }

    public void setBilldate(String billdate) {
        Billdate = billdate;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getBillamount() {
        return Billamount;
    }

    public void setBillamount(double billamount) {
        Billamount = billamount;
    }

    public double getPaymentMade() {
        return PaymentMade;
    }

    public void setPaymentMade(double paymentMade) {
        PaymentMade = paymentMade;
    }

    public double getPendingAmount() {
        return PendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
        PendingAmount = pendingAmount;
    }
}
