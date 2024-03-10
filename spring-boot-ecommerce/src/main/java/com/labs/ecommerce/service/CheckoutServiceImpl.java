package com.labs.ecommerce.service;

import com.labs.ecommerce.dao.CustomerRepository;
import com.labs.ecommerce.dto.PaymentInfo;
import com.labs.ecommerce.dto.Purchase;
import com.labs.ecommerce.dto.PurchaseResponse;
import com.labs.ecommerce.entity.Customer;
import com.labs.ecommerce.entity.Order;
import com.labs.ecommerce.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService{

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository, @Value("${stripe.key.secret}") String secretKey) {
        this.customerRepository = customerRepository;

        // Initialize Stripe key API with secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        // Retrieve the order info from dto
        Order order = purchase.getOrder();

        // Generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // Populate order with orderItems
        Set<OrderItem> orderItem = purchase.getOrderItems();
        orderItem.forEach(item -> order.add(item));

        // Populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // Populate customer with order
        Customer customer = purchase.getCustomer();

        // Check email customer
        Customer customerFromDB = customerRepository.findByEmail(customer.getEmail());
        if (customerFromDB != null) {
            customer = customerFromDB;
        }
        customer.add(order);

        // save to the database
        customerRepository.save(customer);

        // Return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);

        return PaymentIntent.create(params);
    }

    @Override
    public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws StripeException {

        PaymentIntent resource = PaymentIntent.retrieve(paymentIntentId);
        PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder()
                .setPaymentMethod("pm_card_visa")
                .setReturnUrl("https://www.example.com")
                .build();

        PaymentIntent paymentIntent = resource.confirm(params);

        return paymentIntent;
    }

    private String generateOrderTrackingNumber() {
        // Generate a random UUID number (UUID version-4)
        return UUID.randomUUID().toString();
    }
}
