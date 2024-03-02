package com.labs.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class PurchaseResponse {

//    @NonNull // bisa juga menggunakan final
    private final String orderTrackingNumber;
}
