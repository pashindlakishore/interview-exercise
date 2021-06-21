package com.acme.mytrader.execution;

public class ExecutionServiceImpl implements ExecutionService {


    @Override
    public void buy(String security, double price, int volume) {
        System.out.println("A single buy order instruction has been executed for "+security);
    }

    @Override
    public void sell(String security, double price, int volume) {

    }
}
