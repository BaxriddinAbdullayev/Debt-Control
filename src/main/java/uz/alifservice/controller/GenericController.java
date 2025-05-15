package uz.alifservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import uz.alifservice.dto.AppResponse;

public interface GenericController<D, C> {

    ResponseEntity<AppResponse<D>> get(@PathVariable(value = "id") Long id);

    ResponseEntity<AppResponse<Page<D>>> list(C criteria);
}