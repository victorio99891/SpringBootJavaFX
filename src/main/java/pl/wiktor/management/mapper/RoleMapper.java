package pl.wiktor.management.mapper;

import org.mapstruct.Mapper;
import pl.wiktor.management.entity.RoleEntity;
import pl.wiktor.management.model.RoleBO;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleBO fromEntityToBO(RoleEntity roleEntity);

    RoleEntity fromBOToEntity(RoleBO roleBO);

}
