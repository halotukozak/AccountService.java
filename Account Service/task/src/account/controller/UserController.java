//package account.controller;
//
//import account.model.User;
//import account.repository.UserRepository;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//public class UserController {
////    private final UserRepository repository;
////
////    UserController(UserRepository userRepository) {
////        this.repository = userRepository;
////    }
//
//    private final List<User> userList = new ArrayList<>();
//
//
//    @PostMapping("/api/auth/signup")
//    ResponseEntity<User> register(@RequestBody User user) {
//        if (user.email().isBlank() || user.name().isBlank() || !user.email().matches("@acme.com$"))
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        this.userList.add(user);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
//
//    @PostMapping("/api/auth/changepass")
//    void changePass(@RequestBody User user) {
//
//        this.userList.add(user);
//    }
//
////    @PutMapping("/api/admin/user/role")
////    void changeUserRole(User user){
////        this.userList.get(user.)
////    }
//
//    @DeleteMapping("api/admin/user")
//    void deleteUser(User user) {
//        this.userList.remove(user);
//    }
//
//    @GetMapping("/api/admin/user")
//    public List<User> getUsers() {
//        return this.userList;
//    }
//
//}
