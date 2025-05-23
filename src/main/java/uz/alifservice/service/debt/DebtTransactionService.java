package uz.alifservice.service.debt;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.alifservice.criteria.debt.DebtTransactionCriteria;
import uz.alifservice.domain.debt.Debt;
import uz.alifservice.domain.debt.DebtTransaction;
import uz.alifservice.dto.debt.DebtTransactionCrudDto;
import uz.alifservice.enums.DebtRole;
import uz.alifservice.exps.AppBadException;
import uz.alifservice.mapper.debt.DebtTransactionMapper;
import uz.alifservice.repository.debt.DebtRepository;
import uz.alifservice.repository.debt.DebtTransactionRepository;
import uz.alifservice.service.GenericCrudService;
import uz.alifservice.service.message.ResourceBundleService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class DebtTransactionService implements GenericCrudService<DebtTransaction, DebtTransactionCrudDto, DebtTransactionCriteria> {

    private final DebtTransactionRepository repository;
    private final DebtTransactionMapper mapper;
    private final ResourceBundleService bundleService;
    private final DebtRepository debtRepository;

    @Value("${app.default-time-zone}")
    private String defaultTimeZone;

    @Override
    @Transactional(readOnly = true)
    public DebtTransaction get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DebtTransaction> list(DebtTransactionCriteria criteria) {
        return repository.findAll(criteria.toSpecification(),
                PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by(criteria.getDirection(), criteria.getSort())));
    }

    @Override
    @Transactional
    public DebtTransaction create(DebtTransactionCrudDto dto) {
        Optional<Debt> optional = debtRepository.findById(dto.getDebt().getId());
        if (optional.isEmpty()) throw new EntityNotFoundException(String.valueOf(id));

        Debt debtEntity = optional.get();
        DebtTransaction entity = repository.save(mapper.fromCreateDto(dto));

        BigDecimal calculateTotalAmount = calculateDebtAmount(debtEntity, dto);
        debtEntity.setTotalAmount(calculateTotalAmount.abs());
        debtRepository.save(debtEntity);
        return entity;
    }

    @Override
    @Transactional
    public DebtTransaction update(Long id, DebtTransactionCrudDto dto) {
        DebtTransaction entity = get(id);
        return repository.save(mapper.fromUpdate(dto, entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        DebtTransaction entity = get(id);
        entity.setDeleted(true);
    }

    public DebtTransaction createDebtTransaction(DebtTransactionCrudDto dto) {
        return repository.save(mapper.fromCreateDto(dto));
    }

    private BigDecimal calculateDebtAmount(Debt debtEntity, DebtTransactionCrudDto dto) {
        BigDecimal calculateTotalAmount = debtEntity.getTotalAmount();
        switch (debtEntity.getDebtRole()) {
            case LEND -> {
                switch (dto.getAction()) {
                    case GAVE -> calculateTotalAmount = calculateTotalAmount.add(dto.getAmount());
                    case RECEIVED -> {
                        calculateTotalAmount = calculateTotalAmount.subtract(dto.getAmount());
                        if (calculateTotalAmount.compareTo(BigDecimal.ZERO) < 0) {
                            debtEntity.setDebtRole(DebtRole.BORROW);
                        }
                        if (calculateTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
                            debtEntity.setDebtRole(DebtRole.DEBT_PAID_OFF);
                        }
                    }
                    // todo default boshqa status kelishi mumkin. masalan took default yozish kerak
                }
            }
            case BORROW -> {
                switch (dto.getAction()) {
                    case TOOK -> calculateTotalAmount = calculateTotalAmount.add(dto.getAmount());
                    case REPAID -> {
                        calculateTotalAmount = calculateTotalAmount.subtract(dto.getAmount());
                        if (calculateTotalAmount.compareTo(BigDecimal.ZERO) < 0) {
                            debtEntity.setDebtRole(DebtRole.LEND);
                        }
                        if (calculateTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
                            debtEntity.setDebtRole(DebtRole.DEBT_PAID_OFF);
                        }
                    }
                    // todo default boshqa status kelishi mumkin. masalan took default yozish kerak
                }
            }
            case DEBT_PAID_OFF -> {
                switch (dto.getAction()) {
                    case GAVE -> debtEntity.setDebtRole(DebtRole.LEND);
                    case TOOK -> debtEntity.setDebtRole(DebtRole.BORROW);
                    // todo default boshqa status kelishi mumkin. masalan took default yozish kerak
                }
                calculateTotalAmount = calculateTotalAmount.add(dto.getAmount());
            }
            default -> throw new AppBadException(bundleService.getMessage("status.wrong"));
        }
        return calculateTotalAmount;
    }
}
