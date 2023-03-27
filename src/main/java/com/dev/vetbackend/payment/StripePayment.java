package com.dev.vetbackend.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StripePayment {

    @Value("${stripe.secret}")
    private String API_SECRET_KEY;

    @Value("${stripe.secret.test}")
    private String API_SECRET_KEY_TEST;

    public Charge chargeCreditCard(String token, int amount, String currency) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", currency);
        chargeParams.put("description", "Payment Description");
        chargeParams.put("source", token);

        return Charge.create(chargeParams);
    }

    public  String createPaymentIntent(int amount, String currency) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        System.out.println(API_SECRET_KEY);

        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        // Add any additional parameters here, such as customer or metadata

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return paymentIntent.getClientSecret();
    }

}
