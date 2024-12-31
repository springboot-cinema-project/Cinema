package com.movie.controller;

import com.movie.domain.Events;
import com.movie.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

	private final EventService eventService;

	@GetMapping("/single")
	public String eventSingle(Model model) {
		List<Events> eventStart = eventService.eventStart();
		List<Events> eventEnd = eventService.eventEnd();
		model.addAttribute("start", eventStart);
		model.addAttribute("end", eventEnd);
		model.addAttribute("content", "events/event_single");
		model.addAttribute("title", "smkun");

		return "layout/base";
	}

	@GetMapping("/view")
	public String eventCategory(Model model, @RequestParam("id") long id) {
		// 이벤트 세부 정보를 가져옴
		Events eventview = eventService.eventDetail(id);

		// 모델에 데이터를 추가
		model.addAttribute("view", eventview); // 올바르게 변수 사용
		model.addAttribute("content", "events/event_view");
		model.addAttribute("title", "smkun2");

		// 레이아웃으로 반환
		return "layout/base";
	}

}
