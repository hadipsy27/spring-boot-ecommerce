package com.labs.ecommerce.service;

import com.labs.ecommerce.dto.PaymentInfo;
import com.labs.ecommerce.dto.Purchase;
import com.labs.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
