package com.movie.controller;

import com.movie.domain.*;
import com.movie.service.MovieService;
import com.movie.service.ScheduleService;
import com.movie.service.SeatService;
import com.movie.service.UserService;
import com.movie.util.RandomIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private RandomIdUtil randomIdUtil;

    @Value("${portone.storeId}")
    private String storeId;

    @Value("${portone.channelKey}")
    private String channelKey;


    @PostMapping("/proceed")
    public String bookingProceed(@RequestParam("selectedSeatIds") String selectedSeatId, Model model, @RequestParam("scheduleId") long scheduleId, @ModelAttribute("sessionUser") SessionUser sessionUser) {

        if (sessionUser != null) {
            // 문자열을 Long 배열로 변환
            String[] seatIdStrings = selectedSeatId.split(",");
            Long[] seatIds = Arrays.stream(seatIdStrings)
                    .map(Long::valueOf) // 문자열을 Long으로 변환
                    .toArray(Long[]::new); // Long[] 배열로 변환

            seatService.bookingStates(seatIds);

            User user = userService.getUserInfo(sessionUser.getId());
            List<Seats> seats = seatService.bookingSeats(seatIds);
            Schedules schedule = scheduleService.getSchedule(scheduleId);
            Movies movie = movieService.movieInfo(schedule.getMovieId());

            int totalPrice = 0;

            for (Seats seat : seats) {
                totalPrice += seat.getSeatPrice();
            }

            // orderName에 들어갈 seatColumn + seatRow
            List<String> seatNames = seats.stream()
                    .map(s -> String.valueOf(s.getSeatColumn()) + String.valueOf(s.getSeatRow()))
                    .collect(Collectors.toList());


            // customData.item에 들어갈 seat.id
            List<Long> seatIdList = seats.stream()
                    .map(Seats::getId)
                    .collect(Collectors.toList());

            String paymentId = randomIdUtil.makePaymentId();

            model.addAttribute("user", user);
            model.addAttribute("seats", seats);
            model.addAttribute("schedule", schedule);
            model.addAttribute("movie", movie);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("storeId", storeId);
            model.addAttribute("channelKey", channelKey);
            model.addAttribute("paymentId", paymentId);

            model.addAttribute("seatNames", seatNames);
            model.addAttribute("seatIdList", seatIdList);

        } else {
            return "redirect:/";
        }
        return "booking/booking_type";

    }

    @PostMapping("/pay")
    public String doPayment() {

        return "booking/booking_type";
    }
}
