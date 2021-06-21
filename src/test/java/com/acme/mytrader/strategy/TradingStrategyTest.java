package com.acme.mytrader.strategy;

import com.acme.mytrader.dto.Order;
import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceListenerImpl;
import com.acme.mytrader.price.PriceSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class TradingStrategyTest {

    private TradingStrategy tradingStrategy;
    private PriceSource priceSource;
    private ExecutionService executionService;
    private PriceListener priceListener;
    private ArgumentCaptor<String> securityCaptor;
    private ArgumentCaptor<Double> priceCaptor;
    private ArgumentCaptor<Integer> volumeCaptor;

    @Before
    public void setUp(){
        priceSource = mock(PriceSource.class);
        executionService = mock(ExecutionService.class);
        securityCaptor = ArgumentCaptor.forClass(String.class);
        priceCaptor = ArgumentCaptor.forClass(Double.class);
        volumeCaptor = ArgumentCaptor.forClass(Integer.class);

    }



    @Test(expected = IllegalArgumentException.class)
    public void testInitMonitorStockPricesForInvalidBuyPrice() {
        Order customerOrder = new Order("IBM",-10,50,Order.OrderType.BUY);
        tradingStrategy = new TradingStrategy(priceSource,executionService,priceListener);
        tradingStrategy.initMonitorStockPrices(customerOrder);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitMonitorStockPricesForNullSecurity()  {
        Order customerOrder = new Order(null,30,10,Order.OrderType.BUY);
        tradingStrategy = new TradingStrategy(priceSource,executionService,priceListener);
        tradingStrategy.initMonitorStockPrices(customerOrder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitMonitorStockPricesForInvalidVolume()  {
        Order customerOrder = new Order(null,30,-1,Order.OrderType.BUY);
        tradingStrategy = new TradingStrategy(priceSource,executionService,priceListener);
        tradingStrategy.initMonitorStockPrices(customerOrder);

    }

    @Test
    public void testInitMonitorStockPricesToBuyWithMatchingPrice() {
        Order customerOrder = new Order("IBM",30.00,50,Order.OrderType.BUY);
        priceListener = new PriceListenerImpl(executionService,customerOrder);
        tradingStrategy = new TradingStrategy(priceSource,executionService,priceListener);
        tradingStrategy.initMonitorStockPrices(customerOrder);

        verify(executionService)
              .buy(securityCaptor.capture(), priceCaptor.capture(), volumeCaptor.capture());

        assertThat(securityCaptor.getValue(),is("IBM"));
        assertThat(priceCaptor.getValue(),is(30.00));
        assertThat(volumeCaptor.getValue(),is(50));
    }



    @Test
    public void testInitMonitorStockPricesToBuyWithHigherPrice() {
        Order customerOrder = new Order("IBM",70,50,Order.OrderType.BUY);
        priceListener = new PriceListenerImpl(executionService,customerOrder);
        tradingStrategy = new TradingStrategy(priceSource,executionService,priceListener);
        tradingStrategy.initMonitorStockPrices(customerOrder);

        verify(executionService,never())
                .buy(securityCaptor.capture(), priceCaptor.capture(), volumeCaptor.capture());

    }

    @Test
    public void testInitMonitorStockPricesToBuyWithNoMatchingSecurity()  {
        Order customerOrder = new Order("AAPL",30,50,Order.OrderType.BUY);
        priceListener = new PriceListenerImpl(executionService,customerOrder);
        tradingStrategy = new TradingStrategy(priceSource,executionService,priceListener);
        tradingStrategy.initMonitorStockPrices(customerOrder);

        verify(executionService, never())
                .buy(securityCaptor.capture(), priceCaptor.capture(), volumeCaptor.capture());

    }

    @Test
    public void testInitMonitorStockPricesToBuyWithNoMatchingVolume() {
        Order customerOrder = new Order("AAPL",30,200,Order.OrderType.BUY);
        priceListener = new PriceListenerImpl(executionService,customerOrder);
        tradingStrategy = new TradingStrategy(priceSource,executionService,priceListener);
        tradingStrategy.initMonitorStockPrices(customerOrder);

        verify(executionService, never())
                .buy(securityCaptor.capture(), priceCaptor.capture(), volumeCaptor.capture());

    }

    @Test
    public void testInitMonitorStockPricesToBuyWithInCorrectOrderType() {
        Order customerOrder = new Order("AAPL",30,200,Order.OrderType.SELL);
        priceListener = new PriceListenerImpl(executionService,customerOrder);
        tradingStrategy = new TradingStrategy(priceSource,executionService,priceListener);
        tradingStrategy.initMonitorStockPrices(customerOrder);

        verify(executionService, never())
                .buy(securityCaptor.capture(), priceCaptor.capture(), volumeCaptor.capture());

    }


}
