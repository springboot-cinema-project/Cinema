package com.movie.service;

import com.movie.domain.Bookings;
import com.movie.domain.Seats;
import com.movie.mapper.BookingMapper;
import com.movie.mapper.TicketMapper;
import com.movie.mapper.UserCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private TicketMapper ticketMapper;

    public void insertBooking(Long[] seatsId, long userId, long scheduleId, long couponId, long price) {

        Bookings bookings = new Bookings();

        bookings.setUserId(userId);
        bookings.setScheduleId(scheduleId);
        bookings.setCouponId(couponId);
        bookings.setPrice(price);

        bookingMapper.insertBookingWithCoupon(bookings);
        userCouponMapper.updateCouponState(userId, couponId);

        for(Long seatId : seatsId) {
            ticketMapper.insertTicket(seatId, bookings.getId());
        }

    }

    public void insertBooking(Long[] seatsId, long userId, long scheduleId, long price) {

        Bookings bookings = new Bookings();

        bookings.setUserId(userId);
        bookings.setScheduleId(scheduleId);
        bookings.setPrice(price);

        bookingMapper.insertBooking(bookings);

        for(Long seatId : seatsId) {
            ticketMapper.insertTicket(seatId, bookings.getId());
        }

    }
}
