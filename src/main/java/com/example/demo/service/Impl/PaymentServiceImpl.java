package com.example.demo.service.Impl;

import com.example.demo.model.ChargeRequest;
import com.example.demo.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${STRIPE_S_KEY}")
    public String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = this.secretKey;
    }

    @Override
    @Transactional
    public Charge pay(ChargeRequest chargeRequest) throws CardException, APIException, AuthenticationException, InvalidRequestException, APIConnectionException {
        Map<String, Object> chargeMap = new HashMap<>();
        chargeMap.put("amount", chargeRequest.getAmount());
        chargeMap.put("currency", chargeRequest.getCurrency());
        chargeMap.put("source", chargeRequest.getStripeToken());
        chargeMap.put("description", chargeRequest.getDescription());
        return Charge.create(chargeMap);
    }

}
