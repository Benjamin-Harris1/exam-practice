package kea.exercise.examframework.service.delivery;

import kea.exercise.examframework.dto.DeliveryDTO;
import kea.exercise.examframework.dto.ProductOrderDTO;
import kea.exercise.examframework.entity.Delivery;
import kea.exercise.examframework.entity.ProductOrder;
import kea.exercise.examframework.repository.DeliveryRepository;
import kea.exercise.examframework.service.productorder.ProductOrderService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService{

    private final DeliveryRepository deliveryRepository;
    private final ProductOrderService productOrderService;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, ProductOrderService productOrderService) {
        this.deliveryRepository = deliveryRepository;
        this.productOrderService = productOrderService;
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
        // Handle productOrders
        if (deliveryDTO.getProductOrders() != null) {
            delivery.setProductOrders(deliveryDTO.getProductOrders().stream()
            .map(productOrderDTO -> {
                ProductOrder productOrder = productOrderService.convertToEntity(productOrderDTO);
                productOrder.setDelivery(delivery);
                return productOrder;
            })
            .collect(Collectors.toList()));
        }
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return convertToDTO(savedDelivery);
    }

    @Override
    public DeliveryDTO update(int id, DeliveryDTO deliveryDTO) {
        Optional<Delivery> existingDeliveryOpt = deliveryRepository.findById(id);
        if (existingDeliveryOpt.isPresent()) {
            Delivery existingDelivery = existingDeliveryOpt.get();
            existingDelivery.setDestination(deliveryDTO.getDestination());
            existingDelivery.setDeliveryDate(deliveryDTO.getDeliveryDate());
            existingDelivery.setFromWareHouse(deliveryDTO.getFromWareHouse());

            List<ProductOrder> productOrders = deliveryDTO.getProductOrders().stream()
                .map(productOrderDTO -> {
                    ProductOrder productOrder = productOrderService.convertToEntity(productOrderDTO);
                    productOrder.setDelivery(existingDelivery); // Link to the existing delivery
                    return productOrder;
                })
                .collect(Collectors.toList());
            existingDelivery.setProductOrders(productOrders);
            Delivery updatedDelivery = deliveryRepository.save(existingDelivery);
            return convertToDTO(updatedDelivery);
        } else {
            throw new RuntimeException("Delivery not found");
        }
    }

    @Override
    public void deleteDelivery(int id) {
        deliveryRepository.deleteById(id);
    }


    public DeliveryDTO convertToDTO(Delivery delivery) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(delivery.getId());
        dto.setDeliveryDate(delivery.getDeliveryDate());
        dto.setFromWareHouse(delivery.getFromWareHouse());
        dto.setDestination(delivery.getDestination());
        // Convert productorder to dto and add to list
        List<ProductOrderDTO> productOrderDTOS = delivery.getProductOrders().stream()
        .map(productOrderService::convertToDTO)
        .collect(Collectors.toList());
        dto.setProductOrders(productOrderDTOS);
        return dto;
    }

    public Delivery convertToEntity(DeliveryDTO deliveryDTO) {
        Delivery entity = new Delivery();
        entity.setId(deliveryDTO.getId());
        entity.setDeliveryDate(deliveryDTO.getDeliveryDate());
        entity.setFromWareHouse(deliveryDTO.getFromWareHouse());
        entity.setDestination(deliveryDTO.getDestination());

        List<ProductOrder> productOrders = deliveryDTO.getProductOrders().stream()
        .map(productOrderService::convertToEntity)
        .collect(Collectors.toList());
        entity.setProductOrders(productOrders);
        return entity;
    }


}
