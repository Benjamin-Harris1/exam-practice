package kea.exercise.examframework.service.productorder;

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
        return dto;
    }

    public ProductOrder convertToEntity(ProductOrderDTO productOrderDTO){
        ProductOrder productOrder = new ProductOrder();
        productOrder.setId(productOrderDTO.getId());
        productOrder.setQuantity(productOrderDTO.getQuantity());
        if (productOrderDTO.getProduct() != null) {
            Product product = new Product();
            product.setId(productOrderDTO.getProduct().getId());
            product.setName(productOrderDTO.getProduct().getName());
            product.setPrice(productOrderDTO.getProduct().getPrice());
            product.setWeight(productOrderDTO.getProduct().getWeight());
            productOrder.setProduct(product);
        }
        return productOrder;
    }
}
