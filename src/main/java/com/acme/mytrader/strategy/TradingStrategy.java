package com.acme.mytrader.strategy;

import com.acme.mytrader.dto.Order;
import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceListenerImpl;
import com.acme.mytrader.price.PriceSource;
import com.acme.mytrader.validators.OrderValidator;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
public class TradingStrategy {

  private PriceSource priceSource;
  private PriceListener priceListener;
  private ExecutionService executionService;

  public TradingStrategy(PriceSource priceSource,
                         ExecutionService executionService,
                         PriceListener priceListener){
     this.priceListener = priceListener;
     this.executionService = executionService;
     this.priceSource = priceSource;

  }

  public void initMonitorStockPrices(Order customerOrder) throws IllegalArgumentException {
      OrderValidator.validateOrder(customerOrder);
      this.priceSource.addPriceListener(this.priceListener);
      this.priceListener.priceUpdate(customerOrder.getSecurity(), customerOrder.getPrice());
  }





}
