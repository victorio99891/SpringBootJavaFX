package pl.wiktor.management.service;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wiktor.management.entity.UserEntity;
import pl.wiktor.management.exceptions.ExceptionInfo;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.mapper.UserMapper;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.repository.UserRepository;

import java.util.Optional;

@Data
@Service
public class AuthenticationService {

    private UserBO authenticatedUser;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void clearCredentials() {
        this.authenticatedUser = null;
    }

    public boolean checkUserCredentials(String login, String password) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(login);
        if (userEntity.isPresent()) {
            if (passwordEncoder.matches(password, userEntity.get().getPassword())) {
                this.authenticatedUser = userMapper.fromEntityToBO(userEntity.get());
                return true;
            } else {
                ExceptionResolverService.resolve(ExceptionInfo.BAD_CREDENTIALS);
                return false;
            }
        } else {
            ExceptionResolverService.resolve(ExceptionInfo.BAD_CREDENTIALS);
            return false;
        }
    }
}
