package com.acme.mytrader.price;

import com.acme.mytrader.dao.OrderDAO;
import com.acme.mytrader.dao.OrderDAOImpl;
import com.acme.mytrader.dto.Order;
import com.acme.mytrader.execution.ExecutionService;

import java.util.List;
import java.util.Optional;

public class PriceListenerImpl implements PriceListener{

    private ExecutionService executionService;
    private List<Order> buyOrders;
    private List<Order> sellOrders;
    private OrderDAO orderDAO;
    private Order buyOrder;

    private Order matchedSellOrder;

    public PriceListenerImpl(ExecutionService executionService,Order buyOrder) {
        this.executionService = executionService;
        this.buyOrder = buyOrder;
        orderDAO = new OrderDAOImpl();
        buyOrders = orderDAO.getAllBuyOrders();
        sellOrders = orderDAO.getAllSellOrders();
    }

    @Override
    public void priceUpdate(String security, double price) {
        System.out.println("============ Full Order Book Before Trade ============== ");
        monitorPriceMovementsInOrderBook();
        if(matchOrder(security,price)){
            this.executionService.buy(security,price,this.buyOrder.getVolume());
            updateOrderBook(matchedSellOrder);
            System.out.println("A single buy order instruction has been executed for "+security);

            buyOrders = orderDAO.getAllBuyOrders();
            sellOrders = orderDAO.getAllSellOrders();
            System.out.println("============ Full Order Book After Successful Trade ============== ");
            monitorPriceMovementsInOrderBook();
        }
        /*else{ // THIS CODE IS TO ADD A NEW ORDER ENTRY IN THE ORDER BOOK IF NO MATCH ORDER FOUND
            // Add a new order entry in orders.txt file
           // orderDAO.addBuyOrder(buyOrder);
            System.out.println("============ Order Book Without Trade ======================== ");
            monitorPriceMovementsInOrderBook();
        }*/
    }


     public void monitorPriceMovementsInOrderBook(){

        // Monitors buy price movements on a specified single order (e.g. "IBM")
        buyOrders.stream()
                 .filter(order -> order.getSecurity().equals(buyOrder.getSecurity()))
                 .forEach(System.out::println);
        System.out.println("========================================================================");

         // Monitors sell price movements on a specified single order (e.g. "IBM")
        sellOrders.stream()
                 .filter(order -> order.getSecurity().equals(buyOrder.getSecurity()))
                 .forEach(System.out::println);
    }







    public boolean matchOrder(String security, double price){


        Optional<Order> matchOrder = sellOrders.stream()
                .filter(sellOrder -> sellOrder.getOrderType() != buyOrder.getOrderType() && sellOrder.getSecurity().equals(security) )
                .filter(sellOrder -> price > sellOrder.getPrice() && sellOrder.getVolume() >= buyOrder.getVolume())
                .findFirst();
        if(matchOrder.isPresent()) {
             matchedSellOrder = matchOrder.get();
             updateSellOrderVolume(matchedSellOrder);
             return true;
        }

        return false;
    }


    public void updateSellOrderVolume(Order matchedSellOrder){
            // Update sell order volume
            int volume = matchedSellOrder.getVolume() - buyOrder.getVolume();
            matchedSellOrder.setVolume(volume);

    }

    public void updateOrderBook(Order sellOrder){
        orderDAO.editSellOrder(sellOrder.getOrderId(), sellOrder);
    }


}
