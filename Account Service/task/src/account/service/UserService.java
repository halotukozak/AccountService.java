package account.service;


import account.db.model.Role;
import account.db.model.User;
import account.db.repository.RoleRepository;
import account.db.repository.UserRepository;
import account.exceptions.role.InvalidRoleCombinationException;
import account.exceptions.role.RemoveAdministratorException;
import account.exceptions.role.RoleNotFoundException;
import account.exceptions.user.EmailNotFoundException;
import account.exceptions.user.IdenticalPassword;
import account.exceptions.user.UserExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public static final int MAX_FAILED_ATTEMPTS = 5;

    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email.toLowerCase());
    }


    public void changePassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) throw new IdenticalPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User registerUser(String name, String lastname, String email, String password) {
        if (this.existsEmail(email)) throw new UserExistsException();
        User user = new User(name, lastname, email, passwordEncoder.encode(password));

        if (userRepository.existsByRoles_Name(Role.ADMIN)) {
            user.giveRole(roleRepository.findByName(Role.USER));
        } else {
            user.giveRole(roleRepository.findByName(Role.ADMIN));
        }
        userRepository.save(user);
        return user;
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }


    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.findByEmail(email.toLowerCase());

        return user.orElse(null);
    }


    public void deleteUserByEmail(String email) throws EmailNotFoundException, RemoveAdministratorException {
        User user = (User) loadUserByUsername(email);
        if (user.isAdmin()) {
            throw new RemoveAdministratorException();
        }
        userRepository.deleteByEmail(email.toLowerCase());

    }

    public List<User> loadAllUsers() {
        return userRepository.findAll();
    }

    public User giveRole(String email, String role_name) {
        User user = (User) loadUserByUsername(email);
        Role role = roleRepository.findByName(role_name);
        if (role == null) throw new RoleNotFoundException();
        if ((role_name.equals(Role.ADMIN) && user.isBusinessUser()) || (Role.BUSINESS.contains(role_name) && user.isAdmin()))
            throw new InvalidRoleCombinationException();
        user.giveRole(role);
        userRepository.save(user);
        return user;
    }

    public User removeRole(String email, String role_name) {
        User user = (User) loadUserByUsername(email);

        Role role = roleRepository.findByName(role_name);
        if (role == null) throw new RoleNotFoundException();
        user.removeRole(role);
        userRepository.save(user);
        return user;

    }

    public boolean increaseFailedAttempts(User user) {
        user.increaseAttempt();
        userRepository.save(user);
        return user.getFailedAttempt() > MAX_FAILED_ATTEMPTS;
    }

    public void lock(User user) {

        user.lock();
        userRepository.save(user);
    }

    public void unlock(User user) {
        user.resetFailedAttempts();
        user.unlock();
        userRepository.save(user);
    }

}

