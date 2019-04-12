package pl.wiktor.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.management.entity.UserEntity;
import pl.wiktor.management.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserEntity findOne(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
