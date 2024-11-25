package oneachoice.auth.dto.inside;

import lombok.Builder;
import lombok.Data;
import oneachoice.auth.database.mysql.entity.UserEntity;

@Data
@Builder
public class UserDTO {

    private String email;
    private String password;
    private String role;

    public static UserDTO of(UserEntity userEntity) {
        return UserDTO.builder()
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .role(userEntity.getRole())
                .build();
    }
}
