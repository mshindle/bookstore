package dev.irontech.bookstore.order.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "IDX_USER_ID_AND_KEY", columnList = "userId, idempotencyKey", unique = true)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdempotencyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, name = "idempotency_key")
    private String idempotencyKey;

    @Column(nullable = false,name = "user_id")
    private String userId;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "locked_at")
    private LocalDateTime lockedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.lockedAt = now;
    }

}