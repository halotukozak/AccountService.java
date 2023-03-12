package account.service;

import account.db.model.Event;
import account.db.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository paymentRepository) {
        this.eventRepository = paymentRepository;
    }

    public Event registerEvent(LocalDateTime date, Event.ACTION action, String subject, String object, String path) {
        Event event = new Event(date, action, subject, object, path);
        eventRepository.save(event);
        return event;
    }

    public Event registerEvent(Event.ACTION action, String subject, String object, String path) {
        Event event = new Event(LocalDateTime.now(), action, subject, object, path);
        eventRepository.save(event);
        return event;
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }
}
