package uz.alifservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import uz.alifservice.util.SpringSecurityUtil;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Auditable<U> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    protected Long id;

    @CreatedBy
    protected U createdBy;

    @LastModifiedBy
    protected U updatedBy;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN default FALSE")
    private boolean deleted;

    @PrePersist
    public void prePersist() {
        if (SpringSecurityUtil.getCurrentUser() != null) {
            this.createdBy = (U) SpringSecurityUtil.getCurrentUser().getId();
        } else {
            this.createdBy = null;
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (SpringSecurityUtil.getCurrentUser() != null) {
            this.updatedBy = (U) SpringSecurityUtil.getCurrentUser().getId();
        } else {
            this.updatedBy = null;
        }
    }
}
