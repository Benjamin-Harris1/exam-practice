package kea.exercise.examframework.service.van;

import kea.exercise.examframework.dto.VanDTO;
import kea.exercise.examframework.entity.Van;

import java.util.List;

public interface VanService {
    List<VanDTO> findAll();

    Van convertToEntity(VanDTO vanDTO);

    VanDTO convertToDTO(Van van);
}
