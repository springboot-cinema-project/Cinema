package com.movie.domain;

import lombok.Data;

@Data
public class Tickets {

    private Long id;
    private Long ticket_status;
    private Long seatId;
    private Long booking_id;

}
