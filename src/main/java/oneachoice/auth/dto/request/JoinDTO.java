package oneachoice.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinDTO {

    @Email
    private String email;
    
    @NotBlank // 비밀번호 정책에 따라 제약사항이 변경돼야함
    private String password;
}
