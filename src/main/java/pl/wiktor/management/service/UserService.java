package pl.wiktor.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.management.entity.UserEntity;
import pl.wiktor.management.mapper.UserMapper;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserBO> findAllUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();
        return userEntityList.stream().map(userMapper::fromEntityToBO).collect(Collectors.toList());
    }


    public UserEntity findOne(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
