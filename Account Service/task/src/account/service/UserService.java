package account.service;


import account.db.repository.RoleRepository;
import account.db.repository.UserRepository;
import account.exceptions.UserExistException;
import account.exceptions.exceptions.IdenticalPassword;
import account.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean isAdminSet = false;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email.toLowerCase());
    }


    public void changePassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) throw new IdenticalPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User registerUser(String name, String lastname, String email, String password) {
        if (this.existsEmail(email)) throw new UserExistException();
        User user = new User(name, lastname, email, passwordEncoder.encode(password));
        user.giveRole(roleRepository.findByName("ROLE_USER"));
        if (!isAdminSet) {
            user.giveRole(roleRepository.findByName("USER_ADMIN"));
            isAdminSet = true;
        }
        userRepository.save(user);
        return user;
    }

    public void deleteAll() {
        userRepository.deleteAll();
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


    public void deleteUserByEmail(String email) throws UsernameNotFoundException {
        Long count = userRepository.deleteByEmail(email.toLowerCase());

        if (count == 0) {
            throw new UsernameNotFoundException("Email[" + email + "] not found");
        }
    }

}

