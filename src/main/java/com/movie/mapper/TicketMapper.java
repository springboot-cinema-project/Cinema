package com.movie.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TicketMapper {

    @Insert("INSERT INTO tickets (seat_id, booking_id) VALUES (#{seatId}, #{bookingId})")
    public void insertTicket(long seatId, long bookingId);
}
