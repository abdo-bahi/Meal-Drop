package com.example.apptuto;

public class ViewOrder {
    private String orderName, orderList, orderInstruction, orderAdress, orderTotalprice;

    public ViewOrder(String orderName, String orderList, String orderInstruction, String orderAdress, String orderTotalprice) {
        this.orderName = orderName;
        this.orderList = orderList;
        this.orderInstruction = orderInstruction;
        this.orderAdress = orderAdress;
        this.orderTotalprice = orderTotalprice;
    }
    public  ViewOrder(){

    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public String getOrderInstruction() {
        return orderInstruction;
    }

    public void setOrderInstruction(String orderInstruction) {
        this.orderInstruction = orderInstruction;
    }

    public String getOrderAdress() {
        return orderAdress;
    }

    public void setOrderAdress(String orderAdress) {
        this.orderAdress = orderAdress;
    }

    public String getOrderTotalprice() {
        return orderTotalprice;
    }

    public void setOrderTotalprice(String orderTotalprice) {
        this.orderTotalprice = orderTotalprice;
    }
}
