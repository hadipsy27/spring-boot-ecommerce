package com.labs.ecommerce.dto;

import com.labs.ecommerce.entity.Address;
import com.labs.ecommerce.entity.Customer;
import com.labs.ecommerce.entity.Order;
import com.labs.ecommerce.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
