package kea.exercise.examframework.api;

import kea.exercise.examframework.dto.DeliveryDTO;
import kea.exercise.examframework.service.delivery.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries(){
        List<DeliveryDTO> deliveries = deliveryService.findAll();
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(int id){
        DeliveryDTO delivery = deliveryService.findById(id);
        return ResponseEntity.ok(delivery);
    }

    @PostMapping
    public ResponseEntity<DeliveryDTO> createDelivery(DeliveryDTO deliveryDTO){
        DeliveryDTO delivery = deliveryService.create(deliveryDTO);
        return ResponseEntity.ok(delivery);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDTO> updateDelivery(int id, DeliveryDTO deliveryDTO){
        DeliveryDTO delivery = deliveryService.update(id, deliveryDTO);
        return ResponseEntity.ok(delivery);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeliveryDTO> deleteDelivery(int id){
        deliveryService.deleteDelivery(id);
        return ResponseEntity.ok().build();
    }
}
