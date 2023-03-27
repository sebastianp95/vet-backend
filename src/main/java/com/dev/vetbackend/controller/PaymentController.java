package com.dev.vetbackend.controller;

import com.dev.vetbackend.entity.PaymentRequest;
import com.dev.vetbackend.payment.StripePayment;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final StripePayment stripePayment;

    public PaymentController(StripePayment stripePayment) {
        this.stripePayment = stripePayment;
    }

    @PostMapping("/charge")
    public ResponseEntity<?> chargeCreditCard(@RequestBody PaymentRequest paymentRequest) {
        try {
            Charge charge = stripePayment.chargeCreditCard(paymentRequest.getToken(), paymentRequest.getAmount(), paymentRequest.getCurrency());
            return new ResponseEntity<>(charge.getId(), HttpStatus.OK);
        } catch (StripeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/create-payment-intent")
    public ResponseEntity<String> createPaymentIntent() {
        try {
            String clientSecret = stripePayment.createPaymentIntent(100, "usd");
            return ResponseEntity.ok(clientSecret);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating PaymentIntent: " + e.getMessage());
        }
    }
}
