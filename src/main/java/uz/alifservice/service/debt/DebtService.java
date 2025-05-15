package uz.alifservice.service.debt;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.alifservice.criteria.debt.DebtCriteria;
import uz.alifservice.domain.debt.Debt;
import uz.alifservice.dto.debt.DebtCrudDto;
import uz.alifservice.dto.debt.DebtDto;
import uz.alifservice.dto.debt.DebtTransactionCrudDto;
import uz.alifservice.enums.DebtAction;
import uz.alifservice.enums.DebtRole;
import uz.alifservice.mapper.debt.DebtMapper;
import uz.alifservice.repository.debt.DebtRepository;
import uz.alifservice.service.GenericCrudService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebtService implements GenericCrudService<Debt, DebtCrudDto, DebtCriteria> {

    private final DebtRepository repository;
    private final DebtMapper mapper;
    private final DebtTransactionService debtTransactionService;

    @Override
    @Transactional(readOnly = true)
    public Debt get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Debt> list(DebtCriteria criteria) {
        return repository.findAll(criteria.toSpecification(),
                PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by(criteria.getDirection(), criteria.getSort())));
    }

    @Override
    @Transactional
    public Debt create(DebtCrudDto dto) {
        Debt debtEntity = repository.save(mapper.fromCreateDto(dto));
        DebtDto debtDto = mapper.toDto(debtEntity);

        DebtTransactionCrudDto transactionCrudDto = new DebtTransactionCrudDto();
        transactionCrudDto.setDebt(debtDto);
        transactionCrudDto.setAmount(dto.getTotalAmount());
        transactionCrudDto.setDescription(dto.getDescription());
        transactionCrudDto.setFileHash(dto.getFileHash());
        if (dto.getDebtRole() == DebtRole.LEND) {
            transactionCrudDto.setAction(DebtAction.GAVE);
        } else if (dto.getDebtRole() == DebtRole.BORROW) {
            transactionCrudDto.setAction(DebtAction.TOOK);
        }
        transactionCrudDto.setIssueDate(dto.getIssueDate());
        transactionCrudDto.setDueDate(dto.getDueDate());
        debtTransactionService.createDebtTransaction(transactionCrudDto);

        return debtEntity;
    }

    @Override
    @Transactional
    public Debt update(Long id, DebtCrudDto dto) {
        Debt entity = get(id);
        return repository.save(mapper.fromUpdate(dto, entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Debt entity = get(id);
        entity.setDeleted(true);
    }

    public Debt getDebtById(Long id) {
        Optional<Debt> optional = repository.findById(id);
        if (optional.isEmpty()) throw new EntityNotFoundException(String.valueOf(id));
        return optional.get();
    }
}
