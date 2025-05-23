package uz.alifservice.controller.auth;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.alifservice.controller.GenericCrudController;
import uz.alifservice.criteria.auth.RoleCriteria;
import uz.alifservice.domain.auth.Role;
import uz.alifservice.dto.AppResponse;
import uz.alifservice.dto.auth.role.RoleCrudDto;
import uz.alifservice.dto.auth.role.RoleDto;
import uz.alifservice.mapper.auth.RoleMapper;
import uz.alifservice.service.auth.RoleService;
import uz.alifservice.service.message.ResourceBundleService;

@RestController
@AllArgsConstructor
@RequestMapping(value = "${app.api.base-path}", produces = "application/json")
public class RoleController implements GenericCrudController<RoleDto, RoleCrudDto, RoleCriteria> {

    private final RoleService service;
    private final RoleMapper mapper;
    private final ResourceBundleService bundleService;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<RoleDto>> get(@PathVariable(value = "id") Long id) {
        String message = Role.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(mapper.toDto(service.get(id)), message));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<Page<RoleDto>>> list(RoleCriteria criteria) {
        String message = Role.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(service.list(criteria).map(mapper::toDto), message));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public ResponseEntity<AppResponse<RoleDto>> create(@RequestBody RoleCrudDto dto) {
        String message = Role.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("created");
        return new ResponseEntity<>(AppResponse.success(mapper.toDto(service.create(dto)), message), HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/roles/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<AppResponse<RoleDto>> edit(@PathVariable(value = "id") Long id, @RequestBody RoleCrudDto dto) {
        String message = Role.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("updated");
        return ResponseEntity.ok(AppResponse.success(mapper.toDto(service.update(id, dto)), message));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<AppResponse<Boolean>> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        String message = Role.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("deleted");
        return ResponseEntity.ok(AppResponse.success(true, message));
    }
}
