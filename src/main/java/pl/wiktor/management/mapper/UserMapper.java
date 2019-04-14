package pl.wiktor.management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.wiktor.management.entity.UserEntity;
import pl.wiktor.management.model.UserBO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role.name")
    UserBO fromEntityToBO(UserEntity userEntity);

    @Mapping(target = "role.name", source = "role")
    UserEntity fromBOToEntity(UserBO userBO);


}
