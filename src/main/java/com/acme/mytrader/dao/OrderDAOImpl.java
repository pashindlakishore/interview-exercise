package com.acme.mytrader.dao;

import com.acme.mytrader.dto.Order;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class OrderDAOImpl implements OrderDAO{
    public final String ORDER_FILE = "orders.txt";
    public static final String DELIMITER = "::";
    private Map<Integer, Order> buyOrders;
    private Map<Integer, Order> sellOrders;

    public OrderDAOImpl() {
        buyOrders = new TreeMap<>();
        sellOrders = new TreeMap<>();
        // Load orders from orders.txt file
        loadAllOrders();
    }

    @Override
    public void loadAllOrders() {

        try {
            List<String> orderLines = Files.readAllLines(Paths.get(ORDER_FILE));
            orderLines.forEach(orderLine -> {
                Order currentOrder = unmarshallOrder(orderLine);

                if(String.valueOf(currentOrder.getOrderType()).equals("BUY")){
                    buyOrders.put(currentOrder.getOrderId(), currentOrder);
                } else {
                    sellOrders.put(currentOrder.getOrderId(), currentOrder);
                }
            } );

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public int addBuyOrder(Order buyOrderDto) {
        if (buyOrderDto == null)
                return 0;

        // Create order with new ID
        Order buyOrder = new Order(getFreeBuyOrderId(),
                                    buyOrderDto.getSecurity(),
                                    buyOrderDto.getPrice(),
                                    buyOrderDto.getVolume(), Order.OrderType.BUY);


        // Add new order to collection
        buyOrders.put(buyOrder.getOrderId(), buyOrder);

        // Update on file
        writeOrders();

        // Return assigned ID
        return buyOrder.getOrderId();
    }


    private void writeOrders() {

        PrintWriter out = null;

        try {
            out = new PrintWriter(new FileWriter(ORDER_FILE));
        } catch (IOException e) {
            System.out.println("Could not save order data");
        }

        String orderAsText;
        List<Order> buyOrderList = this.getAllBuyOrders();
        for (Order currentOrder : buyOrderList) {
            orderAsText = marshallOrder(currentOrder);
            assert out != null;
            out.println(orderAsText);
            out.flush();
        }
        List<Order> sellOrderList = this.getAllSellOrders();
        for (Order currentOrder : sellOrderList) {
            orderAsText = marshallOrder(currentOrder);
            assert out != null;
            out.println(orderAsText);
            out.flush();
        }

        assert out != null;
        out.close();
    }

    private String marshallOrder (Order aOrder) {
        String orderAsText = aOrder.getOrderId() + DELIMITER;
        orderAsText += aOrder.getSecurity() + DELIMITER;
        orderAsText += aOrder.getPrice() + DELIMITER;
        orderAsText += aOrder.getVolume() + DELIMITER;
        orderAsText += aOrder.getOrderType();
        return orderAsText;
    }

    private int getFreeBuyOrderId() {
        if (buyOrders.isEmpty()) {
            return 1;
        }
        TreeSet<Integer> takenIds = new TreeSet<>(buyOrders.keySet());
        int lastId = takenIds.last();
        return ++lastId;
    }

    private int getFreeSellOrderId() {
        if (sellOrders.isEmpty()) {
            return 1;
        }
        TreeSet<Integer> takenIds = new TreeSet<>(sellOrders.keySet());
        int lastId = takenIds.last();
        return ++lastId;
    }


    @Override
    public List<Order> getAllBuyOrders() {
        return new ArrayList<>(buyOrders.values());
    }

    @Override
    public List<Order> getAllSellOrders() {
        return new ArrayList<>(sellOrders.values());
    }

    private Order unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMITER);
        int orderId = Integer.parseInt(orderTokens[0]);
        String security = orderTokens[1];
        double price = Double.parseDouble(orderTokens[2]);
        int quantity = Integer.parseInt(orderTokens[3]);
        Order.OrderType orderType = Order.OrderType.valueOf(orderTokens[4]);
        return new Order(orderId,security, price, quantity, orderType);
    }

    @Override
    public Order editBuyOrder(int buyOrderId, Order buyOrder) {
        if (buyOrderId == 0 || buyOrder == null) return null;

        Order replacedOrder = buyOrders.replace(buyOrderId, buyOrder);

        // Update on orders.txt
        writeOrders();

        return replacedOrder;
    }

    @Override
    public Order editSellOrder(int sellOrderId, Order sellOrder) {
        if (sellOrderId == 0 || sellOrder == null) return null;

        Order replacedOrder =  sellOrders.replace(sellOrderId, sellOrder);

        // Update on orders.txt
        writeOrders();

        return replacedOrder;
    }


}
