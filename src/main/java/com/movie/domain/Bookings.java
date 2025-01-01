package com.movie.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Bookings {

    private Long id;
    private Long userId;
    private Long scheduleId;
    private Long paymentId;
    private LocalDateTime timestamp;
    private Long couponId;
    private Long price;

}
