package kea.exercise.examframework.service.van;

import kea.exercise.examframework.dto.DeliveryDTO;
import kea.exercise.examframework.dto.VanDTO;
import kea.exercise.examframework.entity.Van;
import kea.exercise.examframework.repository.VanRepository;
import kea.exercise.examframework.service.delivery.DeliveryService;
import kea.exercise.examframework.utils.DeliveryUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VanServiceImpl implements VanService {

    private final VanRepository vanRepository;
    private final DeliveryService deliveryService;

    public VanServiceImpl(VanRepository vanRepository, DeliveryService deliveryService) {
        this.vanRepository = vanRepository;
        this.deliveryService = deliveryService;
    }

    @Override
    public List<VanDTO> findAll() {
    List<Van> vans = vanRepository.findAll();
    return vans.stream().map(van -> {
        // Calculate the total weight of deliveries for each van
        double totalWeight = van.getDeliveries().stream()
                .mapToDouble(DeliveryUtils::calculateTotalWeight)
                .sum();
        // Calculate the remaining capacity
        double remainingCapacity = van.getCapacity() - (totalWeight / 1000);

        // Convert Van to VanDTO, including the calculated remaining capacity
        VanDTO vanDTO = convertToDTO(van);
        vanDTO.setRemainingCapacity((int) remainingCapacity);
        return vanDTO;
    }).collect(Collectors.toList());
}

    @Override
    public Van convertToEntity(VanDTO vanDTO) {
        Van entity = new Van();
        entity.setCapacity(vanDTO.getCapacity());
        entity.setBrand(vanDTO.getBrand());
        entity.setModel(vanDTO.getModel());
        return entity;
    }

    @Override
    public VanDTO convertToDTO(Van van) {
        VanDTO dto = new VanDTO();
        dto.setId(van.getId());
        dto.setBrand(van.getBrand());
        dto.setModel(van.getModel());
        dto.setCapacity(van.getCapacity());
        dto.setRemainingCapacity(van.getRemainingCapacity());

    if (van.getDeliveries() != null) {
        List<DeliveryDTO> deliveryDTOs = van.getDeliveries().stream()
                .map(deliveryService::convertToDTO) // Correctly reference the convertToDTO method
                .collect(Collectors.toList());
        dto.setDeliveries(deliveryDTOs);
    }
    return dto;
}
}
