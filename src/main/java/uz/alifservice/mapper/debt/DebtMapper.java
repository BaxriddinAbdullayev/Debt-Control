package uz.alifservice.mapper.debt;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import uz.alifservice.domain.debt.Debt;
import uz.alifservice.dto.debt.DebtCrudDto;
import uz.alifservice.dto.debt.DebtDto;
import uz.alifservice.mapper.BaseCrudMapper;
import uz.alifservice.mapper.auth.UserMapper;
import uz.alifservice.mapper.currency.CurrencyMapper;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
        UserMapper.class,
        CurrencyMapper.class
})
public interface DebtMapper extends BaseCrudMapper<Debt, DebtDto, DebtCrudDto> {

    @Named("toFullInfo")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "debtRole", source = "debtRole")
    @Mapping(target = "currency", source = "currency", qualifiedByName = "toFullInfo")
    @Mapping(target = "user", source = "user", qualifiedByName = "toFullInfo")
    @BeanMapping(ignoreByDefault = true)
    DebtDto toFullInfo(Debt debt);
}
