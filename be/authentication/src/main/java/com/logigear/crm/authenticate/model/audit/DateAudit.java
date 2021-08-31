package com.logigear.crm.authenticate.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
public abstract class DateAudit implements Serializable {

	private static final long serialVersionUID = 6159010060877568680L;

	@CreatedDate
    @Column(nullable = false, updatable = false,
    name="created_at")
	private Instant createdAt;
	
	@LastModifiedDate
	@Column(nullable = false,
    name="updated_at")
	private Instant updatedAt;

    @Column(name="expired_at")
    private Instant expiredAt;

    @Column(name="token_expired_at")
    private Instant tokenExpiredAt;
    
	public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }
    
    public Instant getTokenExpiredAt() {
        return tokenExpiredAt;
    }

    public void setTokenExpiredAt(Instant tokenExpiredAt) {
        this.tokenExpiredAt = tokenExpiredAt;
    }
}
