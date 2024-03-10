package com.labs.ecommerce.controller;

import com.labs.ecommerce.dto.PaymentInfo;
import com.labs.ecommerce.dto.Purchase;
import com.labs.ecommerce.dto.PurchaseResponse;
import com.labs.ecommerce.service.CheckoutService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    // Set your Stripe API key
    static {
        Stripe.apiKey = "sk_test_51Os4Q7GSA92kQyV7FmgrO86QzX5dM2cZk4YIL0zT6y3aRzLU2Cy4g50YuvzZV6zI2bv1RszqLxl2cEpdY1NyuSOI00uKGluCWu";
    }


    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase){
        PurchaseResponse purchaseResponse = checkoutService.placeOrder(purchase);
        return purchaseResponse;
    }


    @PostMapping("/payment-intent")
    public ResponseEntity<String> createpaymentIntent(@RequestBody PaymentInfo paymentInfo) throws StripeException {
        PaymentIntent paymentIntent = checkoutService.createPaymentIntent(paymentInfo);
        String paymentStr = paymentIntent.toJson();
        return ResponseEntity.ok(paymentStr);
    }
    @PostMapping("/{id}/confirm-payment-intent")
    public ResponseEntity<String> confirmPaymentIntent(@RequestParam("id") String paymentIntentId) throws StripeException{
        PaymentIntent paymentIntent = checkoutService.confirmPaymentIntent(paymentIntentId);
        String confirmPaymentIntent = paymentIntent.toJson();
        return ResponseEntity.ok(confirmPaymentIntent);
    }
}
