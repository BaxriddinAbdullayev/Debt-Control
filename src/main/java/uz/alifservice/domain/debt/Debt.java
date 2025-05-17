package uz.alifservice.domain.debt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.alifservice.domain.Auditable;
import uz.alifservice.domain.auth.User;
import uz.alifservice.domain.currency.Currency;
import uz.alifservice.enums.DebtRole;
import uz.alifservice.enums.DebtStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "debts")
public class Debt extends Auditable<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "debt_role")
    @Enumerated(EnumType.STRING)
    private DebtRole debtRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DebtStatus status;
}
