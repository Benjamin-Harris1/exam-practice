package kea.exercise.examframework.service.delivery;

import kea.exercise.examframework.dto.DeliveryDTO;
import kea.exercise.examframework.dto.ProductOrderDTO;
import kea.exercise.examframework.entity.Delivery;
import kea.exercise.examframework.entity.ProductOrder;
import kea.exercise.examframework.entity.Van;
import kea.exercise.examframework.repository.DeliveryRepository;
import kea.exercise.examframework.repository.VanRepository;
import kea.exercise.examframework.service.productorder.ProductOrderService;

import kea.exercise.examframework.utils.DeliveryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService{

    private final DeliveryRepository deliveryRepository;
    private final ProductOrderService productOrderService;
    private final VanRepository vanRepository;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, ProductOrderService productOrderService, VanRepository vanRepository) {
        this.deliveryRepository = deliveryRepository;
        this.productOrderService = productOrderService;
        this.vanRepository = vanRepository;
    }

    @Override
    public List<DeliveryDTO> findAll() {
        return deliveryRepository.findAllByIsActiveTrue().stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public DeliveryDTO findById(int id) {
        Optional<Delivery> delivery = deliveryRepository.findById(id)
                .filter(Delivery::isActive);
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
        delivery.setActive(true);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return convertToDTO(savedDelivery);
    }

    @Override
    @Transactional
    public DeliveryDTO update(int id, DeliveryDTO deliveryDTO) {
        Delivery existingDelivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        // Update simple fields
        existingDelivery.setDeliveryDate(deliveryDTO.getDeliveryDate());
        existingDelivery.setFromWareHouse(deliveryDTO.getFromWareHouse());
        existingDelivery.setDestination(deliveryDTO.getDestination());

        // IDs of product orders that should remain
        Set<Integer> dtoProductOrderIds = deliveryDTO.getProductOrders().stream()
                .map(ProductOrderDTO::getId)
                .collect(Collectors.toSet());

        // Find and remove product orders not included in the DTO
        List<ProductOrder> toRemove = existingDelivery.getProductOrders().stream()
                .filter(po -> !dtoProductOrderIds.contains(po.getId()))
                .collect(Collectors.toList());
        toRemove.forEach(po -> {
            existingDelivery.getProductOrders().remove(po);
            productOrderService.deleteProductOrder(po.getId());
        });

        // Update existing and add new product orders
        List<ProductOrder> updatedProductOrders = deliveryDTO.getProductOrders().stream()
                .map(dto -> {
                    if (dto.getId() == 0) { // New ProductOrder
                        ProductOrder newProductOrder = productOrderService.convertToEntity(dto);
                        newProductOrder.setDelivery(existingDelivery);
                        return newProductOrder;
                    } else {
                        // Existing ProductOrder, update it
                        return existingDelivery.getProductOrders().stream()
                                .filter(po -> po.getId() == (dto.getId()))
                                .peek(po -> productOrderService.updateFromDTO(po, dto))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("ProductOrder not found for ID " + dto.getId()));
                    }
                })
                .collect(Collectors.toList());

        // Clear the current product orders and add the updated ones
        existingDelivery.getProductOrders().clear();
        existingDelivery.getProductOrders().addAll(updatedProductOrders);

        // Save the updated delivery
        Delivery savedDelivery = deliveryRepository.save(existingDelivery);
        return convertToDTO(savedDelivery);
    }

    @Override
    public void deleteDelivery(int id) {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(() -> new RuntimeException("Delivery not found"));
        delivery.setActive(false);
        deliveryRepository.save(delivery);
    }

    @Override
    public void assignDeliveryToVan(int deliveryId, int vanId) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(deliveryId);
        Optional<Van> vanOpt = vanRepository.findById(vanId);
    
        if (!deliveryOpt.isPresent()) {
            throw new RuntimeException("Delivery not found");
        }
    
        if (!vanOpt.isPresent()) {
            throw new RuntimeException("Van not found");
        }
    
        Delivery delivery = deliveryOpt.get();
        Van van = vanOpt.get();
    
        // Calculate the weight in grams
        double newDeliveryWeightInGrams = DeliveryUtils.calculateTotalWeight(delivery);
        double existingDeliveriesWeight = van.getDeliveries().stream()
                .mapToDouble(DeliveryUtils::calculateTotalWeight)
                .sum();
        // Calculate the total weight including the new delivery
        double totalWeightIncludingNewDelivery = existingDeliveriesWeight + newDeliveryWeightInGrams;
        double vanCapacityInGrams = van.getCapacity() * 1000;
    
        // If total weight exceeds the van's capacity, throw an exception
        if (totalWeightIncludingNewDelivery > vanCapacityInGrams) {
            throw new RuntimeException("The total weight of the delivery exceeds the van's capacity");
        }
    
        // Update the van's remaining capacity
        double remainingCapacityInKg = (vanCapacityInGrams - totalWeightIncludingNewDelivery) / 1000;
        van.setRemainingCapacity((int) remainingCapacityInKg);
    
        // Persist changes to both the delivery and the van
        delivery.setVan(van);
        deliveryRepository.save(delivery);
        vanRepository.save(van);
    }

    @Override
    public List<DeliveryDTO> getDeliveriesForVan(int vanId) {
        return deliveryRepository.findAllByVanId(vanId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    public DeliveryDTO convertToDTO(Delivery delivery) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(delivery.getId());
        dto.setDeliveryDate(delivery.getDeliveryDate());
        dto.setFromWareHouse(delivery.getFromWareHouse());
        dto.setDestination(delivery.getDestination());
        dto.setActive(delivery.isActive());
        // Convert productorder to dto and add to list
        List<ProductOrderDTO> productOrderDTOS = delivery.getProductOrders().stream()
        .map(productOrderService::convertToDTO)
        .collect(Collectors.toList());
        dto.setProductOrders(productOrderDTOS);

        if (delivery.getVan() != null) {
            dto.setVanId(delivery.getVan().getId());
        }
        return dto;
    }

    public Delivery convertToEntity(DeliveryDTO deliveryDTO) {
        Delivery entity = new Delivery();
        entity.setId(deliveryDTO.getId());
        entity.setDeliveryDate(deliveryDTO.getDeliveryDate());
        entity.setFromWareHouse(deliveryDTO.getFromWareHouse());
        entity.setDestination(deliveryDTO.getDestination());
        entity.setActive(deliveryDTO.isActive());

        List<ProductOrder> productOrders = deliveryDTO.getProductOrders().stream()
        .map(productOrderService::convertToEntity)
        .collect(Collectors.toList());
        entity.setProductOrders(productOrders);
        return entity;
    }

}
