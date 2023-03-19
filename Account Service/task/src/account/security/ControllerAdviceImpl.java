package account.security;


import account.db.model.Event;
import account.db.model.User;
import account.exceptions.CustomException;
import account.http.response.ErrorResponse;
import account.service.EventService;
import account.service.UserService;
import org.h2.security.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerAdviceImpl {

//    int status;
//    String timestamp;
//    String path;
//    String message;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

//    private void registerEvent(String email, String path) {
//        User user = (User) userService.loadUserByUsername(email);
//        if (user != null && user.isAccountNonLocked())
//            userService.increaseFailedAttempts(user);
//
//        eventService.registerEvent(Event.ACTION.LOGIN_FAILED, email, path);
//    }


//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(TransactionSystemException.class)
//    public ResponseEntity<ErrorResponse> constraintViolationException(
//            TransactionSystemException e, HttpServletRequest request) {
//
//        status = HttpStatus.BAD_REQUEST.value();
//        timestamp = LocalDateTime.now().toString();
//        message = e.getMessage();
//        path = request.getServletPath();
//
//
//        return new ResponseEntity<>(
//                new ErrorResponse(
//                        timestamp,
//                        status,
//                        "Bad Request",
//                        message,
//                        path)
//                , HttpStatus.BAD_REQUEST
//        );
//    }


    //    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleExceptions(
//            CustomException e, HttpServletRequest request) {
//        status = e.getStatus();
//        timestamp = LocalDateTime.now().toString();
//        path = request.getContextPath();
//
//        registerEvent(request.getHeader("email"), request.getServletPath());
//
//        return new ResponseEntity<>(
//                new ErrorResponse(
//                        timestamp,
//                        status,
//                        e.getError(),
//                        e.getMessage(),
//                        path)
//                , HttpStatus.valueOf(status)
//        );
//    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customHandle(Exception ex,
                                         HttpServletRequest request, HttpServletResponse response) {
        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex,
                                         HttpServletRequest request, HttpServletResponse response) {
        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}