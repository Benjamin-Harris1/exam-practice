package kea.exercise.examframework.service.delivery;

import kea.exercise.examframework.dto.DeliveryDTO;
import kea.exercise.examframework.entity.Delivery;
import kea.exercise.examframework.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService{

    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public List<DeliveryDTO> findAll() {
        return deliveryRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public DeliveryDTO findById(int id) {
        Optional<Delivery> delivery = deliveryRepository.findById(id);
        return delivery.map(this::convertToDTO).orElseThrow(() -> new RuntimeException("Delivery not found"));
    }

    @Override
    public DeliveryDTO create(DeliveryDTO deliveryDTO) {
        Delivery delivery = convertToEntity(deliveryDTO);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return convertToDTO(savedDelivery);
    }

    @Override
    public DeliveryDTO update(int id, DeliveryDTO deliveryDTO) {
        Optional<Delivery> existingDelivery = deliveryRepository.findById(id);
        if (existingDelivery.isPresent()) {
            Delivery delivery = existingDelivery.get();
            delivery.setDestination(deliveryDTO.getDestination());
            delivery.setDeliveryDate(deliveryDTO.getDeliveryDate());
            delivery.setFromWareHouse(deliveryDTO.getFromWareHouse());
            Delivery updatedDelivery = deliveryRepository.save(delivery);
            return convertToDTO(updatedDelivery);
        }
        throw new RuntimeException("Delivery not found");
    }

    @Override
    public void deleteDelivery(int id) {
        deliveryRepository.deleteById(id);
    }


    public DeliveryDTO convertToDTO(Delivery delivery) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(delivery.getId());
        dto.setDestination(delivery.getDestination());
        dto.setDeliveryDate(delivery.getDeliveryDate());
        dto.setFromWareHouse(delivery.getFromWareHouse());
        return dto;
    }

    public Delivery convertToEntity(DeliveryDTO deliveryDTO) {
        Delivery entity = new Delivery();
        entity.setId(deliveryDTO.getId());
        entity.setDestination(deliveryDTO.getDestination());
        entity.setDeliveryDate(deliveryDTO.getDeliveryDate());
        entity.setFromWareHouse(deliveryDTO.getFromWareHouse());
        return entity;
    }


}
