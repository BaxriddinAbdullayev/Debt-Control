package uz.alifservice.mapper.auth;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import uz.alifservice.domain.auth.Role;
import uz.alifservice.domain.auth.User;
import uz.alifservice.dto.auth.role.RoleCrudDto;
import uz.alifservice.dto.auth.role.RoleDto;
import uz.alifservice.dto.auth.user.UserDto;
import uz.alifservice.mapper.BaseCrudMapper;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseCrudMapper<Role, RoleDto, RoleCrudDto> {

    @Named("toFullInfo")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "roleName", source = "roleName")
    @Mapping(target = "displayName", source = "displayName")
    @BeanMapping(ignoreByDefault = true)
    RoleDto toFullInfo(Role debt);
}
