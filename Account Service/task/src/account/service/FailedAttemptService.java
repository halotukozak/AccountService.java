package account.service;

import account.db.model.Event;
import account.db.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class FailedAttemptService {
    private UserService userService;
    private EventService eventService;

    public void increaseFailedAttempt(String email, String path) {
        User user = (User) userService.loadUserByUsername(email);
        if (user == null) eventService.registerEvent(Event.ACTION.LOGIN_FAILED, email, path);

        if (user != null && user.isAccountNonLocked()) {
            eventService.registerEvent(Event.ACTION.LOGIN_FAILED, email, path);
            boolean tooManyAttempts = userService.increaseFailedAttempts(user);
            if (tooManyAttempts) {
                eventService.registerEvent(Event.ACTION.BRUTE_FORCE, email, path);
                lock(user, user.getEmail());
            }
        }
    }

    public void lock(User user, String subject) {
        userService.lock(user);
        eventService.registerEvent(Event.ACTION.LOCK_USER, subject, "Lock user " + user.getEmail());
    }

    public void unlock(User user, String subject) {
        userService.unlock(user);
        eventService.registerEvent(Event.ACTION.UNLOCK_USER, subject, "Unlock user " + user.getEmail());
    }


}
