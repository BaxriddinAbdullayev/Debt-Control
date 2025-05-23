package uz.alifservice.controller.currency;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.alifservice.controller.GenericCrudController;
import uz.alifservice.criteria.currency.CurrencyCriteria;
import uz.alifservice.domain.currency.Currency;
import uz.alifservice.dto.AppResponse;
import uz.alifservice.dto.currency.CurrencyCrudDto;
import uz.alifservice.dto.currency.CurrencyDto;
import uz.alifservice.mapper.currency.CurrencyMapper;
import uz.alifservice.service.currency.CurrencyService;
import uz.alifservice.service.message.ResourceBundleService;

@RestController
@AllArgsConstructor
@RequestMapping(value = "${app.api.base-path}", produces = "application/json")
public class CurrencyController implements GenericCrudController<CurrencyDto, CurrencyCrudDto, CurrencyCriteria> {

    private final CurrencyService service;
    private final CurrencyMapper mapper;
    private final ResourceBundleService bundleService;

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/currencies/{id}", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<CurrencyDto>> get(@PathVariable(value = "id") Long id) {
        String message = Currency.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(mapper.toDto(service.get(id)), message));
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @RequestMapping(value = "/currencies", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<Page<CurrencyDto>>> list(CurrencyCriteria criteria) {
        String message = Currency.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(service.list(criteria).map(mapper::toDto), message));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/currencies", method = RequestMethod.POST)
    public ResponseEntity<AppResponse<CurrencyDto>> create(@RequestBody CurrencyCrudDto dto) {
        String message = Currency.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("created");
        return new ResponseEntity<>(AppResponse.success(mapper.toDto(service.create(dto)), message), HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/currencies/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<AppResponse<CurrencyDto>> edit(@PathVariable(value = "id") Long id, @RequestBody CurrencyCrudDto dto) {
        String message = Currency.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("updated");
        return ResponseEntity.ok(AppResponse.success(mapper.toDto(service.update(id, dto)), message));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/currencies/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<AppResponse<Boolean>> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        String message = Currency.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("deleted");
        return ResponseEntity.ok(AppResponse.success(true, message));
    }
}
