package uz.alifservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import uz.alifservice.dto.AppResponse;

public interface GenericCrudController<D, CD, C> {

    ResponseEntity<AppResponse<D>> get(@PathVariable(value = "id") Long id);

    ResponseEntity<AppResponse<Page<D>>> list(C criteria);

    ResponseEntity<AppResponse<D>> create(@RequestBody CD dto);

    ResponseEntity<AppResponse<D>> edit(@PathVariable(value = "id") Long id, @RequestBody CD dto);

    ResponseEntity<AppResponse<Boolean>> delete(@PathVariable(value = "id") Long id);
}
