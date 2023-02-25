package account.service;


import account.exceptions.BreachedPassword;
import account.exceptions.IdenticalPassword;
import account.exceptions.TooShortPassword;
import account.exceptions.UserExistException;
import account.model.User;
import account.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email.toLowerCase());

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("Email[" + email + "] not found");
        }
    }

    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email.toLowerCase());
    }

    public void validatePassword(String password) {
        List<String> passwords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril", "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust", "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
        if (password.length() < 12) throw new TooShortPassword();
        if (passwords.contains(password)) throw new BreachedPassword();
    }

    public void changePassword(User user, String newPassword) {
        validatePassword(newPassword);
        if (passwordEncoder.matches(newPassword, user.getPassword())) throw new IdenticalPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User createUser(String name, String lastname, String email, String password) {
        if (this.existsEmail(email)) throw new UserExistException();
        validatePassword(password);
        User user = new User(name, lastname, email, passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }
}
