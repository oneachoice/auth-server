package oneachoice.auth.database.redis.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "refresh_token")
@Getter
public class RefreshTokenEntity {

    @Id
    private String id;

    @Indexed
    private String token;

    private String email;

    private String role;

    @TimeToLive
    private long ttl;

    @Builder
    public RefreshTokenEntity(String token, long ttl, String role, String email) {
        this.token = token;
        this.ttl = ttl;
        this.role = role;
        this.email = email;
    }
}
