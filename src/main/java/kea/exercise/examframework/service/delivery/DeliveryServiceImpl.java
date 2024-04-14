package kea.exercise.examframework.service.delivery;

import kea.exercise.examframework.dto.DeliveryDTO;
import kea.exercise.examframework.dto.ProductDTO;
import kea.exercise.examframework.dto.ProductOrderDTO;
import kea.exercise.examframework.entity.Delivery;
import kea.exercise.examframework.entity.Product;
import kea.exercise.examframework.entity.ProductOrder;
import kea.exercise.examframework.entity.Van;
import kea.exercise.examframework.repository.DeliveryRepository;
import kea.exercise.examframework.repository.VanRepository;
import kea.exercise.examframework.service.product.ProductService;
import kea.exercise.examframework.service.productorder.ProductOrderService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Optional<Delivery> existingDelivery = deliveryRepository.findById(id);
        if (existingDelivery.isPresent()) {
            Delivery delivery = existingDelivery.get();
            delivery.setDeliveryDate(deliveryDTO.getDeliveryDate());
            delivery.setFromWareHouse(deliveryDTO.getFromWareHouse());
            delivery.setDestination(deliveryDTO.getDestination());
    
            // Handle existing and new product orders
            List<ProductOrder> updatedProductOrders = deliveryDTO.getProductOrders().stream()
                .map(dto -> {
                    if (dto.getId() == 0) { // ID 0 indicates a new ProductOrder
                        ProductOrder newProductOrder = productOrderService.convertToEntity(dto);
                        newProductOrder.setDelivery(delivery);
                        return newProductOrder;
                    } else {
                        Optional<ProductOrder> existingProductOrder = delivery.getProductOrders().stream()
                            .filter(po -> po.getId() == dto.getId())
                            .findFirst();
                        if (existingProductOrder.isPresent()) {
                            ProductOrder productOrder = existingProductOrder.get();
                            productOrderService.updateFromDTO(productOrder, dto);
                            return productOrder;
                        } else {
                            // Handle case where ProductOrder ID is provided but does not exist in the current Delivery
                            throw new RuntimeException("ProductOrder with ID " + dto.getId() + " not found in Delivery");
                        }
                    }
                })
                .collect(Collectors.toList());
    
            delivery.setProductOrders(updatedProductOrders);
            Delivery savedDelivery = deliveryRepository.save(delivery);
            return convertToDTO(savedDelivery);
        } else {
            throw new RuntimeException("Delivery not found");
        }
    }

    @Override
    public void deleteDelivery(int id) {
        deliveryRepository.deleteById(id);
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
        double totalWeightInGrams = calculateTotalWeight(delivery);
        double vanCapacityInGrams = van.getCapacity() * 1000;
    
        if (totalWeightInGrams > vanCapacityInGrams) {
            throw new RuntimeException("The total weight of the delivery exceeds the van's capacity");
        }
    
        delivery.setVan(van);
        deliveryRepository.save(delivery);
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

    private double calculateTotalWeight(Delivery delivery) {
        return delivery.getProductOrders().stream()
                .mapToDouble(productOrder -> productOrder.getProduct().getWeight() * productOrder.getQuantity())
                .sum();
    }

}
