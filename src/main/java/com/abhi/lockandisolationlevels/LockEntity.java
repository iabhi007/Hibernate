package com.abhi.lockandisolationlevels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lock_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockEntity {

    @Id
    @Column(name = "lock_key")
    private String lockKey;

    @Column(name = "locked_till")
    private LocalDateTime lockedTill;

    @Column(name = "lock_version")
    @Version
    private Long version;
}
