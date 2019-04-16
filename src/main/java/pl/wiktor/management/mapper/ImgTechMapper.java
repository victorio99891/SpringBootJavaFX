package pl.wiktor.management.mapper;

import org.mapstruct.Mapper;
import pl.wiktor.management.entity.ImagingTechniqueEntity;
import pl.wiktor.management.model.ImgTechBO;

@Mapper(componentModel = "spring")
public interface ImgTechMapper {

    ImagingTechniqueEntity fromBOToEntity(ImgTechBO imgTechBO);

    ImgTechBO fromEntityToBO(ImagingTechniqueEntity imagingTechniqueEntity);

}
