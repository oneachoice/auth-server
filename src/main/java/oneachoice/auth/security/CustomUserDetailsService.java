package oneachoice.auth.security;

import lombok.RequiredArgsConstructor;
import oneachoice.auth.database.mysql.entity.UserEntity;
import oneachoice.auth.database.mysql.repository.UserRepository;
import oneachoice.auth.dto.inside.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // DB에서 User가 있는지 조회
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);


        if(optionalUserEntity.isPresent()) {
            // CustomUserDetails를 사용하여 AuthenticationManager가 검증 함
            return new CustomUserDetails(UserDTO.of(optionalUserEntity.get()));
        }

        return null;
    }
}
