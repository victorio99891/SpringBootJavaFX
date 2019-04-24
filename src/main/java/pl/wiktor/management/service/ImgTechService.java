package pl.wiktor.management.service;

import org.springframework.stereotype.Service;
import pl.wiktor.management.mapper.ImgTechMapper;
import pl.wiktor.management.model.ImgTechBO;
import pl.wiktor.management.repository.ImgTechRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImgTechService {

    private final ImgTechRepository imgTechRepository;
    private final ImgTechMapper imgTechMapper;

    public ImgTechService(ImgTechRepository imgTechRepository, ImgTechMapper imgTechMapper) {
        this.imgTechRepository = imgTechRepository;
        this.imgTechMapper = imgTechMapper;
    }


    public List<ImgTechBO> findAllImagingTechniques() {
        return imgTechRepository.findAll().stream().map(imgTechMapper::fromEntityToBO).collect(Collectors.toList());
    }

    ;


}
