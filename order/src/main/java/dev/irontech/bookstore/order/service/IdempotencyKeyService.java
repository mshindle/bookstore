package dev.irontech.bookstore.order.service;

import dev.irontech.bookstore.order.models.IdempotencyKey;
import dev.irontech.bookstore.order.repository.IdempotencyKeyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IdempotencyKeyService {

    public static final Long IDEMPOTENCY_KEY_LOCK_TIMEOUT_IN_SECONDS = 10L; // 10 seconds

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    /**
     * Gets or creates an idempotency key.
     * The key is locked for a period of time after it is created.
     * Throws an exception if the key exists and is locked.
     *
     * @param key    - the idempotency key.
     * @param userId - the user id.
     * @return the idempotency key.
     * @throws IllegalStateException if the key exists and is locked.
     */
    @Transactional
    public IdempotencyKey getOrCreateIdempotencyKeyByKeyAndUserId(String key, String userId) {
        // Fetch with pessimistic lock
        Optional<IdempotencyKey> optionalKey = idempotencyKeyRepository.findByIdempotencyKeyAndUserId(key, userId);
        IdempotencyKey idempotencyKey;

        if (optionalKey.isPresent()) {
            idempotencyKey = optionalKey.get();
            if (checkIfIsLocked(idempotencyKey)) {
                throw new IllegalStateException("Idempotency key is locked");
            }
        } else {
            idempotencyKey = new IdempotencyKey();
            idempotencyKey.setIdempotencyKey(key);
            idempotencyKey.setUserId(userId);
        }

        idempotencyKey.setLockedAt(LocalDateTime.now());
        return idempotencyKeyRepository.save(idempotencyKey);
    }

    public boolean checkIfIsLocked(IdempotencyKey idempotencyKey) {
        return idempotencyKey.getLockedAt()
                .plusSeconds(IdempotencyKeyService.IDEMPOTENCY_KEY_LOCK_TIMEOUT_IN_SECONDS)
                .isAfter(LocalDateTime.now());
    }

}
