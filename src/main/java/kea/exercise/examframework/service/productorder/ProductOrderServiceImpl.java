package kea.exercise.examframework.service.productorder;

import kea.exercise.examframework.dto.ProductDTO;
import kea.exercise.examframework.dto.ProductOrderDTO;
import kea.exercise.examframework.entity.Product;
import kea.exercise.examframework.entity.ProductOrder;
import kea.exercise.examframework.repository.ProductOrderRepository;
import kea.exercise.examframework.service.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final ProductService productService;

    public ProductOrderServiceImpl(ProductOrderRepository productOrderRepository, ProductService productService) {
        this.productOrderRepository = productOrderRepository;
        this.productService = productService;
    }


    @Override
    public List<ProductOrderDTO> findAll() {
        return productOrderRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Override
    public ProductOrderDTO findById(int id) {
        return productOrderRepository.findById(id).map(this::convertToDTO).orElseThrow(() -> new RuntimeException("ProductOrder not found"));
    }

    @Override
    public ProductOrderDTO create(ProductOrderDTO productOrderDTO) {
        ProductOrder productOrder = convertToEntity(productOrderDTO);
        ProductOrder savedProductOrder = productOrderRepository.save(productOrder);
        return convertToDTO(savedProductOrder);
    }

    @Override
    public ProductOrderDTO update(int id, ProductOrderDTO productOrderDTO) {
        ProductOrder productOrder = convertToEntity(productOrderDTO);
        productOrder.setId(id);
        ProductOrder savedProductOrder = productOrderRepository.save(productOrder);
        return convertToDTO(savedProductOrder);
    }

    @Override
    public void deleteProductOrder(int id) {
        productOrderRepository.deleteById(id);
    }

    public ProductOrderDTO convertToDTO(ProductOrder productOrder){
        ProductOrderDTO dto = new ProductOrderDTO();
        dto.setId(productOrder.getId());
        dto.setQuantity(productOrder.getQuantity());

        // Convert product ent to productDTO
        if (productOrder.getProduct() != null){
            ProductDTO productDTO = productService.convertToDTO(productOrder.getProduct());
            dto.setProductId(productDTO.getId());
        }
        return dto;
    }

    public ProductOrder convertToEntity(ProductOrderDTO productOrderDTO){
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(productOrderDTO.getId());
        productOrder.setQuantity(productOrderDTO.getQuantity());

        // Set product based on product id
        ProductDTO productDTO = productService.findById(productOrderDTO.getProductId());
        Product product = productService.convertToEntity(productDTO);
        productOrder.setProduct(product);
        return productOrder;
    }

    public void updateFromDTO(ProductOrder productOrder, ProductOrderDTO productOrderDTO) {
        // Check for null values to avoid NullPointerException
        if (productOrder == null || productOrderDTO == null) {
            throw new IllegalArgumentException("ProductOrder and ProductOrderDTO cannot be null");
        }
    
        // Update fields from productOrderDTO to productOrder
        productOrder.setQuantity(productOrderDTO.getQuantity());

        if (productOrderDTO.getProductId() > 0) {
            ProductDTO productDTO = productService.findById(productOrderDTO.getProductId());
            // Convert to entity
            Product product = productService.convertToEntity(productDTO);
            productOrder.setProduct(product);
        }
    }
}
