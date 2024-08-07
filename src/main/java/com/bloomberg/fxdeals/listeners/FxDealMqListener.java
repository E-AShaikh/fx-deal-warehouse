package com.bloomberg.fxdeals.listeners;

import com.bloomberg.fxdeals.aspects.ToLog;
import com.bloomberg.fxdeals.model.FxDeal;
import com.bloomberg.fxdeals.services.FxDealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class FxDealMqListener {

    private final FxDealService fxDealService;

    private final ObjectMapper objectMapper;


    public FxDealMqListener(FxDealService fxDealService, ObjectMapper objectMapper) {
        this.fxDealService = fxDealService;
        this.objectMapper = objectMapper;
    }

    @ToLog
    @JmsListener(destination = "${ibm.mq.queue}")
    public void receiveMessage(String message) {
        try {
            FxDeal fxDeal = objectMapper.readValue(message, FxDeal.class);
            fxDealService.saveFxDeal(fxDeal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}