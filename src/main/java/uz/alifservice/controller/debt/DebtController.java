package uz.alifservice.controller.debt;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.alifservice.controller.GenericCrudController;
import uz.alifservice.criteria.debt.DebtCriteria;
import uz.alifservice.domain.debt.Debt;
import uz.alifservice.dto.AppResponse;
import uz.alifservice.dto.debt.DebtCrudDto;
import uz.alifservice.dto.debt.DebtDto;
import uz.alifservice.mapper.debt.DebtMapper;
import uz.alifservice.service.debt.DebtService;
import uz.alifservice.service.message.ResourceBundleService;

@RestController
@AllArgsConstructor
@RequestMapping(value = "${app.api.base-path}", produces = "application/json")
public class DebtController implements GenericCrudController<DebtDto, DebtCrudDto, DebtCriteria> {

    private final DebtService service;
    private final DebtMapper mapper;
    private final ResourceBundleService bundleService;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debts/{id}", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<DebtDto>> get(@PathVariable(value = "id") Long id) {
        String message = Debt.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(mapper.toDto(service.get(id)), message));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debts", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<Page<DebtDto>>> list(DebtCriteria criteria) {
        String message = Debt.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(service.list(criteria).map(mapper::toDto), message));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debts", method = RequestMethod.POST)
    public ResponseEntity<AppResponse<DebtDto>> create(@RequestBody DebtCrudDto dto) {
        String message = Debt.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("created");
        return new ResponseEntity<>(AppResponse.success(mapper.toDto(service.create(dto)), message), HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debts/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<AppResponse<DebtDto>> edit(@PathVariable(value = "id") Long id, @RequestBody DebtCrudDto dto) {
        String message = Debt.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("updated");
        return ResponseEntity.ok(AppResponse.success(mapper.toDto(service.update(id, dto)), message));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/debts/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<AppResponse<Boolean>> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        String message = Debt.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("deleted");
        return ResponseEntity.ok(AppResponse.success(true, message));
    }
}
