package uz.alifservice.dto.debt;

import lombok.Getter;
import lombok.Setter;
import uz.alifservice.dto.GenericCrudDto;
import uz.alifservice.enums.DebtAction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public class DebtTransactionCrudDto extends GenericCrudDto {

    private DebtDto debt;
    private BigDecimal amount;
    private String description;
    private ZonedDateTime issueDate;
    private ZonedDateTime dueDate;
    private String fileHash;
    private DebtAction action;
}
