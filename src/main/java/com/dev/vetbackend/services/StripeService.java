package com.dev.vetbackend.services;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secret}")
    private String API_SECRET_KEY;

    @Value("${stripe.secret.test}")
    private String API_SECRET_KEY_TEST;

    @Value("${stripe.webhook.secret}")
    private String STRIPE_WEBHOOK_SECRET;

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

        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        // Add any additional parameters here, such as customer or metadata

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return paymentIntent.getClientSecret();
    }

    public void processWebhook(String payload, String signatureHeader) {
        Stripe.apiKey = API_SECRET_KEY_TEST;

        Event event;

        try {
            event = Webhook.constructEvent(payload, signatureHeader, STRIPE_WEBHOOK_SECRET);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Invalid signature");
        } catch (Exception e) {
            throw new RuntimeException("Invalid payload");
        }

        StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
        if (stripeObject == null) {
            // Handle deserialization failure or throw an exception
        }

        handleEvent(event);
    }

    public void handleEvent(Event event) {
        // Process the event based on its type
        switch (event.getType()) {
            case "charge.succeeded":
                // Handle a successful charge
                System.out.println("success");
                break;
            case "customer.subscription.created":
                // Handle a new subscription
                System.out.println("subscription");
                break;
            case "payment_intent.succeeded":
                // Define and call a function to handle the payment_intent.succeeded event
                System.out.println("hello?");
                break;
            // Add more cases for other event types
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }
    }

}
