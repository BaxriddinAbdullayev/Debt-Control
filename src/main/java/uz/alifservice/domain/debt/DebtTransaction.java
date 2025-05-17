package uz.alifservice.domain.debt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.alifservice.domain.Auditable;
import uz.alifservice.enums.DebtAction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "debt_transactions")
public class DebtTransaction extends Auditable<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debt_id", referencedColumnName = "id")
    private Debt debt;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

    @Column(name = "issue_date")
    private ZonedDateTime issueDate;

    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "file_hash")
    private String fileHash;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private DebtAction action;
}
