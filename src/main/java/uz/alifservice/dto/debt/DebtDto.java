package uz.alifservice.dto.debt;

import lombok.Getter;
import lombok.Setter;
import uz.alifservice.dto.GenericDto;
import uz.alifservice.dto.auth.user.UserDto;
import uz.alifservice.dto.currency.CurrencyDto;
import uz.alifservice.enums.DebtRole;
import uz.alifservice.enums.DebtStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public class DebtDto extends GenericDto {

    private UserDto user;
    private String fullName;
    private String phoneNumber;
    private BigDecimal totalAmount;
    private String description;
    private String fileHash;
    private ZonedDateTime issueDate;
    private ZonedDateTime dueDate;
    private DebtRole debtRole;
    private CurrencyDto currency;
    private DebtStatus status;
}
