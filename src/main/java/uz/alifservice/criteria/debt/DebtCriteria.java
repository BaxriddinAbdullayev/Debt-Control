package uz.alifservice.criteria.debt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import uz.alifservice.criteria.GenericCriteria;
import uz.alifservice.domain.debt.Debt;
import uz.alifservice.enums.DebtRole;
import uz.alifservice.enums.DebtStatus;
import uz.alifservice.specification.debt.DebtSpecification;

@Getter
@Setter
public class DebtCriteria extends GenericCriteria {

    private DebtRole debtRole;
    private DebtStatus status;
    private String fullName;

    @Builder(builderMethodName = "childBuilder")
    public DebtCriteria(Long id, Integer page, Integer size, Sort.Direction direction, String sort) {
        super(id, page, size, direction, sort);
    }

    public Specification<Debt> toSpecification() {
        return new DebtSpecification(this);
    }
}
