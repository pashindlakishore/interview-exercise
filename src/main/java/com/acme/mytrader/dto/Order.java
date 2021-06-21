package com.acme.mytrader.dto;

public class Order {

    private int orderId;
    private String security;
    private double price;
    private int volume;
    private OrderType orderType;


    public Order(int orderId,String security, double price, int volume, OrderType orderType) {
        this.orderId = orderId;
        this.security = security;
        this.price = price;
        this.volume = volume;
        this.orderType = orderType;
    }


    public Order(String security, double price, int volume, OrderType orderType) {
        this.security = security;
        this.price = price;
        this.volume = volume;
        this.orderType = orderType;
    }

    public enum OrderType{
        BUY, SELL
    }


    public void setOrderType(OrderType orderType){
        this.orderType = orderType;
    }

    public OrderType getOrderType(){
        return this.orderType;
    }

    public Order(String security, double price, int volume) {
        this.security = security;
        this.price = price;
        this.volume = volume;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }


    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", security='" + security + '\'' +
                ", price=" + price +
                ", volume=" + volume +
                ", orderType=" + orderType +
                '}';
    }
}
