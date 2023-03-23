package account.service;

import account.db.model.Event;
import account.db.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final HttpServletRequest request;


    public Event registerEvent(Event.ACTION action, String subject, String object) {
        Event event = new Event(LocalDateTime.now(), action, subject, object, request.getServletPath());
        eventRepository.save(event);
        return event;
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }
}
