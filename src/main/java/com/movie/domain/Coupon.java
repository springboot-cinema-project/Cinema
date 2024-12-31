package com.movie.domain;

import lombok.Data;

@Data
public class Coupon {

    private Long id;
    private String couponTitle;
    private String couponType;
    private Integer couponPrice;
}
