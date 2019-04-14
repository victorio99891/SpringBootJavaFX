package pl.wiktor.management.service;

import lombok.Data;
import org.springframework.stereotype.Component;
import pl.wiktor.management.mapper.RoleMapper;
import pl.wiktor.management.model.RoleBO;
import pl.wiktor.management.model.UserBO;
import pl.wiktor.management.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class AppContext {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    private UserBO authenticatedUser;
    private UserBO userToEdit;
    private List<RoleBO> roleBOList;

    private AppContext(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.roleBOList = this.roleRepository.findAll().stream().map(roleMapper::fromEntityToBO).collect(Collectors.toList());
    }


}
