package account.db.repository;

import account.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    boolean existsByRoles_Name(String role_Name);


    Long deleteByEmail(String toLowerCase);


}