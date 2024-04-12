package kea.exercise.examframework.api;

import kea.exercise.examframework.dto.ProductOrderDTO;
import kea.exercise.examframework.service.productorder.ProductOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productorder")
public class ProductOrderController {

    private ProductOrderService productOrderService;

    public ProductOrderController(ProductOrderService productOrderService){
        this.productOrderService = productOrderService;
    }

    @GetMapping
    public ResponseEntity<List<ProductOrderDTO>> getAllProductOrders(){
        List<ProductOrderDTO> productOrders = productOrderService.findAll();
        return ResponseEntity.ok(productOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductOrderDTO> getProductOrderById(@PathVariable int id){
        ProductOrderDTO productOrder = productOrderService.findById(id);
        return ResponseEntity.ok(productOrder);
    }

    @PostMapping
    public ResponseEntity<ProductOrderDTO> createProductOrder(@RequestBody ProductOrderDTO productOrderDTO){
        ProductOrderDTO productOrder = productOrderService.create(productOrderDTO);
        return ResponseEntity.ok(productOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductOrderDTO> updateProductOrder(@PathVariable int id, @RequestBody ProductOrderDTO productOrderDTO){
        ProductOrderDTO productOrder = productOrderService.update(id, productOrderDTO);
        return ResponseEntity.ok(productOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductOrderDTO> deleteProductOrder(@PathVariable int id){
        productOrderService.deleteProductOrder(id);
        return ResponseEntity.ok().build();
    }
}
