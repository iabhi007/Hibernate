package com.abhi.lockandisolationlevels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.time.LocalDateTime;

@Entity
@Table(name = "lock_entity_version_less")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@OptimisticLocking(type = OptimisticLockType.DIRTY)
@DynamicUpdate
public class VersionLessLockEntity {

    @Id
    @Column(name = "lock_key")
    private String lockKey;

    @Column(name = "locked_till")
    private LocalDateTime lockedTill;

    @Column(name = "updated_by")
    private String name;
}