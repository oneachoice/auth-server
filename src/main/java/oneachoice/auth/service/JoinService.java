package oneachoice.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import oneachoice.auth.database.mysql.entity.UserEntity;
import oneachoice.auth.database.mysql.repository.UserRepository;
import oneachoice.auth.dto.request.JoinDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean join(JoinDTO joinDTO) {

        String email = joinDTO.getEmail();
        String password = joinDTO.getPassword();

        if(userRepository.existsByEmail(email)) return false;

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .role("ROLE_USER")
                .build();

        userRepository.save(userEntity);

        return true;
    }
}
