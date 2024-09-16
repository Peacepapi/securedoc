package com.piery.securedoc.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.piery.securedoc.exception.ApiException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt"}, allowGetters = true)
public abstract class Auditable {
    @Id
    @SequenceGenerator(name= "primary_key_seq", sequenceName = "primary_key_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_key_seq")
    @Column(name= "id", updatable = false)
    private Long id;

    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();

    @NotNull
    private Long createdBy;

    @NotNull
    private Long updatedBy;

    @NotNull
    @CreatedDate
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedDate
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

//    Before saving an entity this check will run
    @PrePersist
    public void beforePersist() {
        var userId = 1L;
        if(userId == null) {
            throw new ApiException("Cannot persist entity without user ID in Request Context for this thread");
        }

        setCreatedAt(now());
        setCreatedBy(userId);
        setUpdatedBy(userId);
        setUpdatedAt(now());
    }

//    Before updating an entity this check will run
    @PrePersist
    public void beforeUpdate() {
        var userId = 1L;
        if(userId == null) {
            throw new ApiException("Cannot update entity without user ID in Request Context for this thread");
        }

        setCreatedBy(userId);
        setUpdatedBy(userId);
    }
}

/*
Create a thread for each request separate
 */