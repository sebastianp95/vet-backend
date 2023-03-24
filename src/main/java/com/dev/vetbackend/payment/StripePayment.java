package com.dev.vetbackend.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StripePayment {

    @Value("${stripe.secret}")
    private String API_SECRET_KEY;

    public Charge chargeCreditCard(String token, int amount, String currency) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", currency);
        chargeParams.put("description", "Payment Description");
        chargeParams.put("source", token);

        return Charge.create(chargeParams);
    }

}
