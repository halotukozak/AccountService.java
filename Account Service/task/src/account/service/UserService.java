package account.service;


import account.exceptions.UserExistException;
import account.exceptions.exceptions.IdenticalPassword;
import account.model.User;
import account.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
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


    public void changePassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) throw new IdenticalPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User createUser(String name, String lastname, String email, String password) {
        if (this.existsEmail(email)) throw new UserExistException();
        User user = new User(name, lastname, email, passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}
