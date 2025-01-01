package com.movie.service;

import com.movie.domain.Payment;
import com.movie.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    public long insertPayment(Payment payment) {
        return paymentMapper.insertPayment(payment);
    }
}
