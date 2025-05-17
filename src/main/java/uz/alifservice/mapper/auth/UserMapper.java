package uz.alifservice.mapper.auth;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import uz.alifservice.domain.auth.User;
import uz.alifservice.dto.auth.user.UserCrudDto;
import uz.alifservice.dto.auth.user.UserDto;
import uz.alifservice.mapper.BaseCrudMapper;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
        RoleMapper.class
})
public interface UserMapper extends BaseCrudMapper<User, UserDto, UserCrudDto> {

    @Named("toShortInfo")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @BeanMapping(ignoreByDefault = true)
    UserDto toShortInfo(User debt);

    @Named("toFullInfo")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "tempUsername", source = "tempUsername")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "toFullInfo")
    @BeanMapping(ignoreByDefault = true)
    UserDto toFullInfo(User debt);
}
