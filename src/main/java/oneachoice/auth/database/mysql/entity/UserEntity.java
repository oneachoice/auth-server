package oneachoice.auth.database.mysql.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import oneachoice.auth.database.common.entity.BaseTimeEntity;

import java.util.UUID;

@Entity(name = "user")
@Getter
@NoArgsConstructor
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Builder
    public UserEntity(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
