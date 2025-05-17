package uz.alifservice.specification.debt;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import uz.alifservice.criteria.debt.DebtCriteria;
import uz.alifservice.domain.debt.Debt;
import uz.alifservice.enums.DebtStatus;
import uz.alifservice.specification.GenericSpecification;

import java.util.Objects;

@RequiredArgsConstructor
public class DebtSpecification extends GenericSpecification<Debt> {

    private final DebtCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Debt> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        super.toPredicate(root, query, criteriaBuilder);

        if (!Objects.isNull(criteria.getFullName())) {
            predicates.add(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")),
                            "%" + criteria.getFullName().toLowerCase() + "%")
            );
        }

        if (!Objects.isNull(criteria.getDebtRole())) {
            predicates.add(criteriaBuilder.equal(root.get("debtRole"), criteria.getDebtRole()));
        }

        if (!Objects.isNull(criteria.getStatus())) {
            predicates.add(criteriaBuilder.equal(root.get("status"), criteria.getStatus()));
        }

        return query
                .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])))
                .getRestriction();
    }
}
