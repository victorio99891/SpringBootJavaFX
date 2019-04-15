package pl.wiktor.management.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wiktor.management.entity.RoleEntity;
import pl.wiktor.management.entity.UserEntity;
import pl.wiktor.management.exceptions.ExceptionInfo;
import pl.wiktor.management.exceptions.ExceptionResolverService;
import pl.wiktor.management.mapper.UserMapper;
import pl.wiktor.management.model.RoleEnum;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.repository.RoleRepository;
import pl.wiktor.management.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserBO> findAllUsers() {
        List<UserEntity> userEntityList = userRepository.findAll();
        return userEntityList.stream().map(userMapper::fromEntityToBO).collect(Collectors.toList());
    }

    public void updateUserData(UserBO userBO) {
        UserEntity userEntity = userRepository.findByEmail(userBO.getEmail()).get();
        RoleEntity roleEntity = roleRepository.findByName(userBO.getRole()).get();
        userEntity.setFirstName(userBO.getFirstName());
        userEntity.setLastName(userBO.getLastName());
        userEntity.setRole(roleEntity);
        userRepository.save(userEntity);
    }

    public boolean checkNewUserCredentials(UserEntity userEntity) {
        Optional<UserEntity> userEntityDatabase = userRepository.findByEmail(userEntity.getEmail());
        if (!userEntityDatabase.isPresent()) {
            RoleEntity roleEntity = roleRepository.findByName(RoleEnum.USER.name()).get();
            String password = userEntity.getPassword();
            userEntity.setPassword(passwordEncoder.encode(password));
            userEntity.setRole(roleEntity);
            userRepository.save(userEntity);
            return true;
        } else {
            ExceptionResolverService.resolve(ExceptionInfo.USER_EXIST);
            return false;
        }
    }


    public UserEntity findOne(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<UserBO> findByParameter(String parameter, String searchValue) {
        Optional<UserEntity> userEntity;
        if (parameter.equals("ID")) {
            userEntity = userRepository.findById(Long.valueOf(searchValue));
            if (userEntity.isPresent()) {
                return Collections.singletonList(userMapper.fromEntityToBO(userEntity.get()));
            } else {
                return Collections.emptyList();
            }
        } else if (parameter.equals("FIRSTNAME")) {
            return userRepository.findByFirstNameContainingIgnoringCase(searchValue).stream().map(userMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("LASTNAME")) {
            return userRepository.findByLastNameContainingIgnoringCase(searchValue).stream().map(userMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("EMAIL")) {
            return userRepository.findByEmailContainingIgnoringCase(searchValue).stream().map(userMapper::fromEntityToBO).collect(Collectors.toList());
        } else if (parameter.equals("ROLE")) {
            return userRepository.findByRole_NameContainingIgnoringCase(searchValue).stream().map(userMapper::fromEntityToBO).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
