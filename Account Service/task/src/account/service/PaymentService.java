package account.service;

import account.db.model.Payment;
import account.db.repository.PaymentRepository;
import account.exceptions.payment.NoSuchPaymentException;
import account.exceptions.payment.OccupiedPeriodException;
import account.http.request.PaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final EventService eventService;


    public Payment getPayment(String employee, String period) {
        Payment result = paymentRepository.findByEmployeeAndPeriod(employee, period);
        if (result == null) throw new NoSuchPaymentException();
        return result;
    }

    public List<Payment> getPayment(String employee) {
        return paymentRepository.findAllByEmployeeOrderByPeriodDesc(employee);
    }

    public void createPayment(String employee, String period, Long salary) {
        if (!paymentRepository.existsByEmployeeAndPeriod(employee, period)) {
            paymentRepository.save(new Payment(employee, period, salary));
        } else throw new OccupiedPeriodException();
    }

    public void createPayments(List<@Valid PaymentRequest> payments) {
        payments.forEach(payment -> createPayment(payment.employee(), payment.period(), payment.salary()));
    }

    public void deleteAll() {
        paymentRepository.deleteAll();
    }

    public void updatePayment(String employee, String period, Long salary) {
        Payment payment = paymentRepository.findByEmployeeAndPeriod(employee, period);
        if (payment == null) {
            payment = new Payment(employee, period, salary);
        } else {
            payment.setSalary(salary);
        }
        paymentRepository.save(payment);
    }
}
