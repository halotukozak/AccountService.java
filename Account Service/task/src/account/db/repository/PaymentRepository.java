package account.db.repository;

import account.db.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    Payment findByEmployeeAndPeriod(String employee, String period);

    boolean existsByEmployeeAndPeriod(String employee, String period);

    List<Payment> findAllByEmployeeOrderByPeriodDesc(String employee);
}