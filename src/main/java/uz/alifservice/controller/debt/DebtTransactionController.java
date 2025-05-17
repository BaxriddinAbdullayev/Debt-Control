package uz.alifservice.controller.debt;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.alifservice.controller.GenericCrudController;
import uz.alifservice.criteria.debt.DebtTransactionCriteria;
import uz.alifservice.domain.debt.DebtTransaction;
import uz.alifservice.dto.AppResponse;
import uz.alifservice.dto.debt.DebtTransactionCrudDto;
import uz.alifservice.dto.debt.DebtTransactionDto;
import uz.alifservice.mapper.debt.DebtTransactionMapper;
import uz.alifservice.service.debt.DebtTransactionService;
import uz.alifservice.service.message.ResourceBundleService;

@RestController
@AllArgsConstructor
@RequestMapping(value = "${app.api.base-path}", produces = "application/json")
public class DebtTransactionController implements GenericCrudController<DebtTransactionDto, DebtTransactionCrudDto, DebtTransactionCriteria> {

    private final DebtTransactionService service;
    private final DebtTransactionMapper mapper;
    private final ResourceBundleService bundleService;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debt-transactions/{id}", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<DebtTransactionDto>> get(@PathVariable(value = "id") Long id) {
        DebtTransaction entity = service.get(id);
        DebtTransactionDto dto = service.convertToLocalTime(mapper.toDto(entity));
        String message = DebtTransaction.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(dto, message));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debt-transactions", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<Page<DebtTransactionDto>>> list(DebtTransactionCriteria criteria) {
        Page<DebtTransactionDto> dtos = service.list(criteria).map(entity -> service.convertToLocalTime(mapper.toDto(entity)));
        String message = DebtTransaction.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(dtos, message));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debt-transactions", method = RequestMethod.POST)
    public ResponseEntity<AppResponse<DebtTransactionDto>> create(@RequestBody DebtTransactionCrudDto dto) {
        String messsage = DebtTransaction.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("created");
        return new ResponseEntity<>(AppResponse.success(mapper.toDto(service.create(dto)), messsage), HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debt-transactions/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<AppResponse<DebtTransactionDto>> edit(@PathVariable(value = "id") Long id, @RequestBody DebtTransactionCrudDto dto) {
        String messsage = DebtTransaction.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("updated");
        return ResponseEntity.ok(AppResponse.success(mapper.toDto(service.update(id, dto)), messsage));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debt-transactions/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<AppResponse<Boolean>> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        String messsage = DebtTransaction.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("deleted");
        return ResponseEntity.ok(AppResponse.success(true, messsage));
    }
}
