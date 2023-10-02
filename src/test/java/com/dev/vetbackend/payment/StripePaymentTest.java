package com.dev.vetbackend.payment;

import com.dev.vetbackend.services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerListParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

public class StripePaymentTest {
//    @Autowired
//    private StripeService stripeService;
//    @Test
//    public void testChargeCreditCard_successfulPayment() throws StripeException {
//       String token = "tok_visa"; // replace with valid test token
//        int amount = 1000; // replace with valid amount
//        String currency = "usd";
//        Charge charge = stripeService.chargeCreditCard(token, amount, currency);
//        assertNotNull(charge);
//        assertEquals("succeeded", charge.getStatus());
//    }
//    @Test
//    public void testChargeCreditCard_failedPayment() {
//        String token = "tok_chargeDeclined"; // replace with invalid test token
//        int amount = 1000; // replace with valid amount
//        String currency = "usd";
//
//        try {
//            stripeService.chargeCreditCard(token, amount, currency);
//            fail("Expected a StripeException to be thrown");
//        } catch (StripeException e) {
//            // Exception was thrown, test passes
//        }
//    }
//    @Test
//    public void testChargeCreditCardInvalidToken() {
//        // Set up test data
//        String token = "invalid_token";
//        int amount = 1000;
//        String currency = "USD";
//
//        // Execute the method and assert that it throws a StripeException
//        assertThrows(StripeException.class, () -> {
//            stripeService.chargeCreditCard(token, amount, currency);
//        });
//    }
//
//    @Test
//    public void testChargeCreditCardInvalidAmount() {
//        assertThrows(StripeException.class, () -> {
//            stripeService.chargeCreditCard("valid_token", -1000, "USD");
//        });
//    }
//
//    @Test
//    public void testChargeCreditCardInvalidCurrency() {
//        assertThrows(StripeException.class, () -> {
//            stripeService.chargeCreditCard("valid_token", 1000, "invalid_currency");
//        });
//    }
//
//    @Test
//    public void testChargeCreditCardValidParams() throws StripeException {
//        Charge charge = stripeService.chargeCreditCard("tok_visa", 1000, "USD");
//        assertNotNull(charge.getId());
//        assertEquals(charge.getAmount(), 1000);
//        assertTrue(charge.getCurrency().equalsIgnoreCase("USD"));
//    }
//
//////    Tutorial
//    @Test
//    public void stripeTest() {
//        //Globally
//        Stripe.apiKey = "sk_test_51MjS0nC1mXx7DPaRLMFgbaIpzGI1wXebNSh4yxoz6ya3JGNuNAQbIirsVEiyQacSbGeAb5D6ZeNO6TfGbITHofmY00lvBKQIBV";
//
//        CustomerListParams params = CustomerListParams.builder().build();
//        try {
//            CustomerCollection customers = Customer.list(params);
//            System.out.println(customers);
//        } catch (StripeException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    public void stripePerRequestTest() {
//
////        PerRequest
//        RequestOptions requestOptions = RequestOptions.builder()
//                .setApiKey("sk_test_51MjS0nC1mXx7DPaRLMFgbaIpzGI1wXebNSh4yxoz6ya3JGNuNAQbIirsVEiyQacSbGeAb5D6ZeNO6TfGbITHofmY00lvBKQIBV")
//                .build();
//
//        CustomerListParams params = CustomerListParams.builder().build();
//        try {
//            CustomerCollection customers = Customer.list(params, requestOptions);
//            System.out.println(customers);
//        } catch (StripeException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    Connect TBD
//    @Test
//    void stripeConnectRequestTest() {
//
////        PerRequest
//        RequestOptions requestOptions = RequestOptions.builder()
//                .setStripeAccount("acu_aa")
//                .setApiKey("sk_test_51MjS0nC1mXx7DPaRLMFgbaIpzGI1wXebNSh4yxoz6ya3JGNuNAQbIirsVEiyQacSbGeAb5D6ZeNO6TfGbITHofmY00lvBKQIBV")
//                .build();
//
//        CustomerListParams params = CustomerListParams.builder().build();
//        try {
//            CustomerCollection customers = Customer.list(params, requestOptions);
//            System.out.println(customers);
//        } catch (StripeException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
