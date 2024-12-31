package com.movie.controller;

import com.movie.domain.*;
import com.movie.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {

    private final ScheduleService scheduleService;
    private final MovieService movieService;
    private final SeatService seatService;
    private final MovieDetailService movieDetailService;
    private final UserService userService;
    private final BookingService bookingService;

    @GetMapping("/schedule")
    public String movieScheduleList(long movieId, Model model) {

        List<ScreenScheduleSeatDto> schedules = scheduleService.movieSchedulesList(movieId);
        Movies movies = movieService.movieInfo(movieId);
        MovieDetails movieDetails = movieDetailService.getMovieDetail(movieId);

        Set<LocalDate> dates = schedules.stream()
                .map(ScreenScheduleSeatDto::getWatchDate)
                .collect(Collectors.toCollection(TreeSet::new));

        List<String> desiredFormats = List.of("2D", "3D", "4DX", "IMAX");

        // 스케줄에서 사용 가능한 형식 추출 (중복 없이)
        Set<String> availableFormats = schedules.stream()
                .map(ScreenScheduleSeatDto::getScreenName)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // 원하는 순서대로 필터링된 형식 리스트 생성
        List<String> formats = desiredFormats.stream()
                .filter(availableFormats::contains)
                .collect(Collectors.toList());

        model.addAttribute("content", "movies/movie_booking");
        model.addAttribute("title", "スケジュール");
        model.addAttribute("schedules", schedules);
        model.addAttribute("movie", movies);
        model.addAttribute("detail", movieDetails);
        model.addAttribute("dates", dates);
        model.addAttribute("formats", formats);

        return "layout/base";

    }

    @GetMapping("/seat")
    public String getSeat(long scheduleId, Model model) {
        ScreenScheduleSeatDto seats = seatService.getSeat(scheduleId);
        Movies movies = movieService.movieInfo(seats.getMovieId());

        // 좌석 데이터를 열별로 그룹화 (seatColumn을 String으로 변환)
        Map<String, List<Seats>> groupedSeats = seats.getSeatList().stream()
                .collect(Collectors.groupingBy(seat -> String.valueOf(seat.getSeatColumn())));

        model.addAttribute("groupedSeats", groupedSeats); // 그룹화된 데이터를 추가
        model.addAttribute("movie", movies);
        model.addAttribute("schedule", seats);
        model.addAttribute("scheduleId", scheduleId);

        return "movies/seat_booking";
    }

    @PostMapping("/checkSeatState")
    public ResponseEntity<Map<String, String>> checkSeatState(@RequestBody List<Long> seatIds) {

        long result = seatService.checkSeatState(seatIds);

        Map<String, String> response = new HashMap<>();
        if (result == 0) {
            response.put("state", "possible");
        } else {
            response.put("state", "impossible");
        }

        return ResponseEntity.ok(response);
    }

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

            model.addAttribute("user", user);
            model.addAttribute("seats", seats);
            model.addAttribute("schedule", schedule);
            model.addAttribute("movie", movie);
            model.addAttribute("totalPrice", totalPrice);

        } else {
            return "redirect:/";
        }
        return "booking/booking_type";

    }

    @PostMapping("/payment")
    public String payment(Model model, @RequestParam Long[] seatsId, @RequestParam(required = false) Long couponId, @RequestParam(required = false) Long totalPrice, @RequestParam Long scheduleId, @RequestParam Long movieId, @ModelAttribute("sessionUser") SessionUser sessionUser) {
        System.out.println("쿠폰아이디 넘어옴" + couponId);
        if (sessionUser != null) {
            if (couponId != null) {
                bookingService.insertBooking(seatsId, sessionUser.getId(), scheduleId, couponId, totalPrice);
            } else {
                bookingService.insertBooking(seatsId, sessionUser.getId(), scheduleId, totalPrice);
            }

            Movies movies = movieService.movieInfo(movieId);
            Schedules schedules = scheduleService.getSchedule(scheduleId);
            List<Seats> seats = seatService.bookingSeats(seatsId);

            model.addAttribute("content", "booking/confirmation_screen");
            model.addAttribute("title", "決済確認");
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("seats", seats);
            model.addAttribute("schedule", schedules);
            model.addAttribute("movie", movies);

        } else {
            return "redirect:/";
        }

        return "layout/base";

    }
}
