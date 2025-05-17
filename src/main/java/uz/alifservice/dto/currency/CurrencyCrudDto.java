package uz.alifservice.dto.currency;

import lombok.Getter;
import lombok.Setter;
import uz.alifservice.dto.GenericCrudDto;

import java.math.BigDecimal;

@Getter
@Setter
public class CurrencyCrudDto extends GenericCrudDto {

    private String code;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private String symbol;
    private BigDecimal exchangeRate;
    private boolean popular;
    private Integer ordering;
    private String iconUrl;
    private String baseCurrency;
}
