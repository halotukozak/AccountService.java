package account.controller;

import account.db.model.Event;
import account.service.EventService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security/events")
@Validated
public class EventController {
    final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    List<Event> getEvents() {
        return this.eventService.getAll();
    }
}
