package com.acme.mytrader.dao;

import com.acme.mytrader.dto.Order;

import java.util.List;

public interface OrderDAO {

    int addBuyOrder(Order order);
    void loadAllOrders();
    List<Order> getAllBuyOrders();
    List<Order> getAllSellOrders();
    Order editBuyOrder(int buyOrderId, Order buyOrder);
    Order editSellOrder(int sellOrderId, Order sellOrder);

}
