package oneachoice.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oneachoice.auth.database.redis.entity.RefreshTokenEntity;
import oneachoice.auth.database.redis.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenCahcingService {

    @Value("${jwt.ttl.refresh}")
    private long refreshTokenTTL;

    private final RefreshTokenRepository refreshTokenRepository;

    public void add(String email, String role, String token) {

        RefreshTokenEntity newRefreshTokenEntity = RefreshTokenEntity.builder()
                .email(email)
                .token(token)
                .role(role)
                .ttl(refreshTokenTTL)
                .build();

        refreshTokenRepository.save(newRefreshTokenEntity);
    }

    public void removeByToken(String token) {
        Optional<RefreshTokenEntity> optionalRefreshTokenEntity = refreshTokenRepository.findByToken(token);

        if(optionalRefreshTokenEntity.isEmpty()) {
            return;
        }

        refreshTokenRepository.delete(optionalRefreshTokenEntity.get());
    }

    public boolean existsByToken(String token) {
        return refreshTokenRepository.existsByToken(token);
    }
}
