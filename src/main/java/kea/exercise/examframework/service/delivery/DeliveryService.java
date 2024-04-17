package kea.exercise.examframework.service.delivery;

import kea.exercise.examframework.dto.DeliveryDTO;
import kea.exercise.examframework.entity.Delivery;

import java.util.List;

public interface DeliveryService {
    List<DeliveryDTO> findAll();
    DeliveryDTO findById(int id);
    DeliveryDTO create(DeliveryDTO deliveryDTO);
    DeliveryDTO update(int id, DeliveryDTO deliveryDTO);
    void deleteDelivery(int id);

    void assignDeliveryToVan(int deliveryId, int vanId);
    List<DeliveryDTO> getDeliveriesForVan(int vanId);

    DeliveryDTO convertToDTO(Delivery delivery);

    Delivery convertToEntity(DeliveryDTO deliveryDTO);
}
