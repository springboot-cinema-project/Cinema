package com.movie.domain;

import lombok.Data;

@Data
public class Payment {

    private Long id;
    private Long userId;
    private String paymentName;
    private String orderName;
    private String orderId;
    private int totalAmount;
}
