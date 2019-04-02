package pl.wiktor.springboot_javafx_example.service;

import org.springframework.stereotype.Service;
import pl.wiktor.springboot_javafx_example.entity.User;
import pl.wiktor.springboot_javafx_example.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User findOne(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
