package com.example.rehanr.hmcashew.Models;

/**
 * Created by rehan r on 20-01-2017.
 */
public class RcnStock {

    String tag;
    int bag,quantity;

    public RcnStock(String tag, int bag, int quantity) {
        this.tag = tag;
        this.bag = bag;
        this.quantity = quantity;
    }

    //default constructor

    public RcnStock(){}

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getBag() {
        return bag;
    }

    public void setBag(int bag) {
        this.bag = bag;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
