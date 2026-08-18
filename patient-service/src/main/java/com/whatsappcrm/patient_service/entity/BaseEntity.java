package com.whatsappcrm.patient_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @Column(nullable = false)
    protected LocalDateTime modifiedDate;

    protected String createdBy;

    protected String updatedBy;

    @Column(nullable = false)
    private boolean deleted = false;

    @Version
    protected Integer version;

    @PrePersist
    public void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}
