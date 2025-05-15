package uz.alifservice.controller.auth;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.alifservice.controller.GenericCrudController;
import uz.alifservice.criteria.auth.UserCriteria;
import uz.alifservice.domain.auth.User;
import uz.alifservice.dto.AppResponse;
import uz.alifservice.dto.auth.user.UserCrudDto;
import uz.alifservice.dto.auth.user.UserDto;
import uz.alifservice.mapper.auth.UserMapper;
import uz.alifservice.service.auth.UserService;
import uz.alifservice.service.message.ResourceBundleService;

@RestController
@AllArgsConstructor
@RequestMapping(value = "${app.api.base-path}", produces = "application/json")
public class UserController implements GenericCrudController<UserDto, UserCrudDto, UserCriteria> {

    private final UserService service;
    private final UserMapper mapper;
    private final ResourceBundleService bundleService;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<UserDto>> get(@PathVariable(value = "id") Long id) {
        String messsage = User.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(mapper.toDto(service.get(id)), messsage));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<AppResponse<Page<UserDto>>> list(UserCriteria criteria) {
        String messsage = User.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("retrieved");
        return ResponseEntity.ok(AppResponse.success(service.list(criteria).map(mapper::toDto), messsage));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<AppResponse<UserDto>> create(@RequestBody UserCrudDto dto) {
        String messsage = User.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("created");
        return new ResponseEntity<>(AppResponse.success(mapper.toDto(service.create(dto)), messsage), HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<AppResponse<UserDto>> edit(@PathVariable(value = "id") Long id, @RequestBody UserCrudDto dto) {
        String messsage = User.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("updated");
        return ResponseEntity.ok(AppResponse.success(mapper.toDto(service.update(id, dto)), messsage));
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<AppResponse<Boolean>> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        String messsage = User.class.getSimpleName() + " " + bundleService.getSuccessCrudMessage("deleted");
        return ResponseEntity.ok(AppResponse.success(true, messsage));
    }
}
