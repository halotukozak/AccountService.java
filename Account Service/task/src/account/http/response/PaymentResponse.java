package account.http.response;

import account.db.model.Payment;
import account.db.model.User;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ResponseBody
@Getter
public class PaymentResponse {
    private final String name;
    final String lastname;
    final String period;
    final String salary;

    public PaymentResponse(Payment payment, User user) {
        this.name = user.getName();
        this.lastname = user.getLastname();
        this.period = YearMonth.parse(payment.getPeriod(), DateTimeFormatter.ofPattern("MM-yyyy")).format(DateTimeFormatter.ofPattern("MMMM-yyyy"));
        this.salary = String.format("%d dollar(s) %d cent(s)", payment.getSalary() / 100, payment.getSalary() % 100);
    }

    public static List<PaymentResponse> of(List<Payment> payments, User user) {
        return payments.stream().map(payment -> PaymentResponse.of(payment, user)).collect(Collectors.toList());
    }

    public static PaymentResponse of(Payment payment, User user) {
        return new PaymentResponse(payment, user);
    }
}
