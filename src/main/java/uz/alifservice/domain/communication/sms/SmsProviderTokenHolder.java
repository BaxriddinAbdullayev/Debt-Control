package uz.alifservice.domain.communication.sms;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import uz.alifservice.domain.Auditable;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "sms_provider_token_holder")
public class SmsProviderTokenHolder extends Auditable<Long> {

    @Column(name = "token", columnDefinition = "text")
    private String token;

    @Column(name = "expired_date")
    private ZonedDateTime expiredDate;
}
