package pl.wiktor.management.mapper;

import org.mapstruct.Mapper;
import pl.wiktor.management.entity.UserEntity;
import pl.wiktor.management.model.UserBO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserBO fromEntityToBO(UserEntity userEntity);

    UserEntity fromBOToEntity(UserBO userBO);


}
