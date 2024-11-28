package oneachoice.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinDTO {

    @Email(message = "유효하지 않은 이메일입니다.")
    private String email;
    
    @NotBlank(message = "유요하지 않은 비밀번호입니다.") // 비밀번호 정책에 따라 제약사항이 변경돼야함
    private String password;
}
