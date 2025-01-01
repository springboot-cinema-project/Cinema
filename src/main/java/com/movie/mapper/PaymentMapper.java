package com.movie.mapper;

import com.movie.domain.Payment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    @Insert("INSERT INTO payment (user_id, payment_name, order_name, order_id, total_amount) VALUES (#{userId}, #{paymentName}, #{orderName}, #{orderId}, #{totalAmount})")
    public long insertPayment(Payment payment);
}
