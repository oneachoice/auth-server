package oneachoice.auth.repository;

import oneachoice.auth.database.redis.entity.RefreshTokenEntity;
import oneachoice.auth.database.redis.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void test() {

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token("test")
                .email("oneachoice@gmail.com")
                .role("admin")
                .ttl(10000)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        refreshTokenRepository.findById(refreshTokenEntity.getId());

        refreshTokenRepository.count();

        refreshTokenRepository.delete(refreshTokenEntity);
    }
}