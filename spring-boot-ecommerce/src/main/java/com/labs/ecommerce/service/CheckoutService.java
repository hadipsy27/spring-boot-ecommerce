package com.labs.ecommerce.service;

import com.labs.ecommerce.dto.Purchase;
import com.labs.ecommerce.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}
