package com.dev.vetbackend.services;

import com.dev.vetbackend.entity.User;
import com.dev.vetbackend.security.UserDetailServiceImpl;
import com.dev.vetbackend.security.UserDetailsImpl;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Plan;
import com.stripe.model.Price;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionItem;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.secret}")
    private String API_SECRET_KEY;
    @Value("${stripe.webhook.secret}")
    private String STRIPE_WEBHOOK_SECRET;
    @Value("${stripe.plan.plus}")
    private String PLUS_PLAN_PRODUCT_ID;
    @Value("${stripe.plan.premium}")
    private String PREMIUM_PLAN_PRODUCT_ID;
    private final UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    public StripeService(UserDetailServiceImpl userDetailServiceImpl) {
        this.userDetailServiceImpl = userDetailServiceImpl;

    }

    // This method is currently not in use but is preserved for potential future requirements.
    public Charge chargeCreditCard(String token, int amount, String currency) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount);
        chargeParams.put("currency", currency);
        chargeParams.put("description", "Payment Description");
        chargeParams.put("source", token);

        return Charge.create(chargeParams);
    }

    // This method is currently not in use but is preserved for potential future requirements.
    public String createPaymentIntent(int amount, String currency) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        // Add any additional parameters here, such as customer or metadata

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return paymentIntent.getClientSecret();
    }

    public void processWebhook(String payload, String signatureHeader) {
        Stripe.apiKey = API_SECRET_KEY;

        Event event;

        try {
            event = Webhook.constructEvent(payload, signatureHeader, STRIPE_WEBHOOK_SECRET);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Invalid signature");
        } catch (Exception e) {
            throw new RuntimeException("Invalid payload");
        }


        handleEvent(event);
    }

    public void handleEvent(Event event) {

        switch (event.getType()) {
            case "charge.succeeded":
                // Handle a successful charge
                break;
            case "customer.subscription.created":
                break;
            case "checkout.session.completed":
                break;
            case "invoice.payment_succeeded":
                handleNewSubscription(event);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }
    }

    private void handleNewSubscription(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().orElse(null);
        if (invoice != null) {
            String subscriptionId = invoice.getSubscription();
            String customerId = invoice.getCustomer();

            // Fetch the customer object to get the user's email
            Customer customer = getCustomer(customerId);
            String email = customer.getEmail();
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailServiceImpl.loadUserByUsername(email);
            User user = userDetails.getUser();

            String plan = getProductIdFromInvoice(invoice);
            userDetailServiceImpl.updateUserSubscription(user, subscriptionId, plan, "active");
        }
    }

    private Customer getCustomer(String customerId) {
        try {
            return Customer.retrieve(customerId);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
    private String getProductIdFromInvoice(Invoice invoice) {
        List<InvoiceLineItem> invoiceLineItems = invoice.getLines().getData();
        if (!invoiceLineItems.isEmpty()) {
            Price price = invoiceLineItems.get(0).getPrice();
            String productId = price.getProduct();

            if (productId == null) {
                return null;
            }
            
            if (PLUS_PLAN_PRODUCT_ID.equals(productId)) {
                return "plus";
            } else if (PREMIUM_PLAN_PRODUCT_ID.equals(productId)) {
                return "premium";
            } else {
                // Handle unknown plan
                System.out.println("Unknown plan");
            }
        }
        return null;
    }
}
