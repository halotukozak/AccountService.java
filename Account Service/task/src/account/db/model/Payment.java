package account.db.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String employee;
    private String period;
    private Long salary;

    public Payment(String employee, String period, Long salary) {
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
