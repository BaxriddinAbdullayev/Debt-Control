package uz.alifservice.mapper.currency;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import uz.alifservice.domain.currency.Currency;
import uz.alifservice.dto.currency.CurrencyCrudDto;
import uz.alifservice.dto.currency.CurrencyDto;
import uz.alifservice.mapper.BaseCrudMapper;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CurrencyMapper extends BaseCrudMapper<Currency, CurrencyDto, CurrencyCrudDto> {

    @Named("toFullInfo")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "nameUz", source = "nameUz")
    @Mapping(target = "nameRu", source = "nameRu")
    @Mapping(target = "nameEn", source = "nameEn")
    @Mapping(target = "symbol", source = "symbol")
    @Mapping(target = "exchangeRate", source = "exchangeRate")
    @Mapping(target = "popular", source = "popular")
    @Mapping(target = "ordering", source = "ordering")
    @Mapping(target = "iconUrl", source = "iconUrl")
    @Mapping(target = "baseCurrency", source = "baseCurrency")
    @BeanMapping(ignoreByDefault = true)
    CurrencyDto toFullInfo(Currency debt);

}
